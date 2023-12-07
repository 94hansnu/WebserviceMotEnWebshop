package WebserviceMotEnWebshop.demo.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.lang.NonNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private Double price;

}
