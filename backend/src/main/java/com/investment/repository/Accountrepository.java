package com.investment.repository;

import com.investment.domain.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface Accountrepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUserId(Long userId);
}
