package tcpip2mqtt.conexao;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import tcpip2mqtt.interfaces.Ouvinte;

/**
 * https://github.com/pcollares/tcpip2mqtt
 *
 * @author Paulo Collares
 */
public class ReceptorTCP implements Runnable {

    private final int porta;
    private final int tamanhoPacote;

    private Ouvinte ouvinte;
    private ServerSocket servidorTCP;

    public ReceptorTCP(int porta, int tamanhoPacote, Ouvinte ouvinte) {
        this.porta = porta;
        this.ouvinte = ouvinte;
        this.tamanhoPacote = tamanhoPacote;
    }

    @Override
    public void run() {
        while (true) {
            try (ServerSocket svrTCP = new ServerSocket(porta);) {
                servidorTCP = svrTCP;
                servidorTCP.setReuseAddress(true);
                servidorTCP.setSoTimeout(0);

                while (!servidorTCP.isClosed()) {
                    Socket socketCliente = servidorTCP.accept();
                    socketCliente.setKeepAlive(true);
                    socketCliente.setSoTimeout(0);

                    new Thread(new Conn(socketCliente)).start();

                }
            } catch (IOException ex) {
                Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private class Conn implements Runnable {

        private Socket socketCliente;
        private InputStream in;

        public Conn(Socket socketCliente) throws IOException {
            try {
                this.socketCliente = socketCliente;
                in = socketCliente.getInputStream();

            } catch (IOException e) {
                in.close();
                Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, e);
            }
        }

        @Override
        public void run() {
            while (!socketCliente.isClosed()) {
                try {
                    byte[] dadosReceber = new byte[tamanhoPacote];

                    in.read(dadosReceber);
               
                    ouvinte.notificar(dadosReceber);

                } catch (IOException e) {
                    Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, e);
                    try {

                        in.close();
                        socketCliente.close();
                        socketCliente = null;
                        return;
                    } catch (IOException ioe) {
                        Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ioe);
                    }

                }
            }
        }

    }

}
