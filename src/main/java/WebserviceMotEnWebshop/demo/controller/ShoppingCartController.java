package WebserviceMotEnWebshop.demo.controller;

import WebserviceMotEnWebshop.demo.database.entity.History;
import WebserviceMotEnWebshop.demo.database.entity.ShoppingCartDetail;
import WebserviceMotEnWebshop.demo.service.ShopService;
import WebserviceMotEnWebshop.demo.modell.dto.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shoppingcart")
public class ShoppingCartController {

    @Autowired
    private ShopService shopService;

    //GET-förfrågan- Hämta kundkorgen för inloggad användare
    @GetMapping("/cart")
    public ResponseEntity<List<ShoppingCartDetail>> getShoppingCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<ShoppingCartDetail> shoppingCart = shopService.getShoppingCart(username);
        return ResponseEntity.ok(shoppingCart);
    }


    //POST-förfrågan- Lägg till produkt i kundkorgen
    @PostMapping
    public ResponseEntity<ShoppingCartDetail> addToCart(@RequestBody CartItem item) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            ShoppingCartDetail addedItem = shopService.addOrUpdateArticleInCart(username, item.name(), item.quantity());
            return ResponseEntity.ok(addedItem);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    @PostMapping("/buy")
    public ResponseEntity<List<History>> buy(Authentication authentication) {
        List<History> historyList = shopService.buy(authentication.getName());
        if (historyList.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(shopService.buy(authentication.getName()));
    }
    @DeleteMapping("/") // Tar bort EN rad i shoppingCartDetails av viss produkt baserat på produktnamn
    public ResponseEntity deleteOneRowOfArticles(@RequestParam String articleName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        shopService.removeArticleFromCart(username, articleName);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping
    public ResponseEntity deleteWholeCart(Authentication authentication) {
        shopService.removeAllCartItems(authentication.getName());
        return ResponseEntity.ok().build();
    }

    //PUT-förfrågan- Uppdatera antalet för produkt i kundkorgen
    @PutMapping
    public ResponseEntity<ShoppingCartDetail> updateCartItem(@RequestBody CartItem item) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        ShoppingCartDetail updatedItem = shopService.addOrUpdateArticleInCart(username, item.name(), item.quantity());
        if (updatedItem != null) {
            return ResponseEntity.ok(updatedItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //POST-förfrågan- Slutför köp och lägg till i historik
    @PostMapping("/checkout")
    public ResponseEntity<List<History>> checkout(Authentication authentication) {
        String username = authentication.getName();

        // Hämta innehållet i kundvagnen
        List<ShoppingCartDetail> shoppingCart = shopService.getShoppingCart(username);

        // Skapa historikposter baserat på kundvagnen
        List<History> purchaseHistory = shopService.checkout(username, shoppingCart);

        // Rensa kundvagnen efter genomfört köp
        shopService.removeAllCartItems(username);

        return ResponseEntity.ok(purchaseHistory);
    }

    //GET-förfrågan - Beräkna totalbelopp för kundkorg
    @GetMapping("/totalCartAmount")
    public ResponseEntity<Double> totalCartAmount(@RequestParam String username) {
        double totalAmount = shopService.totalCartAmount(username);
        return ResponseEntity.ok(totalAmount);
    }
}
