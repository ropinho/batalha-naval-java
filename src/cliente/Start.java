package cliente;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

import game.*;

public class Start {

  static Socket socket;
  static String ip;
  static Scanner in;
  static Jogador jogador;
  static Partida partida;

  static ObjectInputStream istream;
  static ObjectOutputStream ostream;
  static DataInputStream dis;
  static DataOutputStream dos;


  public static void print(String s){
    System.out.println(s);
  }

  // cria o jogador para o jogo
  public static void createPlayer(){
    System.out.print("Digite o seu nome para o jogo: ");
    String nome = in.next();
    jogador = new Jogador(nome);
    System.out.println("Jogador criado!");
  }

  // configura o mapa do jogador posicionando os barcos na matriz
  public static void configurePlayerMap(){
    int x, y;
    System.out.println("Posicione as embarcações usando os índices X e Y.");
    while (jogador.getNumBarcos() < jogador.MAX_BARCOS){
      jogador.printTab();
			System.out.print("x: ");
			x = in.nextInt();
			System.out.print("y: ");
			y = in.nextInt();
			jogador.posicionarBarco(x, y);
			System.out.printf("Adicionado! (%d/%d)\n", jogador.getNumBarcos(), jogador.MAX_BARCOS);
    }
    System.out.println("Meu mapa de embarcações:");
    jogador.printTab();
  }



  public static void main(String[] args) {
    in = new Scanner(System.in);

    // criar o seu jogador
    createPlayer();
    // distribuir embarcações no mapa
    configurePlayerMap();

    // conectar: digite a porta em que deseja se conectar
    System.out.print("Digite o número da porta para conectar: ");
    int port = in.nextInt();
    try {
      ip = InetAddress.getLocalHost().getHostAddress();
      socket = new Socket(ip, port);
      System.out.printf("Conectado a porta %d, IP: %s\n", port, socket.getInetAddress().getHostAddress());

      // enviar o jogador para o servidor
      ostream = new ObjectOutputStream(socket.getOutputStream());
      print("Enviando meu jogador para o servidor...");
      ostream.writeObject(jogador);
      ostream.flush();

      // receber o obj batalha
      print("aguardando batalha...");
      istream = new ObjectInputStream(socket.getInputStream());
      print("(...)");
      partida = (Partida) istream.readObject();
      print("(...)");


      ostream.close();
      istream.close();
      socket.close();
    } catch(Exception e) {
      System.err.println("Erro: "+ e.toString());
    }

  }

}
