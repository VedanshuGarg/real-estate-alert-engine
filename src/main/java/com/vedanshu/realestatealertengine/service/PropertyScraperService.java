package com.vedanshu.realestatealertengine.service;

import com.vedanshu.realestatealertengine.entity.PropertyListing;
import com.vedanshu.realestatealertengine.repository.PropertyListingRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Background worker responsible for extracting real estate data using JSoup.
 * Parses HTML DOM elements and ensures idempotent database processing.
 *
 * @author Vedanshu Garg
 */
@Service
public class PropertyScraperService {

    private static final Logger log = LoggerFactory.getLogger(PropertyScraperService.class);

    private final PropertyListingRepository repository;
    private final NotificationService notificationService;

    public PropertyScraperService(PropertyListingRepository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
    }

    /**
     * Executes every 30 minutes.
     * In a production environment, you might configure this via cron to only run during business hours.
     */
    @Scheduled(fixedRate = 1800000)
    @Transactional
    public void scrapeRealEstateListings() {
        log.info("Initiating localized real estate market scan for Sonipat/NCR...");

        // Note: Using a mock URL for the repository to avoid live-scraping legalities
        // In reality, this would be a search URL tailored to Sonipat sectors and plot sizes
        String targetUrl = "https://example.com/sonipat-real-estate-plots";

        try {
            Document doc = Jsoup.connect(targetUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000)
                    .get();

            Elements propertyCards = doc.select(".property-listing-card");

            for (Element card : propertyCards) {
                String externalId = card.attr("data-id");

                if (repository.existsByExternalListingId(externalId)) {
                    continue;
                }

                String title = card.select(".listing-title").text();
                String location = card.select(".listing-location").text();
                String rawPrice = card.select(".listing-price").text().replaceAll("[^\\d.]", "");
                String link = card.select("a.listing-url").attr("abs:href");

                PropertyListing listing = new PropertyListing();
                listing.setExternalListingId(externalId);
                listing.setTitle(title.isEmpty() ? "Unknown Title" : title);
                listing.setLocation(location);
                listing.setPrice(rawPrice.isEmpty() ? BigDecimal.ZERO : new BigDecimal(rawPrice));
                listing.setSourceUrl(link);
                listing.setDiscoveredAt(LocalDateTime.now());
                listing.setAlertSent(false);

                repository.save(listing);
                log.info("New Property Found! Saved ID: {}", externalId);

                notificationService.sendPropertyAlert(listing);
            }

        } catch (Exception e) {
            log.error("Scraping execution failed due to network or parsing error: {}", e.getMessage());
        }
    }
}
