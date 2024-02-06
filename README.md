# Getting Started

### What is the dataprotection application?

The dataprotection application is a command-line tool designed to safeguard sensitive data. 
It achieves this by anonymizing the sensitive data within an enterprise's relational database.

### What is sensitive data? 

Examples of sensitive information include data about customers: company name, names of official representatives,
phones, emails, addresses, credit cards, birth dates.

### Why sensitive data should be kept secure?

Securing sensitive data is crucial for every digital business. 
When sensitive information about customers is leaked to the public, it can result in direct and indirect 
losses for the company, including reputational and financial damage.

### How does it work?

The dataprotection application retrieves data from a relational database, processes it, and writes it back to the database. 
Data processing involves anonymizing it according to rules specified in a transformation configuration file. 
Anonymizing data is a light form of one-way encryption that maintains the structure and possibly the semantic meaning of the data. 
The transformation configuration file outlines which specific columns in the database schema and 
tables should be anonymized, as well as how

### Reference Documentation
For further reference, please consider the following sections:

* [General Data Protection Regulation](https://gdpr-info.eu/)
