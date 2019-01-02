package game;
/*
  A classe partida é usada para manipular as informações do jogo durante
  uma partida/batalha. Os métodos serão usados para prover serviços ás
  solicitações dos clientes (jogadores).
*/
import java.io.Serializable;

public class Batalha implements Serializable {
  // constantes:
  private final String BARCO_DESTRUIDO = "-------------------\nBarco destruído!\n--------------------";
  private final String TIRO_NA_AGUA = "-------------------\nTiro na Água!\n--------------------";
  // vars:
  private Jogador[] jogador = new Jogador[2];

  // construtor
  public Batalha(Jogador[] players) {
    this.jogador = players;
  }


  public Jogador getJogador(int i){
    return this.jogador[i];
  }

  public String getBarcoDestruido(){
    return BARCO_DESTRUIDO;
  }

  public String getTiroNaAgua(){
    return TIRO_NA_AGUA;
  }

  // verificar se os dois jogadores ainda possuem tiros disponiveis
  public boolean jogadoresPossuemTiros(){
    int tiros=0;
    for (Jogador j : jogador)
      tiros += j.getTiros(); // soma os tiros dos 2 jogadores
    // se a soma for maior que zero, é pq ainda há tiros disponiveis
    if (tiros > 0)
      return true;
    return false;
  }


  public void jogadorGanhaPonto(int i){
    this.jogador[i].setPontos(this.jogador[i].getPontos() + 1);
  }


  // jogador ataca o outro jogador nas coordenadas passadas
  // retorna o caractere alvo do tiro
  public char jogadorAtaca(int i, int x, int y) {
    char alvo = '_';

    if (i >= 0 && i < 2){
      Jogador atk=null, def=null;
      atk = jogador[i];
      if (i == 0){
        //def = this.jogador[1];
        alvo = jogador[1].getTab(x,y);
        this.jogador[0].setTiros(this.jogador[0].getTiros()-1); // decrementa o num de tiros do atacante
        if (alvo == 'B') { // if o alvo tiver um B, atingiu um barco: troca por X
          this.jogador[1].setTab(x,y,'X');
          jogadorGanhaPonto(i);
        } else if(alvo == '~'){ // se o alvo tiver um ~ ... tiro na água:
          // troca por '*' para indicar que aquele já foi atirado
          this.jogador[1].setTab(x,y,'*');// atacado muda coord para *
        }

      } else if (i == 1){
        alvo = jogador[0].getTab(x,y);
        this.jogador[1].setTiros(this.jogador[1].getTiros()-1); // decrementa o num de tiros do atacante
        if (alvo == 'B') { // if o alvo tiver um B, atingiu um barco: troca por X
          this.jogador[0].setTab(x,y,'X');
          jogadorGanhaPonto(i);
        } else if(alvo == '~'){ // se o alvo tiver um ~ ... tiro na água:
          // troca por '*' para indicar que aquele já foi atirado
          this.jogador[0].setTab(x,y,'*');// atacado muda coord para *
        }
      }

    }
    return alvo;
  }

  // métodos de print:
  public void printBarcoDestruido(){
    System.out.println(BARCO_DESTRUIDO);
  }
  public void printTiroNaAgua(){
    System.out.println(TIRO_NA_AGUA);
  }

  public void printNomesJogadores(){
    System.out.printf("Batalha: %s vs %s\n", this.jogador[0].getNome(), this.jogador[1].getNome());
  }

  // mostrar o tabuleiro de um dos jogadores e deixar o do outro só com '~'
  public void showTabsForPlayer(int i) {
    if (i == 0){
      System.out.println("Seu tabuleiro:");
      jogador[0].printTab();
      System.out.println("Tabuleiro de "+ jogador[1].getNome());
  		jogador[1].printTabSecret();
    } else if (i == 1){
      System.out.println("Seu tabuleiro:");
      jogador[1].printTab();
      System.out.println("Tabuleiro de "+ jogador[0].getNome());
  		jogador[0].printTabSecret();
    }
  }
}
