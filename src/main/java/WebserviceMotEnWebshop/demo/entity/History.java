package WebserviceMotEnWebshop.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class History extends BaseEntity {
    @ManyToOne
    private Article article;
    @ManyToOne
    private User user;
    private Double price;
    private Integer quantity;

}
