package com.tracker.portfolio.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
public class UsersAuthorities {

    @EmbeddedId
    private UserAuthoritiesId id;

    public UsersAuthorities() {
    }

    public UsersAuthorities(UserAuthoritiesId id) {
        this.id = id;
    }
}
