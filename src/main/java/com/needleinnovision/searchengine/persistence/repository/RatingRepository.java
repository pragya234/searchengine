package com.needleinnovision.searchengine.persistence.repository;

import com.needleinnovision.searchengine.persistence.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Rating findByName(String ratingName);
}
