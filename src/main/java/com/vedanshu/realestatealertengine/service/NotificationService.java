package com.vedanshu.realestatealertengine.service;

import com.vedanshu.realestatealertengine.entity.PropertyListing;

/**
 * Interface for decoupling the scraping logic from the notification delivery mechanism.
 *
 * @author Vedanshu Garg
 */
public interface NotificationService {
    void sendPropertyAlert(PropertyListing property);
}
