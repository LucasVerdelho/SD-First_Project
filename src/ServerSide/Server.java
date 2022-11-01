package ServerSide;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException{
        
        ServerSocket ss = new ServerSocket(8888);
        
        Registos r = new Registos();
        
        //Popular os registos
        MultiObjectSerialization m = new MultiObjectSerialization();
        m.writeall(r);


        while(true){
            
            Socket socket = ss.accept();
            System.out.println("Conecção aceite do port " + socket.getPort());
            Thread slave = new Thread(new ServerSlave(socket,r));
            slave.start();
            
        }


    }
}
