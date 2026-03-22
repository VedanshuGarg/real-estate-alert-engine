package com.vedanshu.realestatealertengine.service;

import com.vedanshu.realestatealertengine.entity.PropertyListing;
import org.springframework.stereotype.Service;

/**
 * Interface for decoupling the scraping logic from the notification delivery mechanism.
 *
 * @author Vedanshu Garg
 */
@Service
public interface NotificationService {
    void sendPropertyAlert(PropertyListing property);
}
