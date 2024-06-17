package edu.poly.shop.service.impl;

import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.poly.shop.domain.Customer;
import edu.poly.shop.reppsitory.CustomerRepository;
import edu.poly.shop.service.CustomerService;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String emailSender;

	@Override
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	@Override
	public Customer findByName(String name) {
		return customerRepository.findByName(name);
	}

	@Override
	public void saveCustomer(Customer customer) {
		customerRepository.save(customer);
	}

	@Override
	public void deleteCustomerById(int id) {
		customerRepository.deleteById(id);
	}

	@Override
	public long count() {
		return customerRepository.count();
	}

	@Override
	public boolean existsById(Integer id) {
		return customerRepository.existsById(id);
	}

	@Override
	public boolean exists(Customer customer) {
		return customerRepository.existsById(customer.getCustomerId());
	}

	@Override
	public Customer getOne(Integer id) {
		return customerRepository.getOne(id);
	}
	
	public boolean sendForgotPasswordEmail(String username, String email) {
        Customer customer = customerRepository.findByName(username);

        if (customer != null && customer.getEmail().equals(email)) {
            try {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom(emailSender);
                mailMessage.setTo(email);
                mailMessage.setSubject("LẤY LẠI MẬT KHẨU THUONG'S BOUTIQUE");
                mailMessage.setText("Mật khẩu của bạn là: " + customer.getPassword());

                // Send email
                javaMailSender.send(mailMessage);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
