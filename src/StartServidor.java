import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.Scanner;

public class StartServidor {

  static ServerSocket servidor;
  static Socket[] cliente = new Socket[2];
  static int num_clientes = 0;
  static int sizeX;
  static int sizeY;
  static Scanner in;


  public static void main(String[] args) {
    in = new Scanner(System.in);

    System.out.println("Configure o servidor.");
    System.out.print("Largura do mapa: ");
    int sizeX = in.nextInt();
    System.out.print("Altura do mapa: ");
    int sizeY = in.nextInt();

    try {
      // aguarda conexões com clientes na porta 8000
      servidor = new ServerSocket(8000);
      System.out.println("Aguardando conexões na porta 8000");
      while (true) { // lopp infinito
        while (num_clientes < 2){
          cliente[num_clientes] = servidor.accept();
          System.out.println("Cliente conectado: "+ cliente[num_clientes].getInetAddress().getHostAddress());
          num_clientes++;
        }
      }
    } catch(Exception e){
      System.out.println("erro: "+ e.toString());
    }

  }

}
