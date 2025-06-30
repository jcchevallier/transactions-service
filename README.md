### CRUD Service for dealing with transactions
Authentication service generates information about transactions. Each transaction contains following information:
-	ID (integer)
-	Timestamp
-	Type (string)
-	Actor (string)
-	Transaction data (key-value map of strings)

The transactions must be collected by a new service. The service should receive the data at HTTP interface, store them in SQL database 
and make them available via the HTTP interface. Implement the service for CRUD (Create Update Delete) and search operations. 
Suggest and design what the search operation should look like to be usable. Implement it as a Spring application using MySQL database.

The architecture of the service is built upon the principles of the 3-tier architecture. The version of the jdk is 17.

# Model
The model is divided between two entities : Transaction and TransactionData. 

The model Transaction respects your requirements. The model TransactionData contains three string properties : customer, employee and place.
Both the columns Timestamp from the table Transaction and Employee from the table TransactionData have a unique constraint. 
These two tables are linked with a one-to-one relationship. 

The file transaction.sql in the folder src/main/resources/ lays down all the instructions to initialize and then manage the data 
in the tables of the mySQL database. 

# Controller
The controller has the basic CRUD features. Besides, i added a research function based upon the parameters type and actor from the model Transaction. 

Because the column timestamp is unique in the table Transaction, i created the method getTransactionByTimestamp in the service layer 
which helps identifying wether a transaction coming from the client already exists in the database in the case of the feature for adding 
a new transaction in the database.

# Java Bean validation
When it comes to the feature of adding a new transaction in the database, i created a class named NewTransactionRequestJSON.java which maps 
the parameters of the request for adding a new transaction. I joined annotations to the parameters of the constructor of NewTransactionRequestJSON.java 
so that the client can receive notifications for some of the errors coming from the request. 

# Exception handling
In order to give detailed notifications about the cause of a failed request to the client, i created the class DefaultExceptionHandler.java
which centralizes the treatment of the different exceptions which can be triggered. The treatment of each exception is generic :  
Instead of the blurred information "Error server" associated with the http status code 500, the client will be notified with an accurate message 
which is defined in the exceptions thrown in the methods of the service layer, a list of errors related to wrong parameters 
in the case of the feature for adding a new transaction and an accurate http status code.

# Unit tests
I wrote unit tests covering the service layer. Happy paths and exceptions are part of the test coverage.
I also wrote integration tests related to the controller layer. All these tests are geared towards happy paths.  
