package WebserviceMotEnWebshop.demo.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("cart")
public class ShoppingCartDetail extends BaseEntity {
    @ManyToOne
    private ShoppingCart cart;
    @ManyToOne
    private Article article;
    private int quantity;
}
