package start;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.Inet4Address;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

import game.*;

public class Servidor {

  static ServerSocket servidor;
  static Socket[] clientes = new Socket[2];
  static Jogador[] jogadores = new Jogador[2];
  static Tabuleiro[] tabs = new Tabuleiro[2];

  // streams de entrada e saída para objetos e dados primitivos
  static Scanner in = new Scanner(System.in);
  static ObjectInputStream[] istream = new ObjectInputStream[2];
  static ObjectOutputStream[] ostream = new ObjectOutputStream[2];
  static DataInputStream[] dis = new DataInputStream[2];
  static DataOutputStream[] dos = new DataOutputStream[2];

  // constantes string:
  private static final String BARCO_DESTRUIDO = "------------------------------\nBarco destruído!\n------------------------------";
  private static final String TIRO_NA_AGUA = "------------------------------\nTiro na Água!\n------------------------------";
  private static final String ALVO_JA_ATACADO = "------------------------------\nAlvo já havia sido atacado antes!\n------------------------------";


  public static void print(String s){
    System.out.println(s);
  }

  // verificar se os dois jogadores ainda possuem tiros disponiveis
  public static boolean jogadoresPossuemTiros(){
    int tiros=0;
    for (Jogador j : jogadores)
      tiros += j.getTiros(); // soma os tiros dos 2 jogadores
    // se a soma for maior que zero, é pq ainda há tiros disponiveis
    if (tiros > 0)
      return true;
    return false;
  }

  public static int tirosSobrando(){
    int tiros=0;
    for (Jogador j : jogadores)
      tiros += j.getTiros();
    return tiros;
  }

  public static String placar(){
    String nome1 = jogadores[0].getNome();
    String pontos1 = ""+jogadores[0].getPontos();
    String nome2 = jogadores[1].getNome();
    String pontos2 = ""+jogadores[1].getPontos();
    return "------------Placar------------\n"+ nome1 + ": "+ pontos1 +"\n"+ nome2 +": "+ pontos2 +"\n------------------------------";
  }

  // pegar o índice do jogador com mais pontos
  public static int indexJogadorComMaisPontos() {
    int p1 = jogadores[0].getPontos();
    int p2 = jogadores[1].getPontos();
    // verifica a maior pontuação para retornar o índice
    if (p1 > p2)
      return 0;
    else if (p2 > p1)
      return 1;
    // se não houver um maior que o outro,
    // significa ue serão iguais, logo, retorne -1
    return -1;
  }


  public static void main(String[] args) {
    int n=0, porta;
    int[] dados = new int[4];

    // configurar jogo
    print("Configure o servidor.");
    System.out.print("Número de linhas do tabuleiro: ");
    dados[0] = in.nextInt();
    System.out.print("Número de colunas do tabuleiro: ");
    dados[1] = in.nextInt();
    System.out.print("Número de barcos para cada jogador: ");
    dados[2] = in.nextInt();
    System.out.print("Número de tiros: ");
    dados[3] = in.nextInt();
    System.out.print("Digite a porta para a conexão: ");
    porta = in.nextInt();
    print("----------------------------------------");

    try {
      String ip = Inet4Address.getLocalHost().getHostAddress();
      servidor = new ServerSocket(porta);
      // aguardar duas conexões e envia os dados do jogo para os clientes

      print("Aguardando conexões na porta "+ porta +". IP ["+ ip +"]");
      for (n=0; n<2; n++){
        clientes[n] = servidor.accept();
        print("Host conectado: "+ clientes[n].getInetAddress().getHostName()+" ["+ clientes[n].getInetAddress().getHostAddress()+ "]");
      }
      print("----------------------------------------");

      // enviar dados do jogo para os clientes
      print("Aguardando jogadores");
      for (int i=0; i<2; i++){
        ostream[i] = new ObjectOutputStream(clientes[i].getOutputStream());
        ostream[i].writeObject(dados);
        ostream[i].flush();
      }

      // receber jogadores dos clientes e enviar o índice para o respecctivo clientes
      for (int i=0; i<2; i++){
        istream[i] = new ObjectInputStream(clientes[i].getInputStream());
        jogadores[i] = (Jogador) istream[i].readObject();
        tabs[i] = jogadores[i].getTabuleiro();
        print("Jogador "+(i+1)+": "+ jogadores[i].getNome());
        ostream[i].writeInt(i); // envia o índice para o respectivo cliente para identificação
        ostream[i].flush();
      }
      print("----------------------------------------");

      // enviar o vetor de jogadores e tabuleiros para os clientes
      for (int i=0; i<2; i++){
        ostream[i].writeObject(jogadores);
        ostream[i].writeObject(tabs);
        ostream[i].flush();
      }

      // iniciar a Batalha
      int i, def, x, y, iteration=0;
      int[] coord = new int[2];
      char alvo;
      String result = "";

      // LOOP ==================================================================================================//
      int tirosSobrando = tirosSobrando();
      while (tirosSobrando() >= 0) {
        coord = null; // reseta coordenadas
        i = iteration % 2; // indice do jogador que ataca
        if (i==0) def = 1; // indice do jogador que é atacado
        else def = 0;
        //print((iteration+1)+ ": " + jogadores[i].getNome() +" ataca.\tTiros: "+ jogadores[i].getTiros());

        // mandar um sinal para os clientes com o índice de quem atacante
        // e a string com o placar
        for (int k=0; k<2; k++){
          if (tirosSobrando() == 0) {
            ostream[k].writeInt(2);
            ostream[k].writeObject( placar() );
            ostream[k].flush();
            //tirosSobrando -=1 ;
          } else {
            ostream[k].writeInt(i);
            ostream[k].writeObject( placar() );
            ostream[k].flush();
          }
        }

        if (tirosSobrando() > 0){
          // esperar coordenadas do tiro
          coord = (int[]) istream[i].readObject();

          // efetuar ataque
          alvo = jogadores[i].atacar(jogadores[def], coord[0], coord[1]);
          if (alvo == 'B') result = BARCO_DESTRUIDO;
          else if (alvo == '~') result = TIRO_NA_AGUA;
          else result = ALVO_JA_ATACADO;

          // enviar log do resultado do tiro para os clientes
          for (int k=0; k<2; k++){
            ostream[k].writeObject(jogadores[i].getNome() + " atirou nas coordenadas ("+ coord[0] +","+ coord[1] +")\n"+ result);
            ostream[k].flush();
          }

          iteration++;

        } else {
          // se tirosSobrando == 0
          break;
        }

      }// fim do while

      // enviar o índice do vencedor
      // em caso de empate, indexVencedor = -1
      int indexVencedor = indexJogadorComMaisPontos();
      for (int k=0; k<2; k++) {
        ostream[k].writeInt(indexVencedor);
        ostream[k].flush();
      }

      // aguarda os clientes encerrarem a conexão
      String[] str = new String[2];
      for (int k=0; k<2; k++)
        str[k] = (String) istream[k].readObject();

      // fechando streams e socket
      for (i=0; i<2; i++) {
        istream[i].close();
        ostream[i].close();
        clientes[i].close();
      }
      print("Conexões encerradas.");

    } catch(Exception e){
      System.out.println("Erro: "+ e.toString());
    }
  }
}
