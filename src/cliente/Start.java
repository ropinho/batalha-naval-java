package cliente;

import java.net.Socket;
import java.net.InetAddress;

public class StartCliente {

  static Socket cliente;
  static String ip;

  public static void main(String[] args) {
    try {
      ip = InetAddress.getLocalHost().getHostAddress();
      int port = 8000;

      cliente = new Socket(ip, port);
      System.out.println("Conectado a porta "+ port);

    } catch(Exception e) {
      System.out.println("erro: "+ e.toString());
    }

  }

}
