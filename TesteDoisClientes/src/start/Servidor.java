package start;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.util.Scanner;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

import game.*;

public class Servidor {

  static ServerSocket servidor;
  static Socket[] clientes = new Socket[2];
  static Jogador[] jogadores = new Jogador[2];
  static Batalha batalha;

  // streams de entrada e saída para objetos e dados primitivos
  static Scanner in = new Scanner(System.in);
  static ObjectInputStream[] istream = new ObjectInputStream[2];
  static ObjectOutputStream[] ostream = new ObjectOutputStream[2];
  static DataInputStream dis;
  static DataOutputStream dos;


  public static void print(String s){
    System.out.println(s);
  }

  public static void main(String[] args) {
    int n=0;

    try {
      // aguardar duas conexões
      servidor = new ServerSocket(8000);
      print("Aguardando conexões na porta 8000");
      for (n=0; n<2; n++){
        clientes[n] = servidor.accept();
        print("Host conectado: "+ clientes[n].getInetAddress().getHostName()+" ["+ clientes[n].getInetAddress().getHostAddress()+ "]");
      }
      print("-----------------------------------------");

      // receber jogadores dos clientes
      for (int i=0; i<2; i++){
        istream[i] = new ObjectInputStream(clientes[i].getInputStream());
        jogadores[i] = (Jogador) istream[i].readObject();
        print("Jogador "+(i+1)+": "+ jogadores[i].getNome());
        ostream[i] = new ObjectOutputStream(clientes[i].getOutputStream());
        ostream[i].writeInt(i); // envia o índice para o respectivo cliente para identificação
        ostream[i].flush();
      }
      print("-----------------------------------------");

      // criar objeto batalha e enviar para os clientes
      batalha = new Batalha(jogadores);
      for (int i=0; i<2; i++){
        ostream[i].writeObject(batalha);
        ostream[i].flush();
      }

      // iniciar a Batalha
      int i, iteration=0;
      int[] coord = new int[2];
      char alvo;
      String result = "";

      while (batalha.jogadoresPossuemTiros()) {
        i = iteration % 2;
        print((iteration+1)+ ": " + jogadores[i].getNome() +" ataca");
        // mandar um sinal para os clientes com o índice de quem atacante
        for (int k=0; k<2; k++){
          ostream[k].writeInt(i);
          ostream[k].writeObject(batalha);
          ostream[k].flush();
        }
        // esperar coordenadas do tiro
        coord = (int[]) istream[i].readObject();

        // efetuar ataque
        alvo = batalha.jogadorAtaca(i+1, coord[0], coord[1]);
        // verifica alvo/resultado do tiro
        if (alvo == 'B'){
          result = batalha.getBarcoDestruido();
        } else if (alvo == '~') {
          result = batalha.getTiroNaAgua();
        } else if (alvo=='X' && alvo=='*'){
          result = "Coordenadas já haviam sido atacadas";
        }

        iteration++;
      }

      // fechando streams e socket
      for (i=0; i<2; i++) {
        istream[i].close();
        ostream[i].close();
        clientes[i].close();
      }
    } catch(Exception e){
      System.out.println("Erro: "+ e.toString());
    }
  }
}
