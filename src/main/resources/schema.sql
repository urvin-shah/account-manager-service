CREATE TABLE ACCOUNT (
  account_number INT PRIMARY KEY,
  account_balance DOUBLE NOT NULL,
  currency VARCHAR(3) NOT NULL
);

CREATE TABLE TRANSACTION (
  tx_id INT AUTO_INCREMENT PRIMARY KEY,
  account_number INT NOT NULL,
  transaction_type VARCHAR(10) NOT NULL,
  transaction_amount DOUBLE NOT NULL,
  transaction_time TIMESTAMP NOT NULL,
  balance DOUBLE NOT NULL
);