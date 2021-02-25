package com.tracker.portfolio.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class UserAuthoritiesId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "id_authority")
    private Authority authority;

    public UserAuthoritiesId() {
    }

    public UserAuthoritiesId(User user, Authority authority) {
        this.user = user;
        this.authority = authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAuthoritiesId that =(UserAuthoritiesId) o;

        if (user.getId() != ((that.user.getId()))) return false;
        return authority.getId() == (((UserAuthoritiesId) o).getAuthority().getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthority(), getUser());
    }
}
