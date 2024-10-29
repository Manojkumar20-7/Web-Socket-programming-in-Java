import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static Set<ClientHandler> clients=new HashSet<>();
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(4321)){
            System.out.println("Connected to ChatServer...");

            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("New Client Connented: "+socket);
                ClientHandler clientHandler= new ClientHandler(socket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static synchronized void broadcast(String message, ClientHandler sender){
        for(ClientHandler client:clients){
            if(client!=sender){
                client.sendMessage(message);
            }
        }
    }
    public static synchronized void removeClient(ClientHandler client){
        clients.remove(client);
    }
}

class ClientHandler implements Runnable{
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ClientHandler(Socket socket){
        this.socket=socket;
    }

    public void run(){
        try{
            while(true){
                out=new PrintWriter(socket.getOutputStream(),true);
                in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String clientMessage;
                while((clientMessage=in.readLine())!=null){
                    ChatServer.broadcast(clientMessage, this);
                    System.out.println("Received: "+clientMessage);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                socket.close();
                System.out.println("Client disconnected: "+socket);

            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message){
        out.println(message);
    }
}