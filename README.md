# Timesheet
Timesheet is a collection of JAR files that can be used to manage a local time sheet.

#### Setting up (for command line)
Package Timesheet by running the Maven command `mvn clean package`. Once Timesheet has been successfully packaged, there 
will be four JAR files and a JSON file in the `bin` directory. Each JAR file has its own use case for interacting with 
the JSON file (the time sheet).

The table below details each JAR file: 

| Number           | JAR        | Use Case           | Command           | 
| :-------------: | :-------------: |  :-------------: |:-------------:| 
| 1 | checkin.jar| Add an IN entry to the time sheet. | `java -jar checkin.jar` | 
| 2 | checkout.jar| Add an OUT entry to the time sheet. | `java -jar checkout.jar` | 
| 3 | report.jar| Produce a report on the total hours logged. | `java -jar report.jar` | 
| 4 | status.jar| Show the latest entry from the time sheet. | `java -jar status.jar` | 

#### Setting up (for IDE)
There are five modules within Timesheet. One is a simple library module; the remaining four modules are runnable. 

The table below details the required environment variables:   

| Number           | Key        | Value           |  
| :-------------: | :-------------: |  :-------------: 
| 1 | CLASSPATH_CONTEXT| true |

#### How to use
Simply run the applications in a `checkin` then `checkout` sequence; eventually running `report` and `status` when required. 

#### Notes 
* Built with Java 1.8. 
* An example of a time sheet can be found in `.dev`.  
* After packaging Timesheet, each module will have its own `timesheet.json` file. These JSON files
can be discarded and/or ignored.   