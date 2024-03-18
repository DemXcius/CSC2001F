JC = javac
JVM = java
MAIN = GenericsKbAVLApp

# Flags
JFLAGS = -d bin

# Source files
SOURCES := $(wildcard src/*.java)

# Classes
CLASSES := $(patsubst src/%.java, bin/%.class, $(SOURCES))

# Default target
all: $(CLASSES)

# Compile Java source files
bin/%.class: src/%.java
	$(JC) $(JFLAGS) $<

# Run
run: all
	$(JVM) -classpath bin $(MAIN)

# Clean
clean:
	rm -rf bin/*

.PHONY: all run clean