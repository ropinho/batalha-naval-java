/*
 * by: Ronaldd Pinho
 * ronaldppinho@gmail.com
 */

public class Jogador implements Serializable {

	private static final long serialVersionUID = 1L;
	final int MAX_BARCOS = 10;

	private byte[] bytes;
	private String nome;
	private int n_tiros = 10;
	private int n_barcos = 0;
	private int pontos = 0;
	private Tabuleiro tab;

	public Jogador(String n){
		this.nome = n;
		this.tab = new Tabuleiro(10, 10);
	}

	//--- getters e setters -------------------------------------------------//
	public String getNome(){
		return this.nome;
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
		this.tab.print();
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

	/* método usado para atacar um inimigo
	 * passando como parâmetros: o inimigo, e as coordenadas x e y do tabuleiro do inimigo
	 * retorna o caractere que estava na posição que foi atacada */
	public char atacar(Jogador inimigo, int x, int y){
		// se a posição passada já tiver sido atacada (estará marcada com um 'X')
		char c = ' ';
		if (inimigo.getTab(x,y) != 'X'){ // se a posição for diferente de X
			c = inimigo.getTab(x,y); // pega o caractere
			inimigo.setTab(x, y, 'X');  // muda a posição atacada para 'X'
			this.n_tiros--; // diminui num de tiros
		}
		return c;
	}
}
