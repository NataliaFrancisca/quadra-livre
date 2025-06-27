package br.com.nat.quadralivre.domain.usuario;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity(name = "Usuario")
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String login;
    private String senha;
    private String nome;
    private String telefone;
    @Column(unique = true)
    private String cpf;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Usuario(UsuarioRegistro usuarioRegistro){
        this.login = usuarioRegistro.login();
        this.senha = usuarioRegistro.senha();
        this.nome = usuarioRegistro.nome();
        this.telefone = usuarioRegistro.telefone();
        this.cpf = usuarioRegistro.cpf();
        this.role = usuarioRegistro.role();
    }

    public void atualizar(UsuarioAtualizacao usuarioAtualizacao){
        if (usuarioAtualizacao.nome() != null){
            this.nome = usuarioAtualizacao.nome();
        }

        if (usuarioAtualizacao.senha() != null){
            this.senha = usuarioAtualizacao.senha();
        }

        if (usuarioAtualizacao.telefone() != null){
            this.telefone = usuarioAtualizacao.telefone();
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role.name()));
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public String getPassword() {
        return this.senha;
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
