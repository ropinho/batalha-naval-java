package cliente;

import java.net.Socket;
import java.net.InetAddress;

public class StartCliente {

  static Socket cliente;
  static String ip;
  static Scanner in;
  static Jogador player;

  // cria o jogador para o jogo
  public static void createPlayer(){
    System.out.print("Digite o seu nome para o jogo: ");
    String nome = in.next();
    player = new Jogador(nome);
  }


  public static void configurePlayerMap(){

  }


  public static void main(String[] args) {
    in = new Scanner(System.in)

    try {
      ip = InetAddress.getLocalHost().getHostAddress();
      int port = 8000;

      cliente = new Socket(ip, port);
      System.out.println("Conectado a porta "+ port);

    } catch(Exception e) {
      System.out.println("erro: "+ e.toString());
    }

  }

}
