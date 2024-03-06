# Java compiler
JC = javac

# Java compiler flags
JFLAGS = -g

# Directory containing the source files
SRC_DIR = src

# Directory to place the compiled .class files
BIN_DIR = bin

# List of Java source files
SRCS = $(wildcard $(SRC_DIR)/*.java)

# List of compiled .class files
CLASSES = $(SRCS:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

# Default target
all: $(CLASSES)

# Compile Java source files
$(BIN_DIR)/%.class: $(SRC_DIR)/%.java
	$(JC) $(JFLAGS) -d $(BIN_DIR) $<

# Clean the compiled files
clean:
	rm -f $(BIN_DIR)/*.class