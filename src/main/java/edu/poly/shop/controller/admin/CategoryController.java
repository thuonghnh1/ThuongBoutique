package edu.poly.shop.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {
	@GetMapping("add")
	public String add() {
		return "admin/categories/addOrEdit";
	}

	@GetMapping("edit/{categoryId}")
	public String edit() {
		return "admin/categories/addOrEdit";
	}

	@GetMapping("delete/{categoryId}")
	public String delete() {
		return "redirect:/admin/categories/list";
	}

	@GetMapping("saveOrUpdate")
	public String saveOrUpdate() {
		return "redirect:/admin/categories/list";
	}

	@GetMapping("")
	public String list() {
		return "admin/categories/list";
	}

	@GetMapping("search")
	public String search() {
		return "admin/categories/search";
	}
}
