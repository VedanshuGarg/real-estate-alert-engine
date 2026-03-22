package com.vedanshu.realestatealertengine.repository;

import com.vedanshu.realestatealertengine.entity.PropertyListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Manages database operations for the scraped property listings.
 *
 * @author Vedanshu Garg
 */
@Repository
public interface PropertyListingRepository extends JpaRepository<PropertyListing, Long> {

    /**
     * Checks if a listing from the external site has already been processed.
     * Crucial for idempotency and preventing duplicate alerts.
     */
    boolean existsByExternalListingId(String externalListingId);
}
