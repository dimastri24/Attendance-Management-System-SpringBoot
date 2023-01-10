## Attendance Management System REST API

## About the project
It's and API of attendance management system build on Spring Boot and MySQL for the DBMS. There are three type of user, Admin, Manager and Employee. Each could do certain of action. Manager and Employee can check in and check out attendance. Each management are based on which Store are they registered.

## How to run project
- clone this project and remove the remote git
- import into your IDE or text editor
- create database
- do maven update project
- run the springboot project
- insert these role to the table
```
INSERT INTO `jumpstart-attendance-db`.`roles` (`name`) VALUES ('ROLE_EMPLOYEE');
INSERT INTO `jumpstart-attendance-db`.`roles` (`name`) VALUES ('ROLE_MANAGER');
INSERT INTO `jumpstart-attendance-db`.`roles` (`name`) VALUES ('ROLE_ADMIN');
```
- insert user admin, two ways
	- first manually add into the table users and user_role
	- second, disable security by commented out the `@Secured` above the `addAdmin` controller under `EmployeeController` then add user through postman or other tools

## Current Status
- cannot generate pdf report of employee attendance
- when running on jvm 17 cannot do the refresh token jwt

## What can it do
- Add admin, Add normal employee
- Get an employee, List employees, Edit employee, Delete employee
- Register user, Login and Logout
- Get profile, Edit profile
- Add store, Get store, List stores, Edit store, Delete store
- Attendance check in, Attendance check out
- Get attendance, List attendances, Edit attendance, Delete attendance
- Promote user become manager, Demote user become employee