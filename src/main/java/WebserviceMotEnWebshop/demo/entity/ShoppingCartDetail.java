package WebserviceMotEnWebshop.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
