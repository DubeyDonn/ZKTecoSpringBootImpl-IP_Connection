# ZKTeco Device Connection Application

This is a Spring Boot application designed to connect to any ZKTeco device using its IP address. 

## Description

The application allows you to set the IP address, port, and COM key manually in the code for connection. The database connection is also manually configured in the `TableManager` file.

The application provides several APIs:

- `/api/connect`: Connects to the ZKTeco device.
- `/api/disconnect`: Disconnects from the ZKTeco device.
- `/api/createBackup`: Creates a backup of the attendance record in a `.json` file.
- `/api/sync`: Saves all the attendance records and registered users to the database.

The database should have tables for `attendance_record` and `user`. A sample database is provided with the application.

## Setup

To run this application, you need to:

1. Set the IP address, port, and COM key in the code.
2. Configure the database connection in the `TableManager` file.
3. Ensure that the database has `attendance_record` and `user` tables.

## Usage

After setting up, you can use the provided APIs to connect to the ZKTeco device, disconnect, create a backup of the attendance record, and sync the attendance records and registered users with the database.
