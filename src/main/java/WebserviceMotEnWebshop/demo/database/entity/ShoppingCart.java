package WebserviceMotEnWebshop.demo.database.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ShoppingCart extends BaseEntity {
    @ManyToOne
    private User user;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cart")
    private List<ShoppingCartDetail> cartDetail = new ArrayList<>();


}
