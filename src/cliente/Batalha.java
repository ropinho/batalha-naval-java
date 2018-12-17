package cliente;

import java.util.Scanner;

public class Batalha extends java.lang.Thread {

	private final Jogador player1;
	private final Jogador player2;
	private boolean endgame;
	private Scanner in;

	public Batalha(Jogador p1, Jogador p2) {
		this.player1 = p1;
		this.player2 = p2;
		in = new Scanner(System.in);
	}

	@Override
	public void run() {
		int x, y;
		char c;
		Jogador ataca, defende, aux;
		ataca = player1;    // "ataca" aponta para o jogador que vai atacar
		defende = player2;  // "defende" aponta para o jogador que vai defender
		// esses ponteiros serão invertidos no fim de cada iteração

		/* enquanto a variavel "endgame" possuir o valor "false"
		 * os jogadores atacam um ao outro em turnos */
		while (!endgame) {

			printAmbosTab(ataca, defende); // imprime os dois tabuleiros
			printPlacar();   // mostra placar do jogo

			System.out.println(ataca.getNome() +"'s turn");
			// escolhe a posição para atacar
			System.out.println("Escolha a posição para atacar:");
			System.out.print("x: ");
			x = in.nextInt();
			System.out.print("y: ");
			y = in.nextInt();

			if (x < 10 && y < 10){
				/* o caractere retornado pelo método atacar() é usado para verificar
				 * o resultado do ataque.
				 * */
				c = ataca.atacar(defende, x, y);
				if (c == 'B'){
					System.out.println("-------------------\nBarco destruído!\n--------------------");
					ataca.setPontos(ataca.getPontos()+1); // incrementa um ponto
				} else if (c == '~'){
					System.out.println("-------------------\nTiro na água!\n--------------------");
				}
			}

			// verificar a qtd de tiros restantes do próximo atacante
			if (defende.getTiros() == 0){
				endgame = true;
			}

			// troca os turnos
			aux = ataca;
			ataca = defende;
			defende = aux;

		}// end of while(!endgame)

		// mostrar vencedor
		if (player1.getPontos() > player2.getPontos()){
			System.out.printf("----------------------------------------\n\t%s VENCEU!\n----------------------------------------\n", player1.getNome());
		} else if (player1.getPontos() < player2.getPontos()) {
			System.out.printf("----------------------------------------\n\t%s VENCEU!\n----------------------------------------\n", player2.getNome());
		} else {
			System.out.printf("----------------------------------------\n\tEMPATE!\n----------------------------------------\n");
		}

	}


	// imprime os tabuleiros dos dois jogadores
	public void printAmbosTab(Jogador j1, Jogador j2){
		System.out.println(j1.getNome() +"'s tab");
		j1.printTab();
		System.out.println(j2.getNome() +"'s tab");
		j2.printTab();
	}

	public void printPlacar() {
		System.out.println("--Placar------------------------------------------");
		System.out.printf("%s:\t%d\tTiros restantes: %d\tBarcos: %d/%d\n", player1.getNome(), player1.getPontos(), player1.getTiros(), player1.getNumBarcos(), player1.MAX_BARCOS);
		System.out.printf("%s:\t%d\tTiros restantes: %d\tBarcos: %d/%d\n", player2.getNome(), player2.getPontos(), player2.getTiros(), player2.getNumBarcos(), player2.MAX_BARCOS);
		System.out.println("--------------------------------------------------\n");
	}

}
