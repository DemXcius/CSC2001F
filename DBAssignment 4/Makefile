# Makefile for Java project

# Compiler and compiler flags
JC = javac
JFLAGS = -g -d bin -cp lib/*:src

# Source and class file directories
SRC_DIR = src
BIN_DIR = bin

# Source files
SOURCES := $(wildcard $(SRC_DIR)/*.java)

# Class files
CLASSES := $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# Default target
default: classes

# Compile Java source files
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	$(JC) $(JFLAGS) $<

# Target to compile all source files
classes: $(CLASSES)

# Target to clean compiled files
clean:
	$(RM) $(BIN_DIR)/*.class

# Run AutoTest.java
run-AutoTest:
	java -cp bin AutoTest

# Run LPAutoTest.java
run-LPAutoTest:
	java -cp bin LPAutoTest

# Run QPAutoTest.java
run-QPAutoTest:
	java -cp bin QPAutoTest

# Run Optimise.java
run-Optimise:
	java -cp bin Optimise
