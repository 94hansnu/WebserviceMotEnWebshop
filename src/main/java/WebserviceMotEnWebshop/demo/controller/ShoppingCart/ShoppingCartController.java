package WebserviceMotEnWebshop.demo.controller.ShoppingCart;

import WebserviceMotEnWebshop.demo.service.ShoppingCartService;
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
    private ShoppingCartService shoppingCartService;

    //GET-förfrågan- Hämta kundkorgen för inloggad användare
    @GetMapping
    public ResponseEntity<List<Article>> getShoppingCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<Article> shoppingCart = shoppingCartService.getShoppingCart(username);
        return ResponseEntity.ok(shoppingCart);
    }

    //POST-förfrågan- Lägg till produkt i kundkorgen
    @PostMapping
    public ResponseEntity<Article> addToCart(@RequestBody Article article) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Article addedItem = shoppingCartService.addToCart(username, article);
    }

    //PUT-förfrågan- Uppdatera antalet för produkt i kundkorgen
    @PutMapping("/{productId}")
    public ResponseEntity<Article> updateCartItem(@PathVariable Long productId, @RequestParam  int quantity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Article updatedItem = shoppingCartService.updateCartItem(username, productId, quantity);
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

        boolean  removed = shoppingCartService.removeFromCart(username, productId);
        if (removed) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
