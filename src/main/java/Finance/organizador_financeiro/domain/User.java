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
@Table(name = "users") // Renomeando a tabela para evitar conflito com a palavra reservada "user" em alguns bancos de dados
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lancamento> lancamentos; // Corrigindo o nome da propriedade para refletir a entidade Lancamento

    // Métodos da interface UserDetails

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Por enquanto, não estamos usando roles (perfis de usuário), então retornamos uma lista vazia.
        // Em uma evolução, poderíamos ter roles como "ROLE_USER", "ROLE_ADMIN".
        return null;
    }

    @Override
    public String getUsername() {
        // O "username" para o Spring Security será o e-mail do nosso usuário.
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // A conta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // A conta nunca é bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // As credenciais nunca expiram
    }

    @Override
    public boolean isEnabled() {
        return true; // O usuário está sempre habilitado
    }
}