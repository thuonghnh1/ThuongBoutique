package edu.poly.shop.reppsitory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.poly.shop.domain.Category;
@Repository
public interface CategoryReppsitory extends JpaRepository<Category, Long>{

}
