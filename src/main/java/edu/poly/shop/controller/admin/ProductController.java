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
import edu.poly.shop.domain.Product;
import edu.poly.shop.model.CategoryDto;
import edu.poly.shop.model.ProductDto;
import edu.poly.shop.service.CategoryService;
import edu.poly.shop.service.ProductService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("admin/products")
public class ProductController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	ProductService productService;

	@ModelAttribute("categories")
	public List<Category> getCategory() {
		return categoryService.findAll();
	}

	@GetMapping("add")
	public String add(Model model) {
		ProductDto dto = new ProductDto();

		dto.setIsEdit(false);
		model.addAttribute("product", dto);
		return "admin/products/addOrEdit";
	}

	@GetMapping("edit/{productId}")
	public ModelAndView edit(ModelMap model, @PathVariable("productId") Long productId) {
		Optional<Product> opt = productService.findById(productId);
		ProductDto dto = new ProductDto();

		if (opt.isPresent()) {
			Product entity = opt.get();
			BeanUtils.copyProperties(entity, dto);
	        dto.setCategoryId(entity.getCategory().getCategoryId());
			dto.setIsEdit(true);
			model.addAttribute("product", dto);
			return new ModelAndView("admin/products/addOrEdit", model);
		}

		model.addAttribute("message", "Product is not existed!");
		return new ModelAndView("forward:/admin/products/addOrEdit", model);
	}

	/*
	 * @PostMapping("saveOrUpdate") public ModelAndView saveOrUpdate(ModelMap
	 * model, @Valid @ModelAttribute("product") ProductDto dto, BindingResult
	 * result) { if (result.hasErrors()) { System.out.println("82"+result); return
	 * new ModelAndView("admin/products/addOrEdit"); }
	 * 
	 * Product entity = new Product();
	 * 
	 * BeanUtils.copyProperties(dto, entity);
	 * 
	 * // lưu category id Optional<Category> optCategory =
	 * categoryService.findById(dto.getCategoryId()); if (optCategory.isPresent()) {
	 * entity.setCategory(optCategory.get()); } else {
	 * result.rejectValue("categoryId", "error.product", "Invalid Category"); return
	 * new ModelAndView("admin/products/addOrEdit"); }
	 * 
	 * productService.save(entity);
	 * 
	 * model.addAttribute("message", "Product is saved!"); return new
	 * ModelAndView("redirect:/admin/products", model); }
	 */

	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdateProduct(@ModelAttribute("product") ProductDto productDto, ModelMap model,@RequestParam("categoryId") Long categoryId) {
	    Product product = convertToEntity(productDto);
	    
	    product.setImage(productDto.getImage());   
	    // Tìm đối tượng Category dựa trên categoryId
	    Optional<Category> categoryOptional = categoryService.findById(categoryId);
	    if (categoryOptional.isPresent()) {
	        Category category = categoryOptional.get();
	        product.setCategory(category);

	        product.setEntereDate(productDto.getEntereDate());
	        
	        productService.save(product);

	        model.addAttribute("message", "Product is saved!");

	        return new ModelAndView("redirect:/admin/products", model);
	    } else {
	        model.addAttribute("message", "Không tìm thấy danh mục với ID cung cấp.");
	        return new ModelAndView("redirect:/admin/products", model);
	    }
	}
	private Product convertToEntity(ProductDto productDto) {
	    Product product = new Product();
	    BeanUtils.copyProperties(productDto, product);  
	    return product;
	}
	
	@GetMapping("delete/{productId}")
	public String delete(@PathVariable("productId") Long productId, ModelMap model) {
		productService.deleteById(productId);
		model.addAttribute("message", "Product is deleted!");
		return "redirect:/admin/products/list";
	}

	@RequestMapping("")
	public String list(ModelMap model) {
		List<Product> list = productService.findAll();
		model.addAttribute("products", list);
		return "admin/products/list";
	}
//	@RequestMapping("")
//	public String list(@RequestParam(defaultValue = "0") int page,
//	                   @RequestParam(defaultValue = "10") int size,
//	                   ModelMap model) {
//	    Pageable pageable = PageRequest.of(page, size);
//	    Page<Product> productPage = productService.findAll(pageable);
//	    model.addAttribute("productPage", productPage);
//	    return "admin/products/list";
//	}

	@GetMapping("search")
	public String search(ModelMap model,
	        @RequestParam(name = "name", required = false) String name,
	        @RequestParam(name = "price", required = false) Double price) {

	    List<Product> list = null;

	    	if (StringUtils.hasText(name)) {
	        list = productService.findByNameContaining(name);
	    } else if (price != null) {
	        list = productService.findByPrice(price);
	    } else {
	        list = productService.findAll();
	    }

	    model.addAttribute("products", list);
	    return "admin/products/search";
	}
	
	@GetMapping("searchpaginated")
	public String searchAndPage(ModelMap model, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "5") int size) {

		int currentPage = page;
		int pageSize = size;

		Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));

		Page<Product> rsPage;
		if (StringUtils.hasText(name)) {
			rsPage = productService.findByNameContaining(name, pageable);
			model.addAttribute("name", name);
		} else {
			rsPage = productService.findAll(pageable);
		}

		int totalPages = rsPage.getTotalPages();

		if (totalPages > 0) {
			int start = Math.max(1, currentPage - 2);
			int end = Math.min(currentPage + 2, totalPages);
			if (totalPages > 5) {
				if (end == totalPages) {
					start = end - 4;
				} else if (start == 1) {
					end = start + 4;
				}
			}
			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
			model.addAttribute("pageNumbers", pageNumbers);
		}

		model.addAttribute("productPage", rsPage);

		return "admin/products/searchpaginated";
	}
	
	@GetMapping("detail/{productId}")
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
	    return new ModelAndView("forward:/admin/products", model);
	}

}
