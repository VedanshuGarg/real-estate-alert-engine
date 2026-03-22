package com.vedanshu.realestatealertengine.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a localized real estate property extracted from an external platform.
 * Tracks notification state to prevent duplicate alerts.
 *
 * @author Vedanshu Garg
 */
@Entity
@Table(name = "property_listings")
public class PropertyListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The unique ID provided by the scraping source (e.g., "PROP-12345")
    @Column(nullable = false, unique = true, name = "external_listing_id")
    private String externalListingId;

    @Column(nullable = false)
    private String title;

    private BigDecimal price;

    private String location;

    @Column(nullable = false, length = 1000)
    private String sourceUrl;

    @Column(nullable = false, name = "discovered_at")
    private LocalDateTime discoveredAt;

    @Column(nullable = false, name = "alert_sent")
    private boolean alertSent = false;

    public PropertyListing() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getExternalListingId() { return externalListingId; }
    public void setExternalListingId(String externalListingId) { this.externalListingId = externalListingId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getSourceUrl() { return sourceUrl; }
    public void setSourceUrl(String sourceUrl) { this.sourceUrl = sourceUrl; }
    public LocalDateTime getDiscoveredAt() { return discoveredAt; }
    public void setDiscoveredAt(LocalDateTime discoveredAt) { this.discoveredAt = discoveredAt; }
    public boolean isAlertSent() { return alertSent; }
    public void setAlertSent(boolean alertSent) { this.alertSent = alertSent; }
}
