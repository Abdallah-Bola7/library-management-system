# Library Management System - Entity Relationship Diagram

This document describes the Entity Relationship Diagram (ERD) for the Library Management System.

## Entities

### User
- Represents system users (Librarians, Admins, Staff)
- Has role-based access control
- Manages book borrowing operations

### Book
- Core entity representing library books
- Tracks total and available copies
- Connected to authors, categories, and publisher
- Contains metadata like ISBN, publication year, etc.

### Author
- Represents book authors
- Can be associated with multiple books
- Includes biographical information

### Category
- Hierarchical structure for book categorization
- Self-referential relationship for parent-child categories
- Books can belong to multiple categories

### Publisher
- Represents book publishers
- Contains publisher contact information
- One publisher can have many books

### Member
- Represents library members/borrowers
- Tracks membership status and personal information
- Has unique membership number

### BorrowRecord
- Tracks book lending transactions
- Records borrow and return dates
- Links books, members, and staff involved
- Maintains borrowing status

### Role
- Defines user roles in the system
- Used for access control
- Examples: ADMIN, LIBRARIAN, STAFF

## Relationships

1. Book - BorrowRecord: One-to-Many
   - One book can have multiple borrow records
   - Each borrow record belongs to one book

2. Member - BorrowRecord: One-to-Many
   - One member can have multiple borrow records
   - Each borrow record belongs to one member

3. User - BorrowRecord: One-to-Many (Two relationships)
   - As issuer: User who checked out the book
   - As returner: User who processed the return

4. Book - Publisher: Many-to-One
   - Many books can belong to one publisher
   - Each book has exactly one publisher

5. Book - Author: Many-to-Many
   - Books can have multiple authors
   - Authors can write multiple books

6. Book - Category: Many-to-Many
   - Books can belong to multiple categories
   - Categories can contain multiple books

7. Category - Category: One-to-Many (Self-referential)
   - Categories can have parent categories
   - Categories can have multiple child categories

8. User - Role: Many-to-Many
   - Users can have multiple roles
   - Roles can be assigned to multiple users

## Notes

- All entities have unique identifiers (Primary Keys)
- Timestamps are used for auditing where appropriate
- Unique constraints are applied on:
  - Book: ISBN
  - Member: membership_number, email
  - User: username, email
- Status enums are used for:
  - Member: MembershipStatus
  - BorrowRecord: BorrowStatus 