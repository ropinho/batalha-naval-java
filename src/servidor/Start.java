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
  static Socket[] cliente = new Socket[2];
  static Jogador[] player = new Jogador[2];
  static int n = 0; // n conta num de clientes conectados

  // streams de entrada e saída para objetos e dados primitivos
  static ObjectInputStream istream;
  static ObjectOutputStream ostream;
  static DataInputStream dis;
  static DataOutputStream dos;


  public static Jogador getPlayerFromClient(Socket socket){
    try {
      istream = new ObjectInputStream(socket.getInputStream());
      Jogador j = (Jogador) istream.readObject();
      istream.close();
      return j;
    } catch(Exception e) {
      System.out.println(e.toString());
    }
    return null;
  }

  public static void sendPlayersData(){
    try {
      for (Socket skt : cliente){
        ostream = new ObjectOutputStream(skt.getOutputStream());
        ostream.writeObject(player);
        ostream.close();
      }
    } catch(Exception e){
      e.getMessage();
    }
  }


  public static void main(String[] args) {
    //teclado = new Scanner(System.in);

    try {
      // aguarda conexões com clientes na porta 8000
      servidor = new ServerSocket(8000);
      System.out.println("Aguardando conexões na porta 8000");

      // loop aguarda por duas conexões clientes
      // encerra quando dois clientes conectam-se
      while (n < 2){
        cliente[n] = servidor.accept();
        System.out.print("Cliente conectado: "+ cliente[n].getInetAddress().getHostName() + "  " + cliente[n].getInetAddress().getHostAddress());
        player[n] = getPlayerFromClient(cliente[n]); // ler obj jogador
        System.out.printf("  (%s)\n", player[n].getNome());
        n++;
      }

      System.out.println("Iniciando batalha...");
      Partida partida = new Partida(player[0], player[1]);

      while (true) {
        Jogador atk, def, aux;


        if (player[0].getTiros() > 0 && player[1].getTiros() > 0) break;
      }

      cliente[0].close();
      cliente[1].close();
    } catch(Exception e){
      System.out.println("erro: "+ e.toString());
    }
  }
}
