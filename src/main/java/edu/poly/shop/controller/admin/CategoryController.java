package edu.poly.shop.controller.admin;

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
import edu.poly.shop.model.CategoryDto;
import edu.poly.shop.reppsitory.CategoryRepository;
import edu.poly.shop.service.CategoryService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@Autowired
	CategoryRepository categoryRepository;

	@GetMapping("add")
	public String add(Model model) {
		CategoryDto dto = new CategoryDto();
		dto.setIsEdit(false);
		model.addAttribute("category", dto);
		return "admin/categories/addOrEdit";
	}

	@GetMapping("edit/{categoryId}")
	public ModelAndView edit(ModelMap model, @PathVariable("categoryId") Long categoryId) {
		Optional<Category> opt = categoryService.findById(categoryId);
		CategoryDto dto = new CategoryDto();

		if (opt.isPresent()) {
			Category entity = opt.get();
			BeanUtils.copyProperties(entity, dto);
			dto.setIsEdit(true);
			model.addAttribute("category", dto);
			return new ModelAndView("admin/categories/addOrEdit", model);
		}

		model.addAttribute("message", "Category is not existed!");
		return new ModelAndView("forward:/admin/categories/addOrEdit", model);
	}

	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryDto dto,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ModelAndView("admin/categories/addOrEdit");
		}

		Category entity = new Category();

		BeanUtils.copyProperties(dto, entity);
		categoryService.save(entity);

		model.addAttribute("message", "Category is saved!");
		return new ModelAndView("redirect:/admin/categories", model);
	}

	@GetMapping("delete/{categoryId}")
	public String delete(@PathVariable("categoryId") Long categoryId, ModelMap model) {
		categoryService.deleteById(categoryId);
		model.addAttribute("message", "Category is deleted!");
		return "redirect:/admin/categories/list";
	}

	@RequestMapping("")
	public String list(ModelMap model) {
		List<Category> list = categoryService.findAll();
		model.addAttribute("categories", list);
		return "admin/categories/list";
	}

	@GetMapping("search")
	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
		List<Category> list = null;
		if (StringUtils.hasText(name)) {
			list = categoryService.findByNameContaining(name);
		} else {
			list = categoryService.findAll();
		}
		model.addAttribute("categories", list);
		return "admin/categories/search";
	}

//	@GetMapping("search/paginated")
//	public String searchAndPage(ModelMap model,
//	                            @RequestParam(name = "name", required = false, defaultValue = "") String name,
//	                            @RequestParam(name = "page", defaultValue = "1") int pageNumber,
//	                            @RequestParam(name = "size", defaultValue = "5") int pageSize) {
//
//	    Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by("name"));
//
//	    Page<Category> categories;
//	    if (!name.isEmpty()) {
//	        categories = categoryRepository.findByNameContaining(name, pageable);
//	    } else {
//	        categories = categoryRepository.findAll(pageable);
//	    }
//
//	    int totalPages = categories.getTotalPages();
//	    List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
//
//	    model.addAttribute("categories", categories.getContent());
//	    model.addAttribute("searchName", name);
//	    model.addAttribute("currentPage", pageNumber);
//	    model.addAttribute("totalPages", totalPages);
//	    model.addAttribute("pageNumbers", pageNumbers);
//
//		return "admin/categories/searchpaginated";
//	}

//	@GetMapping("searchpaginated")
//	public String searchAndPage(ModelMap model, @RequestParam(name = "name", required = false) String name,
//			@RequestParam(name = "page") Optional<Integer> page, @RequestParam(name = "size") Optional<Integer> size) {
//
//		int currerntPage = page.orElse(1);
//		int pageSize = page.orElse(5);
//
//		Pageable pageable = PageRequest.of(currerntPage - 1, pageSize, Sort.by("name"));
//
//		Page<Category> rsPage = null;
//		System.out.println("đã tới 147");
//		if (StringUtils.hasText(name)) {
//			rsPage = categoryService.findByNameContaining(name, pageable);
//			model.addAttribute("name", name);
//		} else {
//			rsPage = categoryService.findAll(pageable);
//		}
//
//		int totalPages = rsPage.getTotalPages();
//
//		if (totalPages > 0) {
//			int start = Math.max(1, currerntPage - 2);
//			int end = Math.min(currerntPage + 2, totalPages);
//			if (totalPages > 5) {
//				if (end == totalPages) {
//					start = end - 5;
//				} else if (start == 1) {
//					end = start + 5;
//				}
//			}
//			List<Integer> pageNumbers = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
//
//			model.addAttribute("pageNumbers", pageNumbers);
//		}
//
//		model.addAttribute("categoryPage", rsPage);
//
//		return "admin/categories/searchpaginated";
//	}

	@GetMapping("searchpaginated")
	public String searchAndPage(ModelMap model, 
	                            @RequestParam(name = "name", required = false) String name,
	                            @RequestParam(name = "page", defaultValue = "1") int page,
	                            @RequestParam(name = "size", defaultValue = "5") int size) {

	    int currentPage = page;
	    int pageSize = size;

	    Pageable pageable = PageRequest.of(currentPage - 1, pageSize, Sort.by("name"));

	    Page<Category> rsPage;
	    if (StringUtils.hasText(name)) {
	        rsPage = categoryService.findByNameContaining(name, pageable);
	        model.addAttribute("name", name);
	    } else {
	        rsPage = categoryService.findAll(pageable);
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

	    model.addAttribute("categoryPage", rsPage);

	    return "admin/categories/searchpaginated";
	}

}
