package co.edu.ucentral.entity;

import jakarta.persistence.*;
import lombok.*;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "correo"),
        @UniqueConstraint(columnNames = "usuario")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 //
    @Column(nullable = false)
    private String nombreCompleto;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false, unique = true)
    private String usuario;

    @Column(nullable = false)
    private String passwordHash;
}