import java.util.Scanner;

public class Play {

	static Jogador jogador1;
	static Jogador jogador2;
	static Batalha batalha;

	static Scanner input = new Scanner(System.in);

	public static void initPlayers(){
		String name;

		System.out.print("Nome do jogador 1: ");
		name = input.next();
		jogador1 = new Jogador(name);

		System.out.print("Nome do jogador 2: ");
		name = input.next();
		jogador2 = new Jogador(name);
	}

	// imprime os tabuleiros dos dois jogadores
	//
	public static void printAmbosTab(){
		System.out.println(jogador1.getNome() +"'s tab");
		jogador1.printTab();
		System.out.println(jogador2.getNome() +"'s tab");
		jogador2.printTab();
	}


	// Posicioinar os 10 barcos em seu tabuleiro
	public static void posicionarBarcos(Jogador j) {
		int x, y;
		System.out.println(j.getNome() + ": Adicionar barco em:");
		while (j.getNumBarcos() < j.MAX_BARCOS){
			System.out.print("x: ");
			x = input.nextInt();
			System.out.print("y: ");
			y = input.nextInt();
			j.posicionarBarco(x, y);
			System.out.printf("Adicionado! (%d/%d)\n", j.getNumBarcos(), j.MAX_BARCOS);
		}
	}


	public static void main(String[] args){

		initPlayers();

		System.out.println("\n" + jogador1.getNome() +" vs "+ jogador2.getNome() + "\n");

		posicionarBarcos(jogador1);
		posicionarBarcos(jogador2);

		printAmbosTab();

		batalha = new Batalha(jogador1, jogador2);
		batalha.start();

	}

}
