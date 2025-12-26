# Quiz Application

This is a Java Swing-based application developed using the MVC architecture. It is a code smell detector tool designed to help identify common code smells in Java projects.

## Features

- Detects common code smells such as Long Method, Large Class, and God Object.
- Provides a user-friendly interface for analyzing Java source files.
- Displays results in a clear and organized manner.

## Prerequisites

- **Java 17** or higher
- **Maven** for build and dependency management

## Getting Started

### Building the Project

To build the project, run the following command in the project root directory:

```sh
mvn clean install
```

### Running the Application

You can run the application directly using the Java extension pack in Visual Studio Code:

1. Open the `Main.java` file located in the `src/main/java/com/scd/quizapp` directory.
2. Use the "Run" button provided by the extension to start the application.

Alternatively, you can use the following command in the terminal:

```sh
java -cp target/codeSmellDetector-1.0-SNAPSHOT.jar com.scd.codeSmellDetector.Main
```

## Project Structure

```
quiz-app
├── .gitignore
├── pom.xml
├── README.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── scd
│   │   │           ├── codeSmellDetector
│   │   │           │   ├── controller
│   │   │           │   ├── model
│   │   │           │   └── view
│   │   │           └── Main.java
│   └── test
│       └── java
└── target
```
