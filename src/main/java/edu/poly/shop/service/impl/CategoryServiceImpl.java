package edu.poly.shop.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import edu.poly.shop.domain.Category;
import edu.poly.shop.reppsitory.CategoryReppsitory;
import edu.poly.shop.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	CategoryReppsitory categoryReppsitory;

	public CategoryServiceImpl(CategoryReppsitory categoryReppsitory) {
		super();
		this.categoryReppsitory = categoryReppsitory;
	}

	@Override
	public <S extends Category> S save(S entity) {
		return categoryReppsitory.save(entity);
	}

	@Override
	public <S extends Category> List<S> saveAll(Iterable<S> entities) {
		return categoryReppsitory.saveAll(entities);
	}

	@Override
	public List<Category> findAll(Sort sort) {
		return categoryReppsitory.findAll(sort);
	}

	@Override
	public void flush() {
		categoryReppsitory.flush();
	}

	@Override
	public <S extends Category> S saveAndFlush(S entity) {
		return categoryReppsitory.saveAndFlush(entity);
	}

	@Override
	public <S extends Category> List<S> saveAllAndFlush(Iterable<S> entities) {
		return categoryReppsitory.saveAllAndFlush(entities);
	}

	@Override
	public List<Category> findAll() {
		return categoryReppsitory.findAll();
	}

	@Override
	public List<Category> findAllById(Iterable<Long> ids) {
		return categoryReppsitory.findAllById(ids);
	}

	@Override
	public Optional<Category> findById(Long id) {
		return categoryReppsitory.findById(id);
	}

	@Override
	public void deleteAllInBatch(Iterable<Category> entities) {
		categoryReppsitory.deleteAllInBatch(entities);
	}

	@Override
	public boolean existsById(Long id) {
		return categoryReppsitory.existsById(id);
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {
		categoryReppsitory.deleteAllByIdInBatch(ids);
	}

	@Override
	public <S extends Category> boolean exists(Example<S> example) {
		return categoryReppsitory.exists(example);
	}

	@Override
	public void deleteAllInBatch() {
		categoryReppsitory.deleteAllInBatch();
	}

	@Override
	public long count() {
		return categoryReppsitory.count();
	}

	@Override
	public void deleteById(Long id) {
		categoryReppsitory.deleteById(id);
	}

	@Override
	public Category getById(Long id) {
		return categoryReppsitory.getById(id);
	}

	@Override
	public void delete(Category entity) {
		categoryReppsitory.delete(entity);
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		categoryReppsitory.deleteAllById(ids);
	}

	@Override
	public void deleteAll(Iterable<? extends Category> entities) {
		categoryReppsitory.deleteAll(entities);
	}

	@Override
	public void deleteAll() {
		categoryReppsitory.deleteAll();
	}

}
