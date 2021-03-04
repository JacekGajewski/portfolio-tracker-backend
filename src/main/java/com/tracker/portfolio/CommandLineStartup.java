package com.tracker.portfolio;

import com.tracker.portfolio.entity.*;
import com.tracker.portfolio.enums.SectorEnum;
import com.tracker.portfolio.service.AuthorityService;
import com.tracker.portfolio.service.PortfolioService;
import com.tracker.portfolio.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.tracker.portfolio.enums.UserRole.ADMIN;
import static com.tracker.portfolio.enums.UserRole.USER;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandLineStartup implements CommandLineRunner {

    private final PortfolioService portfolioService;
    private final UserService userService;
    private final AuthorityService authorityService;

    @Override
    @Transactional
    public void run(String... args) {
        if (authorityService.findAll().isEmpty()) {
            log.info("Adding authorities to the system...");
            authorityService.save(new Authority(ADMIN));
            authorityService.save(new Authority(USER));
        }
        // TODO resolve to save the data
        if (portfolioService.findAll().isEmpty()) {
            log.info("Sample data is being initialized");
            // createAdminWithPortfolio();
            // createUserWithPortfolio();
        }
    }

    // TODO move to dev data
    private void createAdminWithPortfolio() {
        Portfolio portfolio = createAdminPortfolio();
        User admin = new User("admin", "admin12", null, Collections.singletonList(portfolio));
        Authority adminAuthority = authorityService.getAuthority(ADMIN);
        UserAuthoritiesId adminAuthoritiesId = new UserAuthoritiesId(admin, adminAuthority);
        UsersAuthorities adminAuthorities = new UsersAuthorities(adminAuthoritiesId);
        admin.setAuthorities(Collections.singleton(adminAuthorities));
        userService.save(admin);
    }

    // TODO move to dev data
    private void createUserWithPortfolio() {
        Portfolio portfolio = createUserPortfolio();
        User user = new User("linda", "linda12", null, Collections.singletonList(portfolio));
        Authority userAuthority = authorityService.getAuthority(USER);
        UserAuthoritiesId userAuthoritiesId = new UserAuthoritiesId(user, userAuthority);
        UsersAuthorities usersAuthorities = new UsersAuthorities(userAuthoritiesId);
        user.setAuthorities(Collections.singleton(usersAuthorities));
        userService.save(user);
    }

    private Portfolio createAdminPortfolio() {
        Set<Position> positionSet = getListOfAdminStocks().stream()
                .map(this::createPosition)
                .collect(Collectors.toSet());
        return new Portfolio("Admin portfolio",
                getValueOfPositions(positionSet),
                positionSet,
                null);
    }

    private Portfolio createUserPortfolio() {
        Set<Position> positionSet = getListOfUserStocks().stream()
                .map(this::createPosition)
                .collect(Collectors.toSet());
        return new Portfolio("Linda portfolio",
                getValueOfPositions(positionSet),
                positionSet,
                null);
    }

    private BigDecimal getValueOfPositions(Set<Position> positionList) {
        return positionList.stream()
                .map(Position::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Position createPosition(Stock stock) {
        int amount = getRandomAmount();
        BigDecimal value = getPositionValue(stock, amount);
        return new Position(stock, amount, SectorEnum.STOCK, value, null);
    }

    private BigDecimal getPositionValue(Stock stock, int amount) {
        BigDecimal value = stock.getValue();
        return value.multiply(BigDecimal.valueOf(amount));
    }

    private List<Stock> getListOfAdminStocks() {
        return Arrays.asList(
                new Stock("PGE", new BigDecimal("6.75"), "WSE", getLocalDate()),
                new Stock("TAU", new BigDecimal("6.75"), "WSE", getLocalDate()),
                new Stock("APPL", new BigDecimal("123.54"), "NASDAQ", getLocalDate())
        );
    }

    private List<Stock> getListOfUserStocks() {
        return Arrays.asList(
                new Stock("PZU", new BigDecimal("6.75"), "WSE", getLocalDate()),
                new Stock("PKO", new BigDecimal("6.75"), "WSE", getLocalDate()),
                new Stock("AMZN", new BigDecimal("3893.65"), "NASDAQ", getLocalDate()),
                new Stock("TSLA", new BigDecimal("750.76"), "NASDAQ", getLocalDate())
        );
    }

    private static int getRandomAmount() {
        return  new Random().nextInt(100) + 1;
    }

    private LocalDate getLocalDate() {
        return LocalDate.of(2021, 1, 1);
    }
}
