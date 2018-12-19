package start;

import java.net.Socket;
import java.net.SocketAddress;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

import game.*;

public class Cliente {

  static Socket servidor;
  static String ip;
  static Scanner in;
  static Jogador jogador, oponente;
  static Batalha batalha;

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

    createPlayer(); // criar o seu jogador
    configurePlayerMap(); // distribuir embarcações no mapa

    // conectar: digite a porta em que deseja se conectar
    System.out.print("Digite o número da porta para conectar: ");
    int port = in.nextInt();
    try {
      ip = InetAddress.getLocalHost().getHostAddress();
      servidor = new Socket(ip, port);
      print("Conectado a porta "+ port);

      // enviar o obj jogador para o servidor
      ostream = new ObjectOutputStream(servidor.getOutputStream());
      ostream.writeObject(jogador); // escreve o objeto na stream
      ostream.flush();
      // recebe índice do jogador no servidor
      istream = new ObjectInputStream(servidor.getInputStream());
      int myIndex = istream.readInt();
      print("Você é o jogador "+ (myIndex+1) +". [Índice = "+ myIndex +"]");

      // receber batalha criada pelo servidor
      Batalha batalha = (Batalha) istream.readObject();
      print("Objeto Batalha recebido");

      // iniciar a Batalha
      print("==============================:");
      print(" Iniciando a Batalha");
      print("==============================:");
      int sinal, x, y;
      int[] coord = new int[2];

      while (batalha.jogadoresPossuemTiros()) {
        // receber o sinal para definir quem ataca
        sinal = (int) istream.readInt();
        batalha = (Batalha) istream.readObject();
        batalha.showTabsForPlayer(myIndex); // mostrar os dois tabuleiros

        if (sinal == myIndex){
          print("Sua vez. Digite as coordenadas X e Y do tiro.");
          /* recebe os valores X e Y do teclado e em seguida envia para o servidor */
          System.out.print("x: ");
          coord[0] = in.nextInt(); // coordenada x
          System.out.print("y: ");
          coord[1] = in.nextInt(); // coordenada y
          ostream.writeObject(coord); // envia
        } else {
          print("Vez de "+ batalha.getJogador(sinal).getNome() +". Espere...");
        }
      }

      // fechando streams e socket
      istream.close();
      ostream.close();
      servidor.close();
    } catch(Exception e) {
      System.err.println("Erro: "+ e.toString());
    }

  }

}
