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

//functions

//processRequest(){}

//dataStore()

//removeOldEntries()

//syncLamport()



