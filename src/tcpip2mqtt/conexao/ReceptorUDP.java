package tcpip2mqtt.conexao;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tcpip2mqtt.interfaces.Ouvinte;

/**
 * https://github.com/pcollares/tcpip2mqtt
 * @author Paulo Collares
 */
public class ReceptorUDP implements Runnable {

    private final int porta;
    private final byte[] dadosReceber;
    private boolean erro = false;
    private DatagramSocket socket = null;
    private Ouvinte ouvinte;

    public ReceptorUDP(int porta, int tamanhoPacote, Ouvinte ouvinte) {
        this.porta = porta;
        this.ouvinte = ouvinte;
        dadosReceber = new byte[tamanhoPacote];
    }

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
