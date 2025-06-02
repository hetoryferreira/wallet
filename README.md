# Wallet Service

A RESTful API for managing user wallets, balances, and transactions.  
Built with **Java 17+, Spring Boot, H2, Spring Data JPA, Lombok, and Springdoc OpenAPI**.

---

## Features

- **Wallet Management:** Create and retrieve wallets by ID or document number
- **Balance Inquiry:** Get current wallet balance
- **Transactions:** Deposit, withdraw, transfer funds, and retrieve transaction history (with date filter)

---


##                                  Endpoints Overview
## Wallets
-Method	Path	Description
- POST	/wallets	Create new wallet
- GET	/wallets/{walletId}	Get wallet by ID
- GET	/wallets/by-document/{documentNumber}	Get wallet by document

## Balance
- Method	Path	Description
- GET	/balance/{walletId}	Get current wallet balance

## Transactions
- Method	Path	Description
- GET	/transactions/wallet/{walletId}	List transactions (filter by dates optional)
- POST	/transactions/deposit	Deposit into wallet
- POST	/transactions/withdraw	Withdraw from wallet
- POST	/transactions/transfer	Transfer funds between wallets

---




## Tech Stack

- Java 21+
- Spring Boot 3.5
- Spring Data JPA
- Lombok
- Gradle
- Springdoc OpenAPI (Swagger UI)
- JUnit 5 & Mockito
- H2 (in-memory database)
- Acesso ao H2 : http://localhost:8080/h2/login.jsp
- User Name: wallet
- Password:  wallet


![img.png](img.png)


---

## Requirements

- Java 17 or later
- Gradle 7.6+ (wrapper included, no install needed)
- Internet connection (for dependency download)

---
## Installation

```sh
git clone https://github.com/seu-usuario/seu-repo-wallet.git
cd seu-repo-wallet
./gradlew clean build



