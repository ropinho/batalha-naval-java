package game;

/* A classe partida é usada para manipular as informações do jogo durante
   uma partida/batalha. Os métodos serão usados para prover serviços ás
   solicitações dos clientes (jogadores).
*/
public class Partida {

  private Jogador jogador1;
  private Jogador jogador2;

  public Partida(Jogador j1, Jogador j2) {
    jogador1 = j1;
    jogador2 = j2;
  }

  // jogador j1 ataca o jogador j2 nas coordenadas passadas
  public void ataca(Jogador j1, Jogador j2, int x, int y) {

  }

}
