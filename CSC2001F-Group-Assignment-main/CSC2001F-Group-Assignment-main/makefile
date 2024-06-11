# Define the target for compiling Java files
bin: $(wildcard src/*.java) $(wildcard src/**/*.java) $(wildcard src/**/**/*.java)
	javac -d bin -cp "lib/sqlite-jdbc-3.45.3.0.jar:lib/slf4j-api-1.7.36.jar" $^

# Run Array App
run:
	java -cp "lib/sqlite-jdbc-3.45.3.0.jar:lib/slf4j-api-1.7.36.jar:bin" main.Main

# Generate the docs
docs: $(wildcard src/*.java) $(wildcard src/**/*.java) $(wildcard src/**/**/*.java)
	javadoc -d doc -cp "lib/sqlite-jdbc-3.45.3.0.jar:lib/slf4j-api-1.7.36.jar" $^

# Clean up compiled class files
clean:
	rm -rf bin
	rm -rf doc