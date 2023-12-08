package WebserviceMotEnWebshop.demo.controller.ShoppingCart;

import WebserviceMotEnWebshop.demo.database.entity.Article;
import WebserviceMotEnWebshop.demo.database.entity.ShoppingCartDetail;
import WebserviceMotEnWebshop.demo.database.entity.User;
import WebserviceMotEnWebshop.demo.database.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Shoppingcart")
public class ShoppingCartController {

    @Autowired
    private ShopService shopService;

    //GET-förfrågan- Hämta kundkorgen för inloggad användare
    @GetMapping
    public ResponseEntity<List<ShoppingCartDetail>> getShoppingCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<ShoppingCartDetail> shoppingCart = shopService.getShoppingCart(username);
        return ResponseEntity.ok(shoppingCart);
    }


    //POST-förfrågan- Lägg till produkt i kundkorgen
    @PostMapping
    public ResponseEntity<ShoppingCartDetail> addToCart(@RequestBody ShoppingCartDetail shoppingCartDetail) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        ShoppingCartDetail addedItem = shopService.addItem(username, shoppingCartDetail.getArticle(), shoppingCartDetail.getQuantity());
        return ResponseEntity.ok(addedItem);
    }

    //PUT-förfrågan- Uppdatera antalet för produkt i kundkorgen
    @PutMapping("/{productId}")
    public ResponseEntity<ShoppingCartDetail> updateCartItem(@PathVariable Long productId, @RequestParam int quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = new User();
        user.setId(username);

        Article article = new Article();
        article.setId(productId);

        ShoppingCartDetail updatedItem = shopService.addItem(user, article, quantity);
        if (updatedItem != null) {
            return ResponseEntity.ok(updatedItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //DELETE-förfrågan- Ta bort produkt från kundkorgen
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long productId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        boolean removed = shopService.removeFromCart(username, productId);
        if (removed) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
