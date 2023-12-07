package WebserviceMotEnWebshop.demo.database.entity;

import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        } return super.hashCode();
    }
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof BaseEntity)) {
            return false;
        }
        BaseEntity other = (BaseEntity) object;
        if (getId() != null) {
            return getId().equals(other.getId());
        }
        return super.equals(other);
    }

}
