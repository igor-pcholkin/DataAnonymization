# Getting Started

### What is the dataprotection application?

The dataprotection application is a command line tool which ensures security of a sensitive data.
It does it by anonymizing the sensitive data in a relational database of enterprise.

### What is sensitive data? 

Examples of sensitive information include data about customers: company name, names of official representatives,
phones, emails, addresses, credit cards, birth dates.

### Why sensitive data should be kept secure?

Securing sensitive data is important for every digital business. When sensitive information about customers is
leaked into public it can product direct and indirect losses for the company including reputational and 
financial losses.

### How does it work?

The dataprotection application retrieves data from a relational database, processes and
writes it back to the database. Processing data means anonymizing it according to rules defined in a transformation
configuration file. Anonymizing data is a light form of one-way encryption which preserves a structure and possibly semantic
meaning of data. The transformation configuration file describes which specific columns in which database schema and 
tables should be anonymized and how. 

### Reference Documentation
For further reference, please consider the following sections:

* [General Data Protection Regulation](https://gdpr-info.eu/)

