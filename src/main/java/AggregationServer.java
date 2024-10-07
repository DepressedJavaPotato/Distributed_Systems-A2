import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
public class AggregationServer {
    private Database database;
    private LamportClock lamportClock;
    private int port;


public AggregationServer(int port){
    this.port=port;
    this.database=new Database();
    this.lamportClock=new LamportClock();
}

public static void main(String[] args) {
    int port=(args.length > 0) ? Integer.parseInt(args[0]) : 4567;
    AggregationServer agServer = new AggregationServer(port);
    agServer.start();
}

public void start(){
    try (ServerSocket serverSocket = new ServerSocket(port)){
        System.out.println("Aggregation server started on port: "+port);
        while (true) { 
            Socket clientSocket=serverSocket.accept();
            new Thread(new RequestHandler(clientSocket)).start();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public Database getDatabase() {
    return database;
}

private class RequestHandler implements Runnable {
    private Socket clientSocket;
    private ObjectMapper objectMapper;
    public RequestHandler(Socket socket){
        this.clientSocket=socket;
        this.objectMapper= new ObjectMapper();
    }


@Override
public void run(){
    try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
    ){
        String request = in.readLine();
        if(request.startsWith("GET")) {
            handleGET(out);
        }
        else if (request.startsWith("PUT")){
            handlePUT(in, out);
        
        }
        else {
            out.println("HTTP/1.1 400 Bad Request");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void handleGET(PrintWriter out){
    //synchronize lamport clock statement
    lamportClock.incrementClock();

    try {
        String jsonResponse = objectMapper.writeValueAsString(database.fetchData());
    out.println("HTTP/1.1 200 OK");
    out.println("Content-type: application/json");
    out.println("Content-Length: " + jsonResponse.length());
    out.println();
    out.println(jsonResponse);
    } 
    catch (JsonProcessingException e) {
        out.println("HTTP/1.1 500 Internal Server Error");
        out.println("Content-Length: 0");
        out.println();
        System.err.println("Error converting data store to JSON: " + e.getMessage());
    }
    

}

private void handlePUT(BufferedReader in, PrintWriter out){
    //sync lamport clock statement
    lamportClock.incrementClock();
    try {
        StringBuilder jsonBody = new StringBuilder();
        String line;
        while (!(line = in.readLine()).isEmpty()){
            jsonBody.append(line).append("\n");
        }
       
        if (jsonBody.length()>0){
             //weather data file data
        WeatherData data = objectMapper.readValue(jsonBody.toString(), WeatherData.class);
        if (data != null){
            database.storeData(data.getId(), data);
            out.println("HTTP/1.1 200 OK");
        }
        else{
            out.println("HTTP/1.1 500 Internal Server Error");
        }
    }
    else{
        out.println("HTTP/1.1 204 No Content");
    }

    } catch (Exception e) {
        e.printStackTrace();
        out.println("HTTP/1.1 500 Internal Server Error");
    }
}
}
}