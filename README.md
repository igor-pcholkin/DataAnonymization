# Getting Started

 <!-- TOC -->
* [Getting Started](#getting-started)
    * [What is the data anonymization application?](#what-is-the-data-anonymization-application)
    * [What is a sensitive data?](#what-is-a-sensitive-data-)
    * [Why sensitive data should be kept secure?](#why-sensitive-data-should-be-kept-secure)
    * [Anonymization approach](#anonymization-approach)
    * [How does the application work?](#how-does-the-application-work)
    * [What anonymizers are available?](#what-anonymizers-are-available)
    * [Setup](#setup)
    * [Configuration](#configuration)
    * [How to add a custom anonymizer](#how-to-add-a-custom-anonymizer)
    * [Additional functions](#additional-functions)
    * [Running the application](#running-the-application)
    * [Use cases](#use-cases)
    * [Reference Documentation](#reference-documentation)
<!-- TOC -->

### What is the data anonymization application? 

The data anonyization application is a command-line tool designed to safeguard sensitive data. 
It achieves this by masking of the sensitive data within an enterprise's relational database.
The anonymization reduces risk of unintended disclosure of the sensitive data while still keeping it useful for
business processing. The European Union's General Data Protection Regulation (GDPR) requires that stored data 
on people in the EU undergo either anonymization or a pseudonymization process.

### What is a sensitive data? 

Sensitive information is whatever data that can be linked or attributed to a specific person, company or client.
Examples of sensitive information include data about customers: company name, names of official representatives,
phones, emails, addresses, credit cards, birth dates, medical information.

### Why sensitive data should be kept secure?

Securing sensitive data is crucial for every digital business. 
When sensitive information about customers is leaked to the public, it can result in direct and indirect 
losses for the company, including reputational and financial damage.

### Anonymization approach

In the data protection application, anonymization is accomplished through a data masking approach.
This technique involves replacing sensitive values, such as personal identification numbers, credit card numbers, social security numbers, 
and email addresses, with fictitious or randomized values. 
Importantly, this process ensures that the structure and, potentially, the semantic meaning of the data are preserved while safeguarding its confidentiality..

### How does the application work?

The dataprotection application retrieves data from a relational database, performs anonymization on it, 
and writes it back to the database. 
Anonymization is done according to rules specified in a transformation configuration file. 
The transformation configuration file outlines which specific columns in the database schema and 
tables should be anonymized, as well as how - as identified by identifier of anonymizer to use.

### What anonymizers are available?

Currently, the following anonymizers are available, with the possibility of additional ones being introduced in the future.
Displayed on the left is the ID of each anonymizer, which should be specified in the transformation YAML file. 

**name** - the anonymizer replaces alphabetic letters with random alternatives while preserving the initial letters<br/>
**email** - changes email address to a randomly looking one<br/>
**address** - the anonymizer that obfuscates however preserves the appearance of addresses by maintaining spaces and punctuation marks while obscuring the underlying data<br/>
**birthdate** - the date anonymizer generates a new random date, effectively changing the original date<br/>
**ccard** - the credit card anonymizer replaces credit card numbers with randomly generated ones<br/>
**pid** - a basic "one-size-fits-all" anonymizer capable of handling various personal identification codes, social security numbers, and tax payer numbers<br/>
**post** - the post code anonymizer replaces post codes with alternative values sourced from the same column in the database<br/>
**ip** - the ip address anonymizer replaces ip address with another random ip address<br/>

### Setup

This is a console application which requires Java 19 (which should be downloaded and setup separately).
After java 19 is installed, set JAVA_HOME to the corresponding folder, e.g.  

`export JAVA_HOME=~/.jdks/corretto-19.0.2`

The application can be built using `mvnw package` command run in the project folder.

It will build zip packages for windows and linux and place it into `target` folder.
After unpacking one of the packages you should be able to run the `dp.sh` script with arguments as shown below.

### Configuration

* **conf/application.properties** file should be used to set up DB connection URL and credentials.
  This (the only) DB will be used for anonymization and data analysis. <br/>
* **AnonymizerTable.java** - (optionally) for registering a custom anonymizer <br/>
* **columnToAnonymizer.properties** (optionally) - the file is used when adding new anonymizer and using it for auto-detection of columns
  in database for anonymization.

### How to add a custom anonymizer

* Add a new anonymizer class (with test) to com.discreet.dataprotection.anonymizer package. Extend it either from BaseAnonymizer or CharSequenceAnonymizer.
* Register the anonymizer class with corresponding alias in AnonymizerTable.java
* Use anonymizer alias as a value mapped to db column in transformations.yaml file.
* (Optionally) for using anonymizer in auto-detection process, map it to corresponding column name in columnToAnonymizer.properties

### Additional functions

The application autonomously identifies which database columns are suitable for anonymization by leveraging metadata information about the database. 
This information can be provided in two ways: through a Data Definition Language (DDL) file containing statements used to create data tables in a database schema, or via a direct connection to the database schema.

### Running the application

Application can be started used supplied script: dp.bat for windows or dp.sh for Linux or MacOS.
The application requires Java 19 (or newer version), which should be installed separately.
Upon execution without arguments, the application provides information about available command line arguments. 
At the moment the application works in 2 modes: "transform" and "detect", each represented by corresponding 
command and related options:

* **transform** </br>
  _anonymize database_ 
  * -tfn, --transformationsFileName=[transformationsFileName] </br>
  _yaml file describing for each schema/table how each column should be anonymized_
* **detect** </br>
  _auto detect what schema tables and columns in a given columns can be anonymized. The result is a temporary 
transformations.yaml file which can be later used with "transform" command._
  * -dbe, --dbEngine=[dbEngine] </br>
  _one of mysql, oracle, postgresql, db2, jtds, sybase, sqlserver, mariadb, derby, hive, h2, informix_
  * -dsn, --defaultSchemaName=[defaultSchemaName] </br>
  _name to use in case if schema name is missing in DDL schema_
  * -iid, --ignoreMissingIds </br>
  _ignore db tables with missing or not detected id columns_
  * -sfn, --schemaFileName=[schemaFileName] </br>
  _ddl schema file name to use for auto-detection of DB table columns which can be anonymized_
  * -sn, --schemaName=[schemaName] </br>
  _schema name to use for reading metadata from DB and auto-detection of DB table columns which can be anonymized_

### Use cases

**1a.** Auto-detect data eligible for anonymization using supplied DDL schema file:

`./dp.sh detect -dbe mysql -dsn test -sfn schema.sql -iid`

- it will automatically identify database columns in the schema that likely require anonymization. 
Subsequently, it will generate a transformation.yaml file, which can be further refined through post-processing (manual editing) and subsequently utilized for actual anonymization. 
It's important to note that at this stage, no data in the database schema is altered.
-dbe argument is used to supply db dialect. -iid is an optional, yet recommended, option that enables the application to ignore errors and continue processing if it fails to automatically detect the ID column for a specific database table. 
Without this option, the application will generate an error and halt the autodetection process.

or:<br/>
**1b.** Auto-detect data eligible for anonymization using supplied schema name. 

`./dp.sh detect -sn test`

- In this step, the application behaves similarly to the previous one, but instead of relying on a DDL file, it loads schema metadata directly from the database. 
This approach is useful when a DDL file is unavailable. 
- In the provided example, "test" represents the name of the schema to be analyzed in the database as defined in the application.properties file.

**1c.** Generate transformation.yaml file manually using transformations.yaml.example as an example using the following pattern:

[schema name]&#58;<br/>
&nbsp;&nbsp;[db table name]:<br/>
&nbsp;&nbsp;&nbsp;&nbsp;anonymizers:<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[column name]: [anonymizer id]<br/>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;...<br/>
&nbsp;&nbsp;...<br/>

**2.** Use the generated file transformation.yaml for actual anonymization of the selected schema:

`./dp.sh transform -tfn transformations.yaml`

### Reference Documentation
For further reference, please consider the following links:

* [General Data Protection Regulation](https://gdpr-info.eu/)
* [Health Insurance Portability and Accountability Act](https://en.wikipedia.org/wiki/Health_Insurance_Portability_and_Accountability_Act)
