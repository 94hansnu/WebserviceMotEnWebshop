package WebserviceMotEnWebshop.demo.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;


@Entity
@Data
public class ShoppingCartDetail extends BaseEntity {
    @ManyToOne
    private ShoppingCart cart;
    @ManyToOne
    private Article article;
    private int quantity;
}
