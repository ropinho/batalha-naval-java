package cliente;

import java.net.Socket;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class Start {

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

  // configura o mapa do jogador posicionando os barcos na matriz
  public static void configurePlayerMap(){
    int x, y;
    System.out.println("Posicione as embarcações usando os índices X e Y.");
    while (player.getNumBarcos() < player.MAX_BARCOS){
      player.printTab();
			System.out.print("x: ");
			x = in.nextInt();
			System.out.print("y: ");
			y = in.nextInt();
			player.posicionarBarco(x, y);
			System.out.printf("Adicionado! (%d/%d)\n", player.getNumBarcos(), player.MAX_BARCOS);
    }
  }


  public static void sendPlayer(){
    try {
      ObjectOutputStream ostream = new ObjectOutputStream(cliente.getOutputStream());
      ostream.flush();
      ostream.writeObject(player);
      ostream.close();
    } catch(Exception e){
      System.out.println(e.toString());
    }
  }


  public static void main(String[] args) {
    in = new Scanner(System.in);

    createPlayer();
    configurePlayerMap();

    System.out.print("Digite o número da porta para conectar: ");
    int port = in.nextInt();

    try {
      ip = InetAddress.getLocalHost().getHostAddress();
      cliente = new Socket(ip, port);
      System.out.println("Conectado a porta "+ port);

      // enviar obj jogador para o servidor
      sendPlayer();

    } catch(Exception e) {
      System.out.println("erro: "+ e.toString());
    }

    System.out.println("Aguardando servidor...");


  }

}
