# Smart Finance - Personal Financial Organizer

This project aims to be a comprehensive personal financial organizer, helping users manage their income, expenses, and financial goals.

## Project Structure

The project is built using Spring Boot and follows a standard MVC-like architecture.

## Core Functionalities Checklist

This checklist outlines the essential features of a personal financial management system. We will mark each item as `[x]` when it's implemented and verified.

### 1. User Management
- [x] User Registration
- [x] User Login (Authentication)
- [x] User Profile Management (View/Edit)
- [x] Password Reset/Change

### 2. Transaction Management
- [x] Record Income
- [x] Record Expenses
- [x] View All Transactions (with filtering/sorting)
- [x] Edit Transactions
- [x] Delete Transactions
- [x] Categorize Transactions

### 3. Category Management
- [x] Create Custom Categories
- [x] View Categories
- [x] Edit Categories
- [x] Delete Categories

### 4. Reporting and Analytics
- [x] Summary of Income vs. Expenses
- [x] Spending by Category
- [x] Monthly/Annual Reports
- [x] Budgeting (Setting limits for categories)

### 5. Account Management (Optional)
- [ ] Manage Multiple Bank Accounts/Wallets
- [ ] Transfer Funds Between Accounts

## Transaction API Contract

Transaction endpoints require a JWT in the `Authorization: Bearer YOUR_JWT` header:

- `GET /transactions` and `GET /transactions/{id}` retrieve transactions for the authenticated user.
- `POST /transactions` creates a transaction.
- `PUT /transactions/{id}` updates a transaction owned by the authenticated user.
- `DELETE /transactions/{id}` deletes a transaction owned by the authenticated user.

Create and update requests use this JSON shape:

```json
{
  "amount": 48.50,
  "date": "2026-11-10",
  "description": "Lunch",
  "categoryId": 2,
  "type": "EXPENSE"
}
```

`amount` must be positive, `date`, `description`, `categoryId`, and `type` are required, and `type` must be `INCOME` or `EXPENSE` (case-insensitive; responses use uppercase). For compatibility with the current mobile sync payload, requests may also use `transactionDate` instead of `date`. The category must belong to the authenticated user; the API determines the transaction owner from the JWT rather than trusting a client-supplied `userId`.

## How to Run

(Instructions on how to build and run the application will be added here later.)

## Development Notes

(Any specific development notes or decisions will be added here.)
