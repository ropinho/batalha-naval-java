package game;

import java.io.Serializable;

public class Jogador implements Serializable {

	private static final long serialVersionUID = 1L;
	public static int MAX_BARCOS; // num máximo de barcos

	private byte[] bytes;
	private String nome;
	private int n_tiros = 10;
	private int n_barcos = 0;
	private int pontos = 0;
	private Tabuleiro tab;

	public Jogador(String nome, int linhas, int colunas, int barcos, int tiros){
		this.nome = nome;
		this.MAX_BARCOS = barcos;
		this.n_tiros = tiros;
		this.tab = new Tabuleiro(linhas, colunas);
	}

	//--- getters e setters -------------------------------------------------//
	public String getNome(){
		return this.nome;
	}

	public Tabuleiro getTabuleiro(){
		return this.tab;
	}

	public void setTabuleiro(Tabuleiro t){
		this.tab = t;
	}

	public int getNumBarcos(){
		return this.n_barcos;
	}

	public int getTiros(){
		return this.n_tiros;
	}

	public void setTiros(int tiros) {
		this.n_tiros = tiros;
	}

	public int getPontos(){
		return this.pontos;
	}

	public void setPontos(int pontos){
		this.pontos = pontos;
	}

	// retorna a matriz do tabuleiro
	public char[][] getTab(){
		return this.tab.tab;
	}

	public void setTab(char[][] matriz){
		this.tab.setTab(matriz);
	}

	// retorna o caractere da posição específica da matriz do tabuleiro
	public char getTab(int x, int y){
		return this.tab.tab[x][y];
	}

	// define o caractere em uma posição específica da matriz do tabuleiro
	public void setTab(int x, int y, char ch){
		this.tab.tab[x][y] = ch;
	}
	//----------------------------------------------------------------------//

	public void printTab(){
		//System.out.println("Mapa de "+ this.nome);
		this.tab.print();
	}

	public void printTabSecret() {
		//System.out.println("Mapa de "+ this.nome);
		this.tab.printSecret();
	}

	public void posicionarBarco(int x, int y) {
		if (n_barcos < MAX_BARCOS){ // se o número atual de barcos for menor que o máximo
			if (tab.tab[x][y] == '~') { // adiciona se a posição atual for "mar"
				this.tab.tab[x][y] = 'B';
				this.n_barcos++; // incrementa o número atual de barcos
			}
		} else {
			System.out.println("Número máximo de barcos excedido.");
		}
	}

	public char atacar(Jogador inimigo, int x, int y){
		char alvo = inimigo.getTab(x, y);
		this.n_tiros -= 1; // decrementa num de tiros

		if (alvo == 'B'){ // se o alvo for um barco
			inimigo.setTab(x, y, 'X');
			this.setPontos(this.pontos + 1);
		} else if (alvo == '~'){ // se o alvo for água
			inimigo.setTab(x, y, '*');
		}

		return alvo;
	}

}
