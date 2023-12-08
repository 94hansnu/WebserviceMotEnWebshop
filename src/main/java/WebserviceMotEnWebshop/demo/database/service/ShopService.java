package WebserviceMotEnWebshop.demo.database.service;

import WebserviceMotEnWebshop.demo.database.entity.*;
import WebserviceMotEnWebshop.demo.database.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ArticleRepository articleRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartDetailRepository shoppingCartDetailRepository;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;
    @PersistenceContext
    EntityManager entityManager;


    /* Det skall finnas följande funktioner för shop:

    *   FÅ ALLA / SPECIFIK ARTIKEL -> STARTSIDAN, VAD SOM FINNS TILLGÄNGLIGT <1>

    *  (C) LÄGGA TILL ARTIKEL I KUNDKORG ----------------------------------- <2>
    *  (R) SE ALLA ARTIKEL, DESS ANTAL OCH PRIS I KUNDKORG ----------------- <3>
    *  (U) UPPDATERA ANTAL ARTIKEL I KUNDKORG ------------------------------ <2>
    *  (D) TA BORT ARTIKEL I KUNDKORG -------------------------------------- <2>


    * TA BORT HELA KUNDKORG INNEHÅLL -------------------------------------------------- <4>
    * KÖP ALLT I HELA KUNDKORG (SPARA TILL HISTORY -> RADERA RADER I KUNDDETALJKORG) -- <5>
     */




    public List<Article> getArticles(String filter) { // <1>
        if (filter == null || filter.isEmpty()) {
            return articleRepository.findAll();
        }
        else return articleRepository.search(filter);
    }
    // Borde fungera på att lägga till ny, radera och uppdatera kundkorg. Mer testning krävs
    // Om finns tid, gör egna Exeptions
    @Transactional
    public ShoppingCartDetail addItem(User user, Article article, int quantity) { // <2>

        User existingUser = getUser(user);

        ShoppingCart cart = getShoppingCart(existingUser);

        Article fetchedArticle = getArticle(article);

        // Kollar om det finns ett matchande objekt artikel i befintlig kundkorg
        Optional<ShoppingCartDetail> existingItem = cart.getCartDetail().stream()
                .filter(cartDetail -> cartDetail.getArticle().equals(fetchedArticle)).findFirst();

        if (existingItem.isPresent()) {
            ShoppingCartDetail item = existingItem.get();
            // Kontroll om användare försöker att ta bort alla av den artikel
            if (quantity + item.getQuantity() <= 0) {
                shoppingCartDetailRepository.delete(item);

                throw new RuntimeException("Artikel raderad."); // Borde ändras till ett eget fel.
            }
            item.setQuantity(item.getQuantity() + quantity);
            return shoppingCartDetailRepository.save(item);
        } else {
            ShoppingCartDetail newItem = new ShoppingCartDetail(cart, article, quantity);
            return shoppingCartDetailRepository.save(newItem);
        }
    }


    @Transactional // denna bör funka bättre, inte klar, behövs
    public ShoppingCartDetail add(String username, String articleName, int quantity) {
        User user = getUser(username);
        Article article = articleRepository.findByName(articleName).orElseThrow();
        ShoppingCart cart = getShoppingCart(user);
        Optional<ShoppingCartDetail> existingItem = cart.getCartDetail().stream()
                .filter(detail -> detail.getArticle().equals(article)).findFirst();

        if (existingItem.isPresent()) {
            ShoppingCartDetail item = existingItem.get();
            if (quantity + item.getQuantity() <= 0) {
                //deleteArticleInCart(item);
                ShoppingCart cartt = item.getCart();
                cartt.getCartDetail().remove(item);
                shoppingCartDetailRepository.delete(item);
                throw new RuntimeException("Artikel raderad.");
            }
            item.setQuantity(item.getQuantity() + quantity);
            return shoppingCartDetailRepository.save(item);
        } else {
            ShoppingCartDetail newItem = new ShoppingCartDetail(cart, article, quantity);
            return shoppingCartDetailRepository.save(newItem);
        }
    }

    @Transactional
    public List<ShoppingCartDetail> getShoppingCartDetails(User user) { // <3>

        User existingUser = getUser(user);

        Optional<ShoppingCart> cart = shoppingCartRepository.findByUser(user);
        if (cart.isPresent()) {
            return cart.get().getCartDetail();
        } else return Collections.emptyList();
    }
    @Transactional
    public void removeAllCartItems(String username) { // <4>
        User existingUser = getUser(username);
        ShoppingCart cart = getShoppingCart(existingUser);
        shoppingCartDetailRepository.deleteAllByCart(cart);
    }
    @Transactional
    public void removeArticleFromCart(String username, String articleName) {
        // lägg in kontroll som kollar om artikeln finns i användarens korg
        User existingUser = getUser(username);
        ShoppingCart cart = getShoppingCart(existingUser);
        Article article = getArticle(articleName);
        shoppingCartDetailRepository.deleteByArticle(article);
    }
    @Transactional
    public void buy(User user) { // <5>
        User existingUser = getUser(user);
        ShoppingCart cart = getShoppingCart(existingUser);

        for (ShoppingCartDetail detail : cart.getCartDetail()) {
            History history = new History();
            history.setUser(existingUser);
            Article article = getArticle(detail.getArticle());
            history.setArticle(article);
            history.setPrice(article.getPrice());
            history.setQuantity(detail.getQuantity());
            historyRepository.save(history);
        }

        shoppingCartDetailRepository.deleteAllByCart(cart);
        cart.getCartDetail().clear();
        shoppingCartRepository.save(cart);

    }
    private Article getArticle(Article article) {
        Optional<Article> existingArticle = articleRepository.findById(article.getId());
        if (existingArticle.isEmpty()) {
            throw new RuntimeException("Artikel finns inte.");
        }
        Article fetchedArticle = existingArticle.get();
        return fetchedArticle;
    }
    private Article getArticle(String name) {
        Optional<Article> existingArticle = articleRepository.findByName(name);
        if (existingArticle.isEmpty()) {
            throw new RuntimeException("Artikel finns inte." + name);
        }
        Article fetchedArticle = existingArticle.get();
        return fetchedArticle;
    }
    public User getUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException("Användaren hittas inte");
        return optionalUser.get();
    }

    private ShoppingCart getShoppingCart(User existingUser) {
        Optional<ShoppingCart> existingCart = shoppingCartRepository.findByUser(existingUser);
        ShoppingCart cart;
        if (existingCart.isPresent()) {
            cart = existingCart.get();
        } else {
            ShoppingCart newCart = new ShoppingCart();
            newCart.setUser(existingUser);
            cart = shoppingCartRepository.save(newCart);
        }
        return cart;
    }

    private User getUser(User user) {
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException("Användaren hittas inte.");

        User existingUser = optionalUser.get();
        return existingUser;
    }



}
