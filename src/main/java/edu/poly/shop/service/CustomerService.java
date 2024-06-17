package edu.poly.shop.service;

import java.util.List;

import edu.poly.shop.domain.Customer;

public interface CustomerService {

	void deleteCustomerById(int id);

	void saveCustomer(Customer customer);

	List<Customer> getAllCustomers();

	Customer findByName(String name);

	Customer getOne(Integer id);

	boolean exists(Customer customer);

	boolean existsById(Integer id);

	long count();

	boolean sendForgotPasswordEmail(String username, String email);

}
