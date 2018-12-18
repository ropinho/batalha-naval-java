package game;
/*
  A classe partida é usada para manipular as informações do jogo durante
  uma partida/batalha. Os métodos serão usados para prover serviços ás
  solicitações dos clientes (jogadores).
*/
import java.io.Serializable;

public class Partida implements Serializable {

  private Jogador[] jogador = new Jogador[2];

  public Partida(Jogador j1, Jogador j2) {
    this.jogador[0] = j1;
    this.jogador[1] = j1;
  }

  public Jogador getJogador1(){
    return this.jogador[0];
  }
  public Jogador getJogador2(){
    return this.jogador[1];
  }

  // jogador ataca o outro jogador nas coordenadas passadas
  public void jogadorAtaca(int i_jogador, int x, int y) {
    if (i_jogador > 0 && i_jogador < 3){
      Jogador atk, def;
      if (i_jogador == 1){
        atk = jogador[0];
        def = jogador[1];
      } else {
        atk = jogador[1];
        def = jogador[0];
      }


    }
  }

  public void printNomesJogadores(){
    System.out.printf("Batalha: %s vs %s\n", jogador[0].getNome(), jogador[1].getNome());
  }

}
