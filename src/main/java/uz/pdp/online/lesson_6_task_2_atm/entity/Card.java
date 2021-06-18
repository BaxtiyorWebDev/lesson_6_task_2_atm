package uz.pdp.online.lesson_6_task_2_atm.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import uz.pdp.online.lesson_6_task_2_atm.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Card extends AbsEntity implements UserDetails {


    @Column(nullable = false, unique = true)
    private Long number; // 16 digit

    @Column(nullable = false)
    private short pinCode; // 4 digit

    @Column(nullable = false)
    private Integer cvv; // 3 digit

    @Column(nullable = false)
    private String holderName;

    @Column(nullable = false)
    private Date expireDate;


    @ManyToOne
    private Bank bank;

    @ManyToOne
    private CardType cardType;

    private boolean active = true;

    @ManyToOne
    private User user;

    private Double balance;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = true;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return String.valueOf(this.pinCode);
    }

    @Override
    public String getUsername() {
        return String.valueOf(this.number);
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}

