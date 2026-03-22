package com.vedanshu.realestatealertengine.controller;

import com.vedanshu.realestatealertengine.entity.PropertyListing;
import com.vedanshu.realestatealertengine.repository.PropertyListingRepository;
import com.vedanshu.realestatealertengine.service.PropertyScraperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for interacting with the Real Estate Alert Engine.
 * Allows manual triggering of the scraper and retrieval of recent findings.
 *
 * @author Vedanshu Garg
 */
@RestController
@RequestMapping("/api/v1/properties")
public class PropertyController {

    private final PropertyScraperService scraperService;
    private final PropertyListingRepository repository;

    public PropertyController(PropertyScraperService scraperService, PropertyListingRepository repository) {
        this.scraperService = scraperService;
        this.repository = repository;
    }

    /**
     * Retrieves all tracked property listings from the database.
     * In a production app, this would be paginated.
     *
     * @return a list of all scraped properties
     */
    @GetMapping
    public ResponseEntity<List<PropertyListing>> getAllProperties() {
        return ResponseEntity.ok(repository.findAll());
    }

    /**
     * Manually triggers the extraction engine outside of its normal cron schedule.
     * Great for on-demand market scanning.
     *
     * @return a success message
     */
    @PostMapping("/scrape")
    public ResponseEntity<String> triggerManualScrape() {
        // Run the scraping logic asynchronously so the HTTP request doesn't hang
        new Thread(scraperService::scrapeRealEstateListings).start();
        return ResponseEntity.accepted().body("Manual scraping job initiated. Alerts will be sent asynchronously.");
    }
}
