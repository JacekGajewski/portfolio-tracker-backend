package com.tracker.portfolio.repository;


import com.tracker.portfolio.entity.UserAuthoritiesId;
import com.tracker.portfolio.entity.UsersAuthorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersAuthoritiesRepository extends JpaRepository<UsersAuthorities, UserAuthoritiesId> {

}
