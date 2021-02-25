package com.tracker.portfolio.service;

import com.tracker.portfolio.entity.UsersAuthorities;
import com.tracker.portfolio.repository.UsersAuthoritiesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersAuthoritiesService {

    private final UsersAuthoritiesRepository usersAuthoritiesRepository;

    public UsersAuthorities save(UsersAuthorities usersAuthorities) {
        return usersAuthoritiesRepository.save(usersAuthorities);
    }
}
