# Batalha Naval em Java

Simulação do jogo de batalha naval usando para entendimento da utilização de Sockets em Java

Utiliza os pacotes `java.net` e `java.io`

## Como executar

Para rodar, execute os arquivos compilados no diretório `bin/start`:

Servidor:

```sh
cd bin
java start.Servidor
```

Inicia o servidor do jogo, esse tem o objetivo de conectar dois jogadores e definir os parâmetros da partida, como: número de tiros, número de barcos, tamanho do tabuleiro, etc...

Cliente:

```sh
cd bin
java start.Cliente
```

Inicia um cliente, ou seja, um jogador. O cliente personaliza o nome e se conecta ao servidor (é necessário ter um servidor rodando). Após dois clientes se conectarem ao mesmo servidor através do IP e porta, os clientes recebem os parâmetros do jogo e escolhem os locais para posicionar seus barcos no tabuleiro.

A partida inicia quando dois clientes se conectam e definem seus tabuleiros.

## License

[MIT License](https://mit-license.org/)