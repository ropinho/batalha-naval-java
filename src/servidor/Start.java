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
      print("Jogadores:");
      print("Você: "+ jogador.getNome());
      print("Adversário: "+ oponente.getNome());

      // criar partida
      Partida partida = new Partida(jogador, oponente);
      partida.printNomesJogadores();

      // enviar partida para o adversario
      ostream = new ObjectOutputStream(cliente.getOutputStream());
      ostream.writeObject(partida);

      print("==============================:");
      print(" Iniciando a Batalha");
      print("==============================:");

      int iteration=1, x, y;
      int[] coord = new int[2];
      Jogador atk, def;
      char alvo;
      String result="";
      partida.printServerTab();

      /* Loop da batalha */
      while (iteration<10) { //--------------------------------------------------------------//
        // definindo quem ataca e quem defende e enviando um sinal para o cliente
        if (iteration%2 == 1){ // se a iteração for ímpar, o servidor ataca
          // se for a vez do servidor atacar, envia 0 para o cliente
          ostream.writeInt(0);
          ostream.flush();
          print("Sua vez de atacar. Digite as coordenadas X e Y:");
          System.out.print("x: ");
          x = in.nextInt();
          System.out.print("y: ");
          y = in.nextInt();
          alvo = partida.jogadorAtaca(1, x, y);

          // definindo o log para o devido alvo atacado
          if (alvo == 'B'){
            result = partida.getBarcoDestruido();
          } else if (alvo == '~') {
            result = partida.getTiroNaAgua();
          } else if (alvo=='X' && alvo=='*'){
            result = "Coordenadas já haviam sido atacadas";
          }

          print(jogador.getNome()+" ataca nas coordenadas ("+ x +","+ y +")\n"+ result);
          ostream.writeObject(jogador.getNome()+" ataca nas coordenadas ("+ x +","+ y +")\n"+ result); // envia log pro cliente
          // envia atualização da partida para o lado cliente
          ostream.writeObject(partida);
          ostream.flush();
          partida.printServerTab();
          iteration++;

        } else { //-----------------------------------------------------------------------------//
          // se a iteração for par, o cliente ataca e o servidor recebe as coordenadas do ataque
          // envia o valor 1 para o cliente para sinalizar a vez dele de atacar
          ostream.writeInt(1);
          ostream.flush();
          print("Vez de "+ oponente.getNome() +". Esperando...");
          // recebe as coordenadas do atque do cliente
          coord = (int[]) istream.readObject();
          partida.jogadorAtaca(2, coord[0], coord[1]);
          print(oponente.getNome()+" ataca nas coordenadas ("+coord[0]+","+coord[1]+")");

          ostream.writeObject(partida);
          ostream.flush();
          partida.printServerTab();
          iteration++;
        }
      }


      istream.close();
      ostream.close();
    } catch(Exception e){
      System.out.println("Erro: "+ e.toString());
    }
  }
}
