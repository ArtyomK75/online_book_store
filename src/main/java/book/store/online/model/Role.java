package book.store.online.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Table(name = "roles")
@SQLDelete(sql = "UPDATE roles SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private RoleName name;
    @Column(nullable = false)
    private boolean isDeleted = false;

    public Role() {
    }

    public Role(RoleName name) {
        this.name = name;
    }

    public enum RoleName {
        ROLE_USER,
        ROLE_ADMIN
    }
}
