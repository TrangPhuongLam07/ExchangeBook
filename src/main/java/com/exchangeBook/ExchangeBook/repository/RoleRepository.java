package com.exchangeBook.ExchangeBook.repository;

import com.exchangeBook.ExchangeBook.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/*
Create By : ANHTUAN
*/
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
