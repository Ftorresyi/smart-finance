package Finance.organizador_financeiro.domain;

import jakarta.persistence.*;
import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users") // Renaming the table to avoid conflict with the reserved word "user" in some databases
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions; // Mapping to user's transactions

    // Methods from UserDetails interface

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // For now, we are not using roles (user profiles), so we return an empty list.
        // In an evolution, we could have roles like "ROLE_USER", "ROLE_ADMIN".
        return null;
    }

    @Override
    public String getUsername() {
        // The "username" for Spring Security will be our user's email.
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Account never expires
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Account is never locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credentials never expire
    }

    @Override
    public boolean isEnabled() {
        return true; // User is always enabled
    }
}