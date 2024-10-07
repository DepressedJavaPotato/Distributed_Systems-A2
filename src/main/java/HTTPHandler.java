import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HTTPHandler implements Runnable {
    private final Socket clientSocket;
    private final AggregationServer server;
    private final ObjectMapper objectMapper;

    public HTTPHandler(Socket clientSocket, AggregationServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true)) {

            String requestLine = in.readLine();
            if (requestLine != null) {
                System.out.println("Received request: " + requestLine);

                if (requestLine.startsWith("GET")) {
                    handleGET(out);
                } else if (requestLine.startsWith("PUT")) {
                    handlePUT(in, out);
                } else {
                    sendBadRequest(out);
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling client request: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    //handle GET requests
    private void handleGET(PrintWriter out) {
        try {
            String jsonResponse = objectMapper.writeValueAsString(server.getDatabase().fetchData());
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: application/json");
            out.println("Content-Length: " + jsonResponse.length());
            out.println();
            out.println(jsonResponse);
            System.out.println("GET request handled successfully.");
        }
          catch (IOException e){
            sendInternalServerError(out);
        }
    }

    //handle PUT requests
    private void handlePUT(BufferedReader in, PrintWriter out) {
        try {
            StringBuilder requestBody = new StringBuilder();
            String line;
            while (!(line = in.readLine()).isEmpty()) {
                requestBody.append(line).append("\n");
            }

            //parse the incoming JSON to a WeatherData object

            StringBuilder jsonBody = new StringBuilder();
            while (in.ready() && (line = in.readLine()) != null) {
                jsonBody.append(line);
            }
            WeatherData weatherData = objectMapper.readValue(jsonBody.toString(), WeatherData.class);
            if (weatherData!=null){
                //store data
            server.getDatabase().storeData(weatherData.getId(), weatherData);

            //200 OK 
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Length: 0");
            out.println();
            System.out.println("PUT request handled successfully, data stored.");
            }
            else {
                out.println("HTTP/1.1 400 Bad Request");
            }
            

        } catch (IOException e) {
            sendInternalServerError(out);
        }
    }

    //400 Bad Request response
    private void sendBadRequest(PrintWriter out) {
        out.println("HTTP/1.1 400 Bad Request");
        out.println("Content-Length: 0");
        out.println();
        System.out.println("Bad request sent to client.");
    }

    //500 Internal Server Error response
    private void sendInternalServerError(PrintWriter out) {
        out.println("HTTP/1.1 500 Internal Server Error");
        out.println("Content-Length: 0");
        out.println();
        System.out.println("Internal server error response sent to client.");
    }
}
