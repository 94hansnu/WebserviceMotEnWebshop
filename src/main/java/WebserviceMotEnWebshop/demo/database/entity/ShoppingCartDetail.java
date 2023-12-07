package WebserviceMotEnWebshop.demo.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDetail extends BaseEntity {
    @ManyToOne
    private ShoppingCart cart;
    @ManyToOne
    private Article article;
    private int quantity;
}
