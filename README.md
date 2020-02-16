
# RapidCodeRunner
Tool used to test java code snippets.

![](https://i.imgur.com/HS0q5pL.gif)

## Requirements
* Java 8 minimum for runtime
* The JAVA_HOME environment variable needs to be set

## Features
* You can write code snippet and compile it
* Then it shows the compilation and execution outputs

## Possible evolutions
* choose different JDK versions
* syntax highlighting
* add line numbers
* user settings and preferences
* other programming languages

## Changelog
### 0.3.0-SNAPSHOT
* Print error message if javac command failed to execute
* Use of the JAVA_HOME environment variable
* Added some unit tests
* Moved classes to sub packages
* Refactoring code

### 0.2.2-SNAPSHOT
* Manage compilation errors
* Print compilation/execution time

### 0.2.1-SNAPSHOT
* GUI improvments (JFX CSS) [GUI preview](https://i.imgur.com/HS0q5pL.gif)

### 0.2.0-SNAPSHOT
* minimalist GUI (JavaFX) [GUI preview](https://i.imgur.com/6kHa3MH.gif)

### 0.1.2-SNAPSHOT
* separate execution status (compile and running via 2 StringBuilders)

### 0.1.1-SNAPSHOT
* retrieve the execution status via StringBuilder

### 0.1.0-SNAPSHOT
* initial commit (no gui)
* java runner mecanism
