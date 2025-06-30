use technical_test;

### Primary key columns setup
 DROP TABLE transaction_data;
 DROP TABLE transaction;
 DROP TABLE transaction_data_sequence;
 DROP TABLE transaction_sequence;

### Sequence of the table transaction
SELECT next_val FROM transaction_sequence;
UPDATE transaction_sequence SET next_val= 5;

### Sequence of the table transaction_data
SELECT next_val FROM transaction_data_sequence;
UPDATE transaction_data_sequence SET next_val= 5;

# SELECT REQUESTS
SELECT * FROM transaction;
SELECT * FROM transaction_data;
SELECT * FROM transaction t INNER JOIN transaction_data td ON t.id = td.transaction_id;

## SELECT BY Id
SELECT * FROM transaction WHERE id = 1;
SELECT * FROM transaction t INNER JOIN transaction_data td ON t.id = td.transaction_id WHERE t.id = 1;

## SELECT BY Type and then Actor
SELECT * FROM transaction WHERE actor= 'Garrett HEDLUNG' AND type = 'Law';
SELECT * FROM transaction t INNER JOIN transaction_data td ON t.id = td.transaction_id WHERE t.type = 'Law' AND t.actor= 'Garrett HEDLUNG';

# CREATE REQUESTS
### ADD Transaction#1
INSERT INTO transaction (id,actor,timestamp,type) VALUES (1,'Legislation service', '2025-05-25 21:23:33.564', 'Law');
INSERT INTO transaction_data (id,customer,employee,place,transaction_id) VALUES (1,'Thales', 'Garrett HEDLUNG', 'Prague',1);

### ADD Transaction#2
INSERT INTO transaction (id,actor,timestamp,type) VALUES (2,'Pre-sales service', '2025-04-25 10:23:33.564', 'Business');
INSERT INTO transaction_data (id,customer,employee,place,transaction_id) VALUES (2,'SII', 'Alexander JOHNSON', 'London',2);

### ADD Transaction#3
INSERT INTO transaction (id,actor,timestamp,type) VALUES (3,'IT service', '2025-05-20 15:23:33.564', 'Engineering');
INSERT INTO transaction_data (id,customer,employee,place,transaction_id) VALUES (3,'Microsoft', 'Emily ROWLING', 'New York',3);

### ADD Transaction#4
INSERT INTO transaction (id,actor,timestamp,type) VALUES (4,'IT service', '2025-06-10 08:23:33.564', 'Engineering');
INSERT INTO transaction_data (id,customer,employee,place,transaction_id) VALUES (4,'Amazon', 'Christiano RODRIGUEZ', 'Madrid',4);

### ADD Transaction#5
INSERT INTO transaction (actor,timestamp,type) VALUES ('actor', 'timestamp', 'type');
INSERT INTO transaction_data (customer,employee,place) VALUES ('customer', 'employee', 'place');

# UPDATE REQUESTS
### UPDATE Transaction#1
UPDATE transaction SET type= 'Business' WHERE id = 1;
UPDATE transaction_data SET employee = 'Alfred SCHMIDT', place= 'Frankfurt' WHERE id = 1;

### UPDATE Transaction#2
UPDATE transaction SET type= 'Engineering' WHERE id = 2;
UPDATE transaction_data SET employee = 'Maximillian BERGER', place= 'Berlin' WHERE id = 2;

### UPDATE Transaction#3
UPDATE transaction SET type= 'Law' WHERE id = 3;
UPDATE transaction_data SET employee = 'Pablo BRUSSEL', place= 'Dusseldorf' WHERE id = 3;

### UPDATE Transaction#4
UPDATE transaction SET type= 'Business' WHERE id = 4;
UPDATE transaction_data SET employee = 'Henrik LASSU', place= 'Vienna' WHERE id = 4;

# DELETE REQUESTS
## DELETE ALL THE ROWS
DELETE FROM transaction_data;
DELETE FROM transaction;

## DELETE BY Id
DELETE FROM transaction_data WHERE id = 1;
DELETE FROM transaction WHERE id = 1;

DELETE FROM transaction_data WHERE id = 2;
DELETE FROM transaction WHERE id = 2;

DELETE FROM transaction_data WHERE id = 3;
DELETE FROM transaction WHERE id = 3;

DELETE FROM transaction_data WHERE id = 4;
DELETE FROM transaction WHERE id = 4;

## DELETE THE NEW RECORD
DELETE FROM transaction_data WHERE id IN (5,6);
DELETE FROM transaction WHERE id IN (5,6);