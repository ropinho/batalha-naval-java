package servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

import game.*;

public class Start {

  static ServerSocket servidor;
  static Socket cliente;
  static Jogador jogador, oponente;

  // streams de entrada e saída para objetos e dados primitivos
  static Scanner in = new Scanner(System.in);
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
    System.out.println("Posicione as embarcações usando os índices X (Linha) e Y Coluna.");
    while (jogador.getNumBarcos() < jogador.MAX_BARCOS){
      jogador.printTab();
			System.out.print("Linha X: ");
			x = in.nextInt();
			System.out.print("Coluna Y: ");
			y = in.nextInt();
			jogador.posicionarBarco(x, y);
			System.out.printf("Adicionado! (%d/%d)\n", jogador.getNumBarcos(), jogador.MAX_BARCOS);
    }
    System.out.println("Meu mapa de embarcações:");
    jogador.printTab();
  }


  public static void main(String[] args) {

    createPlayer(); // criar o seu jogador
    configurePlayerMap(); // distribuir embarcações no mapa

    try {
      // aguardar uma conexão
      servidor = new ServerSocket(8000);
      print("Aguardando conexão na porta 8000");
      cliente = servidor.accept();
      print("Host "+ cliente.getInetAddress().getHostName()+" ("+ cliente.getInetAddress().getHostAddress()+ ") se conectou");

      // receber o jogador do cliente conectado
      print("Recebendo jogador adversário...");
      istream = new ObjectInputStream(cliente.getInputStream());
      oponente = (Jogador) istream.readObject();
      print("Você: "+ jogador.getNome());
      print("Adversário: "+ oponente.getNome());

      // criar partida
      Partida partida = new Partida(jogador, oponente);
      print("Partida criada!");

      // enviar partida para o adversario
      ostream = new ObjectOutputStream(cliente.getOutputStream());
      print("Enviando dados da batalha para o outro jogador...");
      ostream.writeObject(partida);



      istream.close();
      ostream.close();
    } catch(Exception e){
      System.out.println("Erro: "+ e.toString());
    }
  }
}
