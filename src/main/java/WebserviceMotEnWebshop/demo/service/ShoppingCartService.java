/*package WebserviceMotEnWebshop.demo.service;

import WebserviceMotEnWebshop.demo.entity.dao.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    public List<Article> getShoppingCart(String username) {
        return shoppingCartRepository.findByUsername(username);
    }

    public Article addToCart(String username, Article article) {
        article.setUsername(username);
        return shoppingCartRepository.save(article);
    }

    public Article updateCartItem(String username, Long productId, int quantity) {
        Article existingItem = shoppingCartRepository.findByUsernameAndProductId(username, productId);

        if (existingItem != null) {
            existingItem.setQuantity(quantity);
            return shoppingCartRepository.save(existingItem);
        } else {
            return null;
        }
    }

    public boolean removeFromCart(String username, Long productId) {
        Article existingItem = shoppingCartRepository.findByUsernameAndProductId(username, productId);
        if (existingItem != null) {
            shoppingCartRepository.delete(existingItem);
            return true;
        } else {
            return false;
        }
    }
}*/
