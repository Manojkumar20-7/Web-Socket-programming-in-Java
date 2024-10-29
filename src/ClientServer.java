import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientServer {
    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost",4321)){
            System.out.println("Connected to ChatServer...");

            new Thread(new IncomingMessageHandler(socket)).start();

            PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            String sendingMessage;
            while((sendingMessage=in.readLine())!=null){
                out.println(sendingMessage);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

class IncomingMessageHandler implements Runnable{
    
    private Socket socket;

    public IncomingMessageHandler(Socket socket){
        this.socket=socket;
    }
    
    public void run(){
        try{
            BufferedReader reader= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;
            while((message=reader.readLine())!=null){
                System.out.println("Received : "+message);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
