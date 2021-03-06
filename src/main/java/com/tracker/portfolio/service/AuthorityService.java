package com.tracker.portfolio.service;

import com.tracker.portfolio.entity.Authority;
import com.tracker.portfolio.entity.User;
import com.tracker.portfolio.entity.UserAuthoritiesId;
import com.tracker.portfolio.entity.UsersAuthorities;
import com.tracker.portfolio.enums.UserRole;
import com.tracker.portfolio.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.tracker.portfolio.enums.UserRole.ADMIN;
import static com.tracker.portfolio.enums.UserRole.USER;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;
    private final UsersAuthoritiesService usersAuthoritiesService;

    public Authority getAuthority(UserRole role) {
        return authorityRepository.getByUserRole(role);
    }

    public List<Authority> findAll() {
        return authorityRepository.findAll();
    }

    public void createAuthority(UserRole userRole, User user) {
        UserAuthoritiesId userAuthoritiesId = new UserAuthoritiesId(
                user,
                authorityRepository.getByUserRole(userRole)
        );
        UsersAuthorities usersAuthorities = new UsersAuthorities(
                userAuthoritiesId
        );
        usersAuthoritiesService.save(usersAuthorities);
    }

    public void save(Authority authority) {
        authorityRepository.save(authority);
    }

    public void initAuthorities() {
        if (!findAll().isEmpty()) {
            authorityRepository.save(new Authority(USER));
            authorityRepository.save(new Authority(ADMIN));
            log.info("Adding authorities to the system...");
        }

    }
}
