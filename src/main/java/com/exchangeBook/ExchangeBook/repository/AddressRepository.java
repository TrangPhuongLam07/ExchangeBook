package com.exchangeBook.ExchangeBook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.exchangeBook.ExchangeBook.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{

}
