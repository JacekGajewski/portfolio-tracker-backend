package com.tracker.portfolio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
public class User extends BaseEntity{

    private String username;

    private String  password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "id.user", cascade = CascadeType.ALL)
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<UsersAuthorities> authorities;

    @OneToMany(mappedBy = "portfolioOwner", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Portfolio> portfolioList;


    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(int id, String username, String password) {
        this.username = username;
        this.password = password;
    }
}
