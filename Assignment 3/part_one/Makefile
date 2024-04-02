# Compiler
JAVAC = javac
# Java Virtual Machine
JAVA = java
# Main class for SimulatorOne
MAIN_CLASS = SimulatorOne

# Target: compile Graph.java
Graph.class: Graph.java
    $(JAVAC) Graph.java

# Target: compile SimulatorOne.java and depend on Graph.class
SimulatorOne.class: SimulatorOne.java Graph.class
    $(JAVAC) SimulatorOne.java

# Target: run the SimulatorOne class
run: SimulatorOne.class
    $(JAVA) $(MAIN_CLASS)

# Target: clean compiled classes
clean:
    rm -f *.class
