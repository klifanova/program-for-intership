# Calculator of the amount of time for a student who has an individual program.

Console program that allows you to determine - considering the current date - when the student will complete the course.
Calculate how many days and hours the student still has before the end of the program or how many days and hours ago the
student completed the course.

## Technical Requirements

* Java 17
* Maven 4.0.0
* Git

## How to run

1) `cd../courses-for-students`

2) `mvn clean install`

3) `java -jar target/courses-for-students-1.0-SNAPSHOT.jar  <fileName> <date> <reportDataType>`

Parameters, which we need to use:

`fileName` - the file name

`date` - the specific date for which report will be generated

`reportDataType` - the form of report data (full/short)

An example of report data:

`java -jar target/courses-for-students-1.0-SNAPSHOT.jar  src/main/resources/students-data.json 2022-04-05T10:00:00.00Z full`

4) To run test :`mvn test`