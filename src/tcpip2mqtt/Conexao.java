package tcpip2mqtt;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexao {

    private final int porta;
    private final Ouvinte ouvinte;
    private final int tamanhoPacote;

    public Conexao(int porta, Ouvinte ouvinte, int tamanhoPacote) {
        this.porta = porta;
        this.ouvinte = ouvinte;
        this.tamanhoPacote = tamanhoPacote;
    }

    public void iniciar() {
        new Thread(new Recebe()).start();
    }

    class Recebe implements Runnable {

        byte[] dadosReceber = new byte[tamanhoPacote];
        boolean erro = false;
        DatagramSocket socket = null;

        @Override
        public void run() {
            while (true) {
                try {
                    socket = new DatagramSocket(porta);
                } catch (SocketException ex) {
                    Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
                }
                erro = false;
                while (!erro) {
                    DatagramPacket pacoteRecebido = new DatagramPacket(dadosReceber, dadosReceber.length);
                    try {
                        socket.receive(pacoteRecebido);

                        byte[] b = new byte[pacoteRecebido.getData().length];
                        b = pacoteRecebido.getData();

                        ouvinte.notificar(b);
                    } catch (Exception e) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        erro = true;
                        continue;
                    }
                }
            }
        }
    }

}
