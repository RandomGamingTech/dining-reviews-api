package com.projects.diningreview.repository;

import com.projects.diningreview.models.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends CrudRepository<Restaurant, Long> {
    Optional<Restaurant> findByName(String name);
    List<Restaurant> findByZipCodeOrderByPeanutScoreDesc(String zipCode);
    List<Restaurant> findByZipCodeOrderByEggScoreDesc(String zipCode);
    List<Restaurant> findByZipCodeOrderByDairyScoreDesc(String zipCode);
}
