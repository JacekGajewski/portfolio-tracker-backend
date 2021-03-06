package com.tracker.portfolio.startup;

import com.tracker.portfolio.service.AuthorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!dev")
public class CommandLineStartupProd implements CommandLineRunner {

    private final AuthorityService authorityService;

    @Override
    @Transactional
    public void run(String... args) {
        authorityService.initAuthorities();
    }
}
