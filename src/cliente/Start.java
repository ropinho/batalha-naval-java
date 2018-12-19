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
  static Jogador jogador, oponente;
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
      print("Aguardando batalha...");
      istream = new ObjectInputStream(socket.getInputStream());
      partida = (Partida) istream.readObject();
      print("Batalha recebida");
      oponente = partida.getJogador1();
      partida.printNomesJogadores();

      boolean finish = false;
      int turno; // var para verificar de quem é a vez de atacar
      int x, y;  // coordenadas do ataque
      int[] coord = new int[2];
      String log;

      print("==============================:");
      print(" Iniciando a Batalha");
      print("==============================:");
      partida.printClientTab();

      while (!finish) {
        turno = (int) istream.readInt();

        if (turno > 0){ // sua vez de atacar quando o turno receber o valor 1 do servidor
          print("Sua vez de atacar. Digite as coordenadas X e Y:");
          /* recebe os valores X e Y e em seguida envia para o servidor */
          System.out.print("x: ");
          coord[0] = in.nextInt(); // coordenada x
          System.out.print("y: ");
          coord[1] = in.nextInt(); // coordenada y
          ostream.writeObject(coord);
          print(jogador.getNome()+" ataca nas coordenadas ("+coord[0]+","+coord[1]+")");

        } else { // vez do servidor, esperar...
          print("Vez de "+ oponente.getNome() +". Esperando...");
          log = (String) istream.readObject();
          print(log);

        }
        // recebe atualização do estado da partida
        partida = (Partida) istream.readObject();
        jogador  = partida.getJogador2();
        oponente = partida.getJogador1();
        partida.printClientTab();

      }// end of while

      ostream.close();
      istream.close();
      socket.close();
    } catch(Exception e) {
      System.err.println("Erro: "+ e.toString());
    }

  }

}
