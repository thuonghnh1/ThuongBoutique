package edu.poly.shop.reppsitory;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.poly.shop.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByNameContaining(String name);

    Page<Product> findByNameContaining(String name, Pageable pageable);
   
    @Query("SELECT p FROM Product p WHERE p.unitPrice = :unitPrice")
    List<Product> findByPrice(@Param("unitPrice") Double unitPrice);

}
