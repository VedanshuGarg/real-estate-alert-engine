# 🏙️ Real Estate Alert Engine (Gurugram/NCR)

![Java](https://img.shields.io/badge/Java-17-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.x-brightgreen.svg)
![JSoup](https://img.shields.io/badge/JSoup-HTML%20Parsing-blueviolet.svg)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue.svg)

An automated backend web scraping and notification engine designed to monitor localized real estate markets (focusing on the Gurugram/NCR region) for high-value investment opportunities.

This service utilizes background cron jobs to periodically extract property listing data, evaluates the listings against custom investment criteria (e.g., price per sq yard, specific sectors), and pushes real-time alerts via external notification APIs.



## 🏗️ Architecture & Core Features

* **Data Extraction Engine:** Implements `JSoup` to reliably parse dynamic DOM structures from regional real estate listing platforms.
* **Idempotent Processing:** Uses PostgreSQL to track previously processed listing IDs, ensuring duplicate alerts are never sent even if the scraper runs every 5 minutes.
* **Asynchronous Notifications:** Integrates with the Telegram Bot API (or SMTP Mail) to push formatted alerts to the user asynchronously, decoupling the scraping logic from the notification delivery.
* **Cron-Driven Execution:** Leverages Spring's `@Scheduled` annotation for lightweight, reliable background polling.

## 🛠️ Tech Stack
* **Core:** Java 17, Spring Boot 3.x
* **Scraping:** JSoup
* **Persistence:** PostgreSQL via Spring Data JPA
* **Notifications:** REST via Spring `WebClient` / Telegram API

## 📡 Core Domain Model

The primary entity is the `PropertyListing`, which stores the extracted metadata and tracks whether an alert has been dispatched.

```json
{
  "listingId": "PROP-98765",
  "title": "250 Sq Yard Plot in Sector 15, Gurugram",
  "price": 15000000.00,
  "location": "Sector 15, Gurugram, Haryana",
  "url": "[https://example-real-estate.com/prop-98765](https://example-real-estate.com/prop-98765)",
  "discoveredAt": "2026-03-22T10:00:00Z",
  "alertSent": true
}
```

## 🚀 Local Development Setup

1. **Clone the repository:**
   
  ```bash
  git clone [https://github.com/yourusername/real-estate-alert-engine.git](https://github.com/yourusername/real-estate-alert-engine.git)
  cd real-estate-alert-engine
  ```

2. **Configure Environment Variables:**
   
* Provide your database credentials and Telegram Bot Token in the application.yml:

  * DB_USER & DB_PASSWORD
  
  * TELEGRAM_BOT_TOKEN
  
  * TELEGRAM_CHAT_ID
 
3. **Run the Application:**
   
   ```bash
   ./mvnw spring-boot:run
   ```
