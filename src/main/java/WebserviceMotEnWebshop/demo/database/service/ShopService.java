package WebserviceMotEnWebshop.demo.database.service;

import WebserviceMotEnWebshop.demo.database.entity.*;
import WebserviceMotEnWebshop.demo.database.repository.*;
import WebserviceMotEnWebshop.demo.service.HistoryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Autowired
    private HistoryService historyService;
    @PersistenceContext
    EntityManager entityManager;


    /* Det skall finnas följande funktioner för shop:

    *   FÅ ALLA / SPECIFIK ARTIKEL -> STARTSIDAN, VAD SOM FINNS TILLGÄNGLIGT <1>

    *  (C) LÄGGA TILL ARTIKEL I KUNDKORG ----------------------------------- <2>
    *  (R) SE ALLA ARTIKEL, DESS ANTAL OCH PRIS I KUNDKORG ----------------- <3>
    *  (U) UPPDATERA ANTAL ARTIKEL I KUNDKORG ------------------------------ <2>
    *  (D) TA BORT ARTIKEL I KUNDKORG -------------------------------------- <6,2>


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

    @Transactional // <2> // klar fungerar för uppdatera antal (+ & -), lägga till nytt
    public ShoppingCartDetail addOrUpdateArticleInCart(String username, String articleName, int quantity) {
        User user = getUser(username);
        Article article = getArticle(articleName);
        ShoppingCart cart = getShoppingCart(user);

        Optional<ShoppingCartDetail> existingItem = cart.getCartDetail().stream()
                .filter(detail -> detail.getArticle().equals(article)).findFirst();

        if (existingItem.isPresent() &&
                (quantity + existingItem.get().getQuantity() >= 1)) {

            ShoppingCartDetail item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return shoppingCartDetailRepository.save(item);

        }
        else if (!existingItem.isPresent() && quantity >= 1) {

            ShoppingCartDetail newItem = new ShoppingCartDetail(cart, article, quantity);
            return shoppingCartDetailRepository.save(newItem);
        }
        else throw new RuntimeException("Du försöker att ta bort fler än vad du har,\neller så försöker du ta bort allt. " +
                "Använd metoden removeArticleFromCart.");
    }

    @Transactional
    public List<ShoppingCartDetail> getShoppingCart(String username) { // <3>
        User existingUser = getUser(username);
        Optional<ShoppingCart> cart = shoppingCartRepository.findByUser(existingUser);
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
    public void removeArticleFromCart(String username, String articleName) { // <6>
        // lägg in kontroll som kollar om artikeln finns i användarens korg
        User existingUser = getUser(username);
        ShoppingCart cart = getShoppingCart(existingUser);
        Article article = getArticle(articleName);
        shoppingCartDetailRepository.deleteByArticle(article);
    }
    @Transactional // <5>
    public List<History> buy(String username) {
        User existingUser = getUser(username);
        ShoppingCart cart = getShoppingCart(existingUser);
        List<History> historyList = new ArrayList<>();

        for (ShoppingCartDetail detail : cart.getCartDetail()) {
            History history = new History();
            history.setUser(existingUser);
            Article article = getArticle(detail.getArticle().getName());
            history.setArticle(article);
            history.setPrice(article.getPrice());
            history.setQuantity(detail.getQuantity());
            historyList.add(historyRepository.save(history));
        }
        shoppingCartDetailRepository.deleteAllByCart(cart);
        cart.getCartDetail().clear();
        shoppingCartRepository.save(cart);

        return historyList;
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


    @Transactional
    public List<History> checkout(String username, List<ShoppingCartDetail> shoppingCart) {
        User user = getUser(username);

        // Skapa historikposter baserat på innehållet i kundvagnen
        List<History> purchaseHistory = new ArrayList<>();

        for (ShoppingCartDetail cartDetail : shoppingCart) {
            Article article = cartDetail.getArticle();
            int quantity = cartDetail.getQuantity();

            // Skapa en historikpost för varje artikel i kundvagnen
            History history = new History();
            history.setUser(user);
            history.setArticle(article);
            history.setPrice(article.getPrice());
            history.setQuantity(quantity);

            // Spara historikposten
            History savedHistory = historyService.addHistory(history);
            purchaseHistory.add(savedHistory);
        }

        // Rensa kundvagnen efter genomfört köp
        removeAllCartItems(username);

        return purchaseHistory;
    }
}
