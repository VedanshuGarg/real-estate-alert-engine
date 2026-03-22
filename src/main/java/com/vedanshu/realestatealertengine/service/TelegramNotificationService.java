package com.vedanshu.realestatealertengine.service;

import com.vedanshu.realestatealertengine.entity.PropertyListing;
import com.vedanshu.realestatealertengine.repository.PropertyListingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Implementation of the NotificationService that pushes real-time alerts
 * to a configured Telegram chat using the Telegram Bot API.
 * Uses reactive WebClient for non-blocking network I/O.
 *
 * @author Vedanshu Garg
 */
@Service
public class TelegramNotificationService implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(TelegramNotificationService.class);

    private final WebClient webClient;
    private final PropertyListingRepository repository;

    @Value("${spring.telegram.bot-token}")
    private String botToken;

    @Value("${spring.telegram.chat-id}")
    private String chatId;

    public TelegramNotificationService(WebClient.Builder webClientBuilder, PropertyListingRepository repository) {
        this.webClient = webClientBuilder.baseUrl("https://api.telegram.org").build();
        this.repository = repository;
    }

    @Override
    public void sendPropertyAlert(PropertyListing property) {
        if (property.isAlertSent()) {
            log.debug("Alert already sent for property ID: {}", property.getExternalListingId());
            return;
        }

        log.info("Dispatching Telegram alert for new property: {}", property.getExternalListingId());

        String messageText = String.format(
                "*New Property Alert!*\n\n" +
                        "*Location:* %s\n" +
                        "*Price:* ₹%s\n" +
                        "*Details:* %s\n\n" +
                        "[View Listing](%s)",
                property.getLocation(),
                property.getPrice(),
                property.getTitle(),
                property.getSourceUrl()
        );

        Map<String, String> payload = Map.of(
                "chat_id", chatId,
                "text", messageText,
                "parse_mode", "Markdown"
        );

        webClient.post()
                .uri("/bot{token}/sendMessage", botToken)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    log.info("Telegram alert sent successfully for ID: {}", property.getExternalListingId());
                    property.setAlertSent(true);
                    repository.save(property);
                })
                .doOnError(error -> log.error("Failed to send Telegram alert: {}", error.getMessage()))
                .onErrorResume(e -> Mono.empty())
                .subscribe();
    }
}
