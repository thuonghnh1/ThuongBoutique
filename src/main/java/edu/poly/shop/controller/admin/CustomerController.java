package edu.poly.shop.controller.admin;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import edu.poly.shop.domain.Category;
import edu.poly.shop.domain.Customer;
import edu.poly.shop.domain.Product;
import edu.poly.shop.model.CategoryDto;
import edu.poly.shop.model.ProductDto;
import edu.poly.shop.service.CategoryService;
import edu.poly.shop.service.CustomerService;
import edu.poly.shop.service.ProductService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("customer")
public class CustomerController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductService productService;

	@Autowired
    private CustomerService customerService;
	
	@ModelAttribute("categories")
	public List<Category> getCategory() {
		return categoryService.findAll();
	}
	
	@GetMapping("/products")
    public String showHomePage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "customer/home"; // Tên của view template (home.html)
    }
	
	@GetMapping("/detail/{productId}")
	public ModelAndView viewDetail(ModelMap model, @PathVariable("productId") Long productId) {
	    Optional<Product> opt = productService.findById(productId);

	    if (opt.isPresent()) {
	    	 Product product = opt.get();
	         System.out.println("Product: " + product);
	         System.out.println("Category: " + product.getCategory().getName());
	         
	         model.addAttribute("product", product);
	        return new ModelAndView("admin/products/detail", model);
	    }

	    model.addAttribute("message", "Product not found!");
	    return new ModelAndView("forward:/customer/products", model);
	}
	
	@GetMapping("/profile/edit")
	public String showEditProfileForm(@RequestParam("customerId") Integer customerId, Model model) {
	    model.addAttribute("customerId", customerId);
	    Customer customer = customerService.getOne(customerId);
	    model.addAttribute("customer", customer);
	    return "customer/edit-profile";
	}
	
	 @PostMapping("/profile/edit")
	    public String processEditProfileForm(@Valid @ModelAttribute("customer") Customer customer, BindingResult result, Model model) {
	        try {
	            customerService.saveCustomer(customer);
	            model.addAttribute("error", "Profile updated successfully.");
	            return "redirect:/customer/profile/edit?customerId=" + customer.getCustomerId();
	        } catch (Exception e) {
	            model.addAttribute("error", "Failed to update profile. Please try again.");
	            e.printStackTrace();
	            return "customer/edit-profile";
	        }
	    }
}
