package edu.poly.shop.reppsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.poly.shop.domain.Customer;

@Repository
public interface CustomerRepository extends  JpaRepository<Customer, Integer>{
	Customer findByName(String name);
}
