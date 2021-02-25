package com.tracker.portfolio.repository;

import com.tracker.portfolio.entity.Authority;
import com.tracker.portfolio.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {

    Authority getByUserRole(UserRole role);

}
