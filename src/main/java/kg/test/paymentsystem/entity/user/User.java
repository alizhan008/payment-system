package kg.test.paymentsystem.entity.user;

import jakarta.persistence.*;
import kg.test.paymentsystem.entity.card.Card;
import kg.test.paymentsystem.entity.card.Elcard;
import kg.test.paymentsystem.entity.card.MasterCard;
import kg.test.paymentsystem.entity.card.Visa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Visa> visas;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<MasterCard> masterCards;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Elcard> elcards;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
       return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
