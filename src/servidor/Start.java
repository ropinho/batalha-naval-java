package servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class StartServidor {

  static ServerSocket servidor;
  static Socket[] cliente = new Socket[2];
  static int num_clientes = 0;
  static int sizeX;
  static int sizeY;
  static Scanner teclado, in_client1, in_client2;
  static Jogador jog1, jog2;


  public static void getPlayersFromClients(){
    
  }


  public static void main(String[] args) {
    teclado = new Scanner(System.in);

    System.out.println("Configure o servidor.");
    System.out.print("Largura do mapa: ");
    int sizeX = teclado.nextInt();
    System.out.print("Altura do mapa: ");
    int sizeY = teclado.nextInt();

    try {
      // aguarda conexões com clientes na porta 8000
      servidor = new ServerSocket(8000);
      System.out.println("Aguardando conexões na porta 8000");

      while (num_clientes < 2){
        cliente[num_clientes] = servidor.accept();
        System.out.println("Cliente conectado: "+ cliente[num_clientes].getInetAddress().getHostAddress());
        num_clientes++;
      }



    } catch(Exception e){
      System.out.println("erro: "+ e.toString());
    }
  }
}
