package WebserviceMotEnWebshop.demo.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article extends BaseEntity {
    private String name;
    private String description;
    private Double price;

}
