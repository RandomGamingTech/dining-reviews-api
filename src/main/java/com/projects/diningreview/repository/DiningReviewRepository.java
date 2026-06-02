package com.projects.diningreview.repository;

import com.projects.diningreview.models.DiningReview;
import com.projects.diningreview.models.ReviewStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DiningReviewRepository extends CrudRepository<DiningReview, Long> {
    List<DiningReview> findByReviewStatus(ReviewStatus reviewStatus);
    List<DiningReview> findByRestaurantIdAndReviewStatus(Long id, ReviewStatus reviewStatus); // por ver aún, pendiente para mañana
}
