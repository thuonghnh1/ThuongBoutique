package edu.poly.shop.reppsitory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import edu.poly.shop.domain.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	List<Category> findByNameContaining(String name);

    Page<Category> findByNameContaining(String name, Pageable pageable);

//	Page<Category> findAll(Pageable pageable);

}
