# Compiler
JC = javac

# Flags
JFLAGS = -d bin -sourcepath src

# Source files
SOURCES := $(wildcard src/*.java)

# Classes
CLASSES := $(patsubst src/%.java, bin/%.class, $(SOURCES))

# Default target
all: $(CLASSES)

# Compile Java source files
bin/%.class: src/%.java
	mkdir -p bin
	$(JC) $(JFLAGS) $<

# Generate JavaDoc
docs:
	javadoc -d doc src/*.java

# Clean
clean:
	rm -rf bin/*.class

.PHONY: all docs clean
