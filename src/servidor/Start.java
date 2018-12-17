package servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Start {

  static ServerSocket servidor;
  static Socket[] cliente = new Socket[2];
  static Jogador[] player = new Jogador[2];
  static int n = 0;
  //static int sizeX, sizeY;
  static Scanner teclado, in_client1, in_client2;


  public static Jogador getPlayerFromClient(Socket c){
    try {
      ObjectInputStream istream = new ObjectInputStream(c.getInputStream());
      Jogador j = (Jogador) istream.readObject();
      istream.close();
      return j;
    } catch(Exception e) {
      System.out.println(e.toString());
    }
    return null;
  }


  public static void main(String[] args) {
    teclado = new Scanner(System.in);

    /*
    System.out.println("Configure o servidor.");
    System.out.print("Largura do mapa: ");
    int sizeX = teclado.nextInt();
    System.out.print("Altura do mapa: ");
    int sizeY = teclado.nextInt();
    */

    try {
      // aguarda conexões com clientes na porta 8000
      servidor = new ServerSocket(8000);
      System.out.println("Aguardando conexões na porta 8000");

      while (n < 2){
        cliente[n] = servidor.accept();
        System.out.println("Cliente conectado: "+ cliente[n].getInetAddress().getHostName() + "  " + cliente[n].getInetAddress().getHostAddress());
        player[n] = getPlayerFromClient(cliente[n]); // ler obj jogador
        n++;
      }



    } catch(Exception e){
      System.out.println("erro: "+ e.toString());
    }
  }
}
