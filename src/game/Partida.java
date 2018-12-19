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
    this.jogador[1] = j2;
  }

  public Jogador getJogador1(){
    return this.jogador[0];
  }
  public Jogador getJogador2(){
    return this.jogador[1];
  }

  // jogador ataca o outro jogador nas coordenadas passadas
  public void jogadorAtaca(int ij, int x, int y) {
    if (ij > 0 && ij < 3){
      Jogador atk, def;
      atk = jogador[ij-1];
      if (ij == 1){
        def = this.jogador[1];
      } else if (ij == 2){
        def = this.jogador[0];
      }

      System.out.printf("%s ataca nas coordenadas (%d, %d)\n", atk.getNome(), x, y);

    }
  }

  public void printNomesJogadores(){
    System.out.printf("Batalha: %s vs %s\n", this.jogador[0].getNome(), this.jogador[1].getNome());
  }

  // imprime os tabuleiros dos dois jogadores

	public void printClientTab(){
    // jogador[1] é o cliente
    System.out.println("Seu tabuleiro:");
    jogador[1].printTab();
    // jogador[0] é o servidor
		System.out.println("Tabuleiro de "+ jogador[0].getNome());
		jogador[0].printTabSecret();
	}

  public void printServerTab(){
    // jogador[0] é o servidor
    System.out.println("Seu tabuleiro:");
    jogador[0].printTab();
    // jogador[1] é o cliente
		System.out.println("Tabuleiro de "+ jogador[1].getNome());
		jogador[1].printTabSecret();
	}

}
