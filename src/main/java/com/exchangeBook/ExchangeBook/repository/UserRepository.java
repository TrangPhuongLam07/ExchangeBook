package com.exchangeBook.ExchangeBook.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exchangeBook.ExchangeBook.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable paging);
    
    Optional<User> findByEmail(String email);
    
    Boolean existsByEmail(String email);
    
    Boolean existsByPassword(String password);
    
    //
//    Optional<User> findById(Long id);
//    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
//    List<User> findAllByRoleName(@Param("roleName") String roleName);

}
