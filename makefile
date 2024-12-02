JAVAC = javac
JAVA = java
JFLAGS = -d target/classes -cp target/classes
SRC_DIR = src/main/java
BIN_DIR = target\classes
MAIN_SERVER = AggregationServer
MAIN_CONTENT = ContentServer
MAIN_CLIENT = GETClient
DATA_FILE = src\main\weather.txt

# Compile all Java files
compile:
	@echo "Compiling Java source files..."
	@if not exist $(BIN_DIR) then mkdir $(BIN_DIR)
	$(JAVAC) $(JFLAGS) $(SRC_DIR)\*.java

# Run the Aggregation Server (default port 4567)
run-server: compile
	@echo "Running Aggregation Server..."
	$(JAVA) -cp $(BIN_DIR) $(MAIN_SERVER) 4567

# Run the Content Server
run-content: compile
	@echo "Running Content Server..."
	$(JAVA) -cp $(BIN_DIR) $(MAIN_CONTENT) http://localhost:4567 $(DATA_FILE)

# Run the GET Client to fetch data
run-client: compile
	@echo "Running GET Client..."
	$(JAVA) -cp $(BIN_DIR) $(MAIN_CLIENT) http://localhost:4567

# Clean up compiled files
clean:
	@echo "Cleaning up..."
	@if exist $(BIN_DIR) rd /s /q $(BIN_DIR)

.PHONY: compile run-server run-content run-client clean