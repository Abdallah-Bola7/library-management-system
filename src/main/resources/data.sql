-- Roles
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_LIBRARIAN');
INSERT INTO roles (name) VALUES ('ROLE_STAFF');

-- Users (password is 'password' encoded with BCrypt)
INSERT INTO users (username, email, password, active) VALUES 
('admin', 'admin@library.com', '$2a$10$rDvSGR1P6JB.PB7UX/WBKOGYFp3gX9R9d9wlm.YT9gM/HHHbGR7Gy', true),
('librarian', 'librarian@library.com', '$2a$10$rDvSGR1P6JB.PB7UX/WBKOGYFp3gX9R9d9wlm.YT9gM/HHHbGR7Gy', true),
('staff', 'staff@library.com', '$2a$10$rDvSGR1P6JB.PB7UX/WBKOGYFp3gX9R9d9wlm.YT9gM/HHHbGR7Gy', true);

-- User Roles
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); -- admin -> ROLE_ADMIN
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2); -- librarian -> ROLE_LIBRARIAN
INSERT INTO user_roles (user_id, role_id) VALUES (3, 3); -- staff -> ROLE_STAFF

-- Categories
INSERT INTO categories (name, description) VALUES 
('Fiction', 'Fictional literature'),
('Non-Fiction', 'Non-fictional literature'),
('Science Fiction', 'Science fiction books'),
('Mystery', 'Mystery and detective books'),
('Biography', 'Biographical books');

-- Update category hierarchies
UPDATE categories SET parent_id = 1 WHERE name = 'Science Fiction';
UPDATE categories SET parent_id = 1 WHERE name = 'Mystery';
UPDATE categories SET parent_id = 2 WHERE name = 'Biography';

-- Publishers
INSERT INTO publishers (name, description, address, phone, email, website) VALUES 
('Penguin Books', 'Major publishing house', '123 Publisher St', '123-456-7890', 'contact@penguin.com', 'www.penguin.com'),
('Random House', 'Leading publisher', '456 Random St', '098-765-4321', 'contact@randomhouse.com', 'www.randomhouse.com'),
('HarperCollins', 'International publisher', '789 Harper St', '456-789-0123', 'contact@harpercollins.com', 'www.harpercollins.com');

-- Authors
INSERT INTO authors (name, biography) VALUES 
('J.K. Rowling', 'British author best known for Harry Potter series'),
('George Orwell', 'English novelist and essayist'),
('Jane Austen', 'English novelist known for romantic fiction'),
('Stephen King', 'American author of horror and suspense novels');

-- Books
INSERT INTO books (title, isbn, publisher_id, language, publication_year, edition, summary, total_copies, available_copies) VALUES 
('1984', '978-0451524935', 1, 'English', 1949, 'First Edition', 'A dystopian social science fiction novel', 5, 3),
('Pride and Prejudice', '978-0141439518', 2, 'English', 1813, 'Classic Edition', 'A romantic novel of manners', 3, 2),
('The Shining', '978-0307743657', 3, 'English', 1977, 'Reprint', 'A horror novel', 4, 4);

-- Book Authors
INSERT INTO book_authors (book_id, author_id) VALUES 
(1, 2), -- 1984 -> George Orwell
(2, 3), -- Pride and Prejudice -> Jane Austen
(3, 4); -- The Shining -> Stephen King

-- Book Categories
INSERT INTO book_categories (book_id, category_id) VALUES 
(1, 1), -- 1984 -> Fiction
(1, 3), -- 1984 -> Science Fiction
(2, 1), -- Pride and Prejudice -> Fiction
(3, 1), -- The Shining -> Fiction
(3, 4); -- The Shining -> Mystery

-- Members
INSERT INTO members (first_name, last_name, email, phone, address, membership_number, status) VALUES 
('John', 'Doe', 'john.doe@email.com', '123-456-7890', '123 Main St', 'MEM001', 'ACTIVE'),
('Jane', 'Smith', 'jane.smith@email.com', '098-765-4321', '456 Oak St', 'MEM002', 'ACTIVE'),
('Bob', 'Johnson', 'bob.johnson@email.com', '456-789-0123', '789 Pine St', 'MEM003', 'ACTIVE');

-- Borrow Records
INSERT INTO borrow_records (book_id, member_id, borrow_date, due_date, status, issued_by_id) VALUES 
(1, 1, CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP + INTERVAL '20 days', 'BORROWED', 2),
(2, 2, CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP + INTERVAL '25 days', 'BORROWED', 2); 