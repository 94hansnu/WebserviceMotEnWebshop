package WebserviceMotEnWebshop.demo.database.repository;

import WebserviceMotEnWebshop.demo.database.entity.ShoppingCart;
import WebserviceMotEnWebshop.demo.database.entity.ShoppingCartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface ShoppingCartDetailRepository extends JpaRepository<ShoppingCartDetail, Long> {

    List<ShoppingCartDetail> findByCart(ShoppingCart cart);

    // Radera alla rader i tbl som tillhör en kundkorg (använd vid köp)
    @Transactional
    @Modifying
    @Query("DELETE FROM ShoppingCartDetail sd WHERE sh.cart = :shoppingChart")
    void deleteByShoppingCart(@Param("shoppingCart") ShoppingCart shoppingCart);
}
