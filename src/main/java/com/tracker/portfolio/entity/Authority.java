package com.tracker.portfolio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tracker.portfolio.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Authority extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "id.authority")
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Set<UsersAuthorities> usersAuthorities;

    public Authority() {
    }

    public Authority(UserRole userRole) {
        this.userRole = userRole;
    }

    public Authority(UserRole userRole, Set<UsersAuthorities> usersAuthorities) {
        this.userRole = userRole;
        this.usersAuthorities = usersAuthorities;
    }
}


