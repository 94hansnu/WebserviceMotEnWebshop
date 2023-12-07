package WebserviceMotEnWebshop.demo.database.repository;

import WebserviceMotEnWebshop.demo.database.entity.ShoppingCart;
import WebserviceMotEnWebshop.demo.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<User> findByUser(User user);
}
