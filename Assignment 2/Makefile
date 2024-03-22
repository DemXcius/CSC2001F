JC = javac
JVM = java
MAIN_AVL = GenericsKbAVLApp
MAIN_EXP = AVLExperiment
MAIN_VISUAL = AVLVisual

# Flags
JFLAGS = -d bin -sourcepath src

# Source files
SOURCES := $(wildcard src/*.java)

# Classes
CLASSES := $(SOURCES:src/%.java=bin/%.class)

# Default target
all: $(CLASSES)

# Compile Java source files
bin/%.class: src/%.java | bin
	$(JC) $(JFLAGS) $<

bin:
	mkdir -p bin

# Generate JavaDoc
docs:
	javadoc -d doc src/*.java

# Run AVL application
run_avl: all
	$(JVM) -classpath bin $(MAIN_AVL)

# Run experiment
run_exp: all
	$(JVM) -classpath bin $(MAIN_EXP)

#Run Visual
run_visual: all
	$(JVM) -classpath bin $(MAIN_VISUAL)

# Clean
clean:
	rm -rf bin

.PHONY: all run_avl run_exp clean