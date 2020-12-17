Main rules: 
- Clean code, based on Martin's book 
- Code in repository with frequently changes 
- jUnit test coverage > 65% 
- Functional working of application must be proofed by unit test. No frontend. 



Bank has a name. 

- as a director of a bank, I can generates reports cross all data. Ex: history of transactions (per day, per client, per type of transaction), 5 newest clients, 10 transactions with highes amount 

Bank has emloyees 

- employee can accept new opened bank account 
- employee can view a personal data of each client 

Bank has many customers 
- each customer has login, password, first name, last name, date of birth, date of joned to the bank.
- after login process, clinet can request for new account.
- new account must be approved by a bank employee, before any transaction will be done 
- client can generate a report with history of transaction per chosen account. 
- client can generate a report with history of transaction cross all accounts but from chosen data range 

Each customer can have one or many account 
- each account has number (10 digits: first 2 is a bank code, next 5 is client identifier, last 3 are account identifier)
- each account has current amount 
- each account has a type: 
-- regular - simple account to do money transfer 
-- saving - money transfer are allowed only to regular account. Interest is 0.01% per each minute. Insert is added to amount of account during a transfer to regular account 
-- international - account to do international transfers. This type of account has additional field: IBAN

Each client can do following transfers 
- regular debit - (adding money into my account) with fields: amount (per each account), data of transaction, from account, number of transaction (per account) 
- regular credit - (payout from account) with fields: amount (per each account), data of transaction, to account, number of transaction (per account) 
- international wire - (only payout from account) with fields: amount (per each account), data of transaction, to account, IBAN, number of transaction (calculated per all international accounts) 
