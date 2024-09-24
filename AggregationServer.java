import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
public class AggregationServer {
    private Map<String, WeatherData> dataStore;
    private LamportClock lamportClock;
    private int port;
}

public AggregationServer(int port){
    this.port=port;
    this.dataStore=new HashMap<>();
    this.lamportClock=new LamportClock();
}

public static void main(String[] args) {
    int port;
    AggregationServer agServer = new AggregationServer(port)
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

private class RequestHandler implements Runnable {
    private Socket clientSocket;
    public RequestHandler(Socket socket){
        this.clientSocket=socket;
    }


}
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

}

private void handePUT(BufferedReader in, PrintWriter out){
    
}
//functions

//processRequest(){}

//dataStore()

//removeOldEntries()

//syncLamport()



