package com.needleinnovision.searchengine.persistence.repository;

import com.needleinnovision.searchengine.persistence.entity.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListingRepository extends JpaRepository<Listing, Integer> {
 Listing findByName(String listingName);
}
