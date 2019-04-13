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

    private Ouvinte ouvinte;
    private ServerSocket servidorTCP;

    public ReceptorTCP(int porta, Ouvinte ouvinte) {
        this.porta = porta;
        this.ouvinte = ouvinte;
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
                    InputStream in = socketCliente.getInputStream();
                    try {
                        byte[] dadosReceber = new byte[in.available()];

                        in.read(dadosReceber);
                        ouvinte.notificar(dadosReceber);

                    } catch (IOException e) {
                        Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, e);
                    } finally {
                        try {
                            in.close();
                            socketCliente.close();
                        } catch (IOException ioe) {
                            Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ioe);
                        }
                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(Conexao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
