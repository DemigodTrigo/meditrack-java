# MediTrack -- Healthcare Management System

MediTrack is a Java-based console application for managing doctors,
patients, appointments, and billing.

## Features

### Doctor Management

-   Add, view, update, and delete doctors
-   View all doctors
-   Search doctors by name
-   Search by specialization
-   Calculate average consultation fee
-   Find the highest-fee doctor

### Patient Management

-   Add, view, update, and delete patients
-   View all patients
-   Search patients by name, ID, phone, or email
-   Display patient count

### Appointment Management

-   Create, view, update, and delete appointments
-   View appointments by doctor, patient, or status
-   View upcoming appointments
-   Confirm and cancel appointments
-   Prevent appointments in the past
-   Prevent doctor and patient scheduling conflicts
-   Check doctor availability

### Billing Management

-   Create consultation and custom bills
-   View bills and bills by appointment
-   Process payments
-   Calculate tax and total amount
-   Generate immutable bill summaries
-   View unpaid bills
-   Calculate total revenue

## Project Structure

``` text
src/com/airtribe/meditrack/
├── entity/
│   ├── Person.java
│   ├── Doctor.java
│   ├── Patient.java
│   ├── Appointment.java
│   ├── Bill.java
│   ├── BillSummary.java
│   └── MedicalEntity.java
├── service/
│   ├── DoctorService.java
│   ├── PatientService.java
│   ├── AppointmentService.java
│   └── BillingService.java
├── util/
│   ├── Validator.java
│   ├── DateUtil.java
│   ├── CSVUtil.java
│   ├── IdGenerator.java
│   ├── DataStore.java
│   └── AIHelper.java
├── exception/
│   ├── AppointmentNotFoundException.java
│   ├── DoctorNotFoundException.java
│   ├── PatientNotFoundException.java
│   └── InvalidDataException.java
├── interfaces/
│   ├── Searchable.java
│   ├── Payable.java
│   └── NotificationListener.java
├── constants/
│   ├── Constants.java
│   ├── AppointmentStatus.java
│   ├── BillingType.java
│   └── Specialization.java
├── test/
│   ├── DoctorTest.java
│   ├── PatientTest.java
│   ├── AppointmentTestRunner.java
│   ├── BillingTest.java
│   └── TestRunner.java
└── Main.java
```

## Architecture

The application follows a simple layered design:

``` text
Main
 │
 ▼
Service Layer
 │
 ├── DoctorService
 ├── PatientService
 ├── AppointmentService
 └── BillingService
 │
 ├───────────────┐
 ▼               ▼
Entity Layer   Utility Layer
 │               │
 ▼               ▼
Doctor        DataStore<T>
Patient       Validator
Appointment   DateUtil
Bill          CSVUtil
              IdGenerator
```

### Responsibilities

-   **Entity** -- represents application data
-   **Service** -- contains business logic
-   **Utility** -- reusable helper functionality
-   **Exception** -- handles application-specific errors
-   **Interface** -- defines common behavior
-   **Constants** -- stores fixed application values
-   **Main** -- handles console interaction

## OOP Concepts Demonstrated

### Encapsulation

Entity fields are private and accessed through getters and setters.

### Inheritance

`Doctor` and `Patient` extend `Person`.

``` text
Person
├── Doctor
└── Patient
```

### Polymorphism

Common interfaces and inherited types allow objects to be used through
their parent/interface types.

### Abstraction

Abstract classes and interfaces are used to define common behavior.

### Interfaces

The project includes:

-   `Searchable<T>`
-   `Payable`
-   `NotificationListener`

### Generics

`DataStore<T>` is a generic in-memory repository used for multiple
entity types.

### Immutability

`BillSummary` is an immutable class with final fields and no setters.

### Enums

Enums are used for:

-   `AppointmentStatus`
-   `BillingType`
-   `Specialization`

## Billing

The application currently uses an 18% tax rate.

``` text
Tax   = Base Amount × 18%
Total = Base Amount + Tax
```

For a consultation fee of ₹800:

``` text
Base Amount = ₹800
Tax         = ₹144
Total       = ₹944
```

## Appointment Validation

Before an appointment is created, the system checks:

1.  Doctor exists
2.  Patient exists
3.  Date and time are valid
4.  Appointment is not in the past
5.  Doctor is available
6.  Doctor has no conflicting appointment
7.  Patient has no conflicting appointment

Example date/time format:

``` text
dd-MM-yyyy HH:mm

21-12-2026 12:12
```

## Running the Application

### Requirements

-   Java JDK 17 or compatible version
-   IntelliJ IDEA
-   Git

### IntelliJ IDEA

1.  Open the project in IntelliJ IDEA.
2.  Configure the Java SDK.
3.  Open `Main.java`.
4.  Run `Main`.
5.  Use the console menus to manage the system.

## Testing

The project contains simple Java test runners:

-   `DoctorTest`
-   `PatientTest`
-   `AppointmentTestRunner`
-   `BillingTest`
-   `TestRunner`

Run `TestRunner` to execute the available tests together.

The tests cover CRUD operations, searching, appointment conflicts,
confirmation/cancellation, billing, tax calculation, payments, revenue,
and exception handling.

## Example Main Menu

``` text
========================================
             MEDI TRACK
========================================

1. Doctor Management
2. Patient Management
3. Appointment Management
4. Billing Management
5. Dashboard
0. Exit
```

## Utilities

### DataStore`<T>`{=html}

A generic `HashMap`-based in-memory store supporting:

-   Save
-   Find by ID
-   Find all
-   Update
-   Delete
-   Existence check
-   Count

### Validator

Provides reusable validation such as:

-   Null checks
-   Positive number checks
-   Non-blank text checks
-   Age validation

### DateUtil

Handles parsing, formatting, and date checks using Java's
`LocalDateTime`.

### CSVUtil

Provides basic CSV file operations:

-   Read
-   Write
-   Append
-   Check existence
-   Delete

### IdGenerator

Generates IDs for doctors, patients, appointments, and bills.

## Exceptions

Custom exceptions include:

``` text
AppointmentNotFoundException
DoctorNotFoundException
PatientNotFoundException
InvalidDataException
```

The console application catches operation failures and displays readable
error messages.

## Technologies

-   Java
-   Object-Oriented Programming
-   Collections Framework
-   Generics
-   Java Streams
-   Java Time API
-   File I/O
-   Git
-   GitHub
-   IntelliJ IDEA

## Future Enhancements

Possible extensions include:

-   Database persistence
-   CSV persistence integration
-   Login and role-based access
-   Doctor/patient accounts
-   Appointment notifications
-   Email/SMS integration
-   Prescription management
-   Medical reports
-   Advanced reporting
-   AI-assisted recommendations
-   REST API with Spring Boot
-   Web or mobile frontend
-   JUnit test suite
-   Additional design patterns

## Project Status

MediTrack currently contains functional modules for:

-   Doctor management
-   Patient management
-   Appointment management
-   Billing management
-   Payment processing
-   Search
-   Validation
-   Exception handling
-   Generic data storage
-   Service-level test runners

## Author

**MediTrack Java Project**

A Java OOP / Low-Level Design project demonstrating practical software
design, business logic, and reusable Java components.
