package com.example.demo.Model;

import com.example.demo.Utilities.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User extends BaseModel implements UserDetails {

    @JsonView(Views.Public.class)
    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @JsonView(Views.Public.class)
    @Column(name = "nickname")
    private String nickname;

    @JsonView(Views.Detail.class)
    @Column(name = "phone")
    private String phone;

    @JsonView(Views.Detail.class)
    @Column(name = "email")
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @JsonView(Views.Public.class)
    @Column(name = "cart_Id")
    private UUID cartId;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
//    @JsonIgnore
    @JoinColumn(name = "cart_id",referencedColumnName = "id", insertable = false, updatable = false)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonIgnoreProperties("user")
    @EqualsAndHashCode.Exclude
    private Set<Order> orders;

    @JsonView(Views.Public.class)
    @Enumerated(EnumType.STRING)
    private Role role;

    private String verificationToken;

    private boolean emailVerified;

    @JsonView(Views.Detail.class)
    private String linkImage;

    @JsonView({Views.Public.class})
    private BigDecimal coin = BigDecimal.ZERO;

    public void createCartAndOrder() {
        cart = new Cart();
        cartId = cart.getId();
        System.out.println(cart);
        orders = new HashSet<>();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
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
    public boolean isEnabled() { return true; }

    public void addCoin(BigDecimal newCoin) {
        this.coin = this.coin.add(newCoin);
        System.out.println("---------------------------------- coin: " + coin);
        System.out.println("---------------------------------- newcoin: " + newCoin);

    }
    public User(String username, String nickname, String phone, String email, String password, Role role, boolean emailVerified) {
        this.username = username;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.role = role;
        this.emailVerified = emailVerified;
    }

    @Override
    public String toString() {
        return "User{" +
                "username=" + username +
                "coin=" + coin +
                '}';
    }
}
