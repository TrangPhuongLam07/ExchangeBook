package com.exchangeBook.ExchangeBook.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.exchangeBook.ExchangeBook.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

}
