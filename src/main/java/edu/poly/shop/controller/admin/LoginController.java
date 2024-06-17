package edu.poly.shop.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.poly.shop.domain.Customer;
import edu.poly.shop.reppsitory.CustomerRepository;
import edu.poly.shop.service.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	@Autowired
	CustomerService customerService;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	HttpSession session;

	@GetMapping("/account/login")
	public String login(Model model, @RequestParam(value = "error", required = false) String error) {
		if (error != null) {
			model.addAttribute("message", "Invalid username or password!");
		}
		return "account/login";
	}

	@PostMapping("/account/login")
	public String login(Model model, HttpSession session,
	                    @RequestParam("name") String name,
	                    @RequestParam("password") String password) {
	    try {
	        Customer customer = customerRepository.findByName(name);
	        if (customer == null || !customer.getPassword().equals(password)) {
	            model.addAttribute("message", "Invalid username or password");
	            return "account/login";
	        } else {
	            session.setAttribute("customer", customer);
	            model.addAttribute("customer", customer);
	            model.addAttribute("isAdmin", customer.isAdmin());
	            System.out.println(customer.isAdmin()+" 49 test");
	            if (customer.isAdmin()) {
	                return "redirect:/admin/products";
	            } else {
	                return "redirect:/customer/products";
	            }
	        }
	    } catch (Exception e) {
	        model.addAttribute("message", "Invalid username: " + e.getMessage());
	        e.printStackTrace();
	        return "account/login";
	    }
	}

	@GetMapping("/account/logout")
	public String logout(HttpSession session) {
	    session.removeAttribute("customer");
	    session.invalidate(); // Xóa hết các session attributes
	    return "redirect:/account/login";
	}
	
	@GetMapping("/account/register")
	public String register(Model model) {
		return "account/register";
	}

	@PostMapping("/account/register")
	public String register(Model model, @RequestParam("name") String name, @RequestParam("fullname") String fullname,
	        @RequestParam("email") String email, @RequestParam("password") String password,
	        @RequestParam("phone") String phone) {
	    try {
	        // Kiểm tra name đã tồn tại trong hệ thống hay chưa
	    	if (customerService.findByName(name) != null) {
                model.addAttribute("message", "Username is already taken. Please choose another one.");
                return "account/register";
            }
	        Customer customer = new Customer();
	        customer.setName(name);
	        customer.setFullname(fullname);
	        customer.setEmail(email);
	        customer.setPassword(password);
	        customer.setPhone(phone);
	        customer.setAdmin(false);
	        customerService.saveCustomer(customer);
	        model.addAttribute("message", "Registration successful");
	        return "account/login";
	    }
	        catch (Exception e) {
	            model.addAttribute("message", "Registration failed: " + e.getMessage());
	            e.printStackTrace();
	            return "account/register";
	        }
	}
	
	@GetMapping("/forgotpassword")
    public String showForgotPasswordForm() {
        return "account/forgotpassword";
    }

    @PostMapping("/FindPassword")
    public ModelAndView processForgotPassword(HttpServletRequest request,
                                              @RequestParam("name") String username,
                                              @RequestParam("email") String email) {

        boolean emailSent = customerService.sendForgotPasswordEmail(username, email);

        ModelAndView mav = new ModelAndView("account/forgotpassword");
        if (emailSent) {
            mav.addObject("success", "Vui lòng check mail. Chúng tôi đã gửi password đến mail của bạn!");
        } else {
            mav.addObject("message", "Username và email không đúng với đăng ký!!");
        }
        return mav;
    }

}
