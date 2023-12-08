package WebserviceMotEnWebshop.demo.database.service;

import WebserviceMotEnWebshop.demo.database.entity.Article;
import WebserviceMotEnWebshop.demo.database.entity.ShoppingCart;
import WebserviceMotEnWebshop.demo.database.entity.ShoppingCartDetail;
import WebserviceMotEnWebshop.demo.database.entity.User;
import WebserviceMotEnWebshop.demo.database.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    /* Det skall finnas följande funktioner för shop:

    *   FÅ ALLA / SPECIFIK ARTIKEL -> STARTSIDAN, VAD SOM FINNS TILLGÄNGLIGT

    *  (C) LÄGGA TILL ARTIKEL I KUNDKORG
    *  (R) SE ALLA ARTIKEL, DESS ANTAL OCH PRIS I KUNDKORG
    *  (U) UPPDATERA ANTAL ARTIKEL I KUNDKORG
    *  (D) TA BORT ARTIKEL I KUNDKORG


    * TA BORT HELA KUNDKORG INNEHÅLL
    * KÖP ALLT I HELA KUNDKORG (-> SPARA TILL HISTORY -> RADERA RADER I KUNDDETALJKORG)
     */



    public List<Article> getArticles(String filter) {
        if (filter == null || filter.isEmpty()) {
            return articleRepository.findAll();
        }
        else return articleRepository.search(filter);
    }



    // Borde fungera på att lägga till ny, radera och uppdatera kundkorg. Mer testning krävs
    // Om finns tid, gör egna Exeptions
    @Transactional
    public ShoppingCartDetail addItem(User user, Article article, int quantity) {

        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isEmpty()) throw new UsernameNotFoundException("Användaren hittas inte.");

        User existingUser = optionalUser.get();

        Optional<ShoppingCart> existingCart = shoppingCartRepository.findByUser(existingUser);
        ShoppingCart cart;
        if (existingCart.isPresent()) {
            cart = existingCart.get();
        } else {
            ShoppingCart newCart = new ShoppingCart();
            newCart.setUser(existingUser);
            cart = shoppingCartRepository.save(newCart);
        }

        Optional<Article> existingArticle = articleRepository.findById(article.getId());
        if (existingArticle.isEmpty()) {
            throw new RuntimeException("Artikel finns inte.");
        }
        Article fetchedArticle = existingArticle.get();

        // Kollar om det finns ett matchande objekt artikel i befintlig kundkorg
        Optional<ShoppingCartDetail> existingItem = cart.getCartDetail().stream()
                .filter(cartDetail -> cartDetail.getArticle().equals(fetchedArticle)).findFirst();

        if (existingItem.isPresent()) {
            ShoppingCartDetail item = existingItem.get();
            // Kontroll om användare försöker att ta bort alla av den artikel
            if (quantity + item.getQuantity() <= 0) {
                shoppingCartDetailRepository.delete(item);
            }
            item.setQuantity(item.getQuantity() + quantity);
            return shoppingCartDetailRepository.save(item);
        } else {
            ShoppingCartDetail newItem = new ShoppingCartDetail(cart, article, quantity);
            return shoppingCartDetailRepository.save(newItem);
        }
    }

}
