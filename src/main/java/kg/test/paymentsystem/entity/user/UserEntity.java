package kg.test.paymentsystem.entity.user;

import jakarta.persistence.*;
import kg.test.paymentsystem.entity.card.ElcardEntity;
import kg.test.paymentsystem.entity.card.MasterCardEntity;
import kg.test.paymentsystem.entity.card.VisaEntity;
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
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<VisaEntity> visas;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<MasterCardEntity> masterCards;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<ElcardEntity> elcards;

    @Enumerated(EnumType.STRING)
    private RoleEntity role;
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
