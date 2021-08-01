INSERT INTO ACCOUNT (account_number, account_balance, currency) VALUES
  (88888888, 1000000.00, 'HKD'),
  (12345678, 1000000.00, 'HKD');

INSERT INTO TRANSACTION (tx_id,account_number, transaction_type,transaction_amount, balance) VALUES
  (1,88888888,'DEPOSIT', 1000000.00,1000000.00),
  (2,12345678,'DEPOSIT', 1000000.00,1000000.00);