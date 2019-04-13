package tcpip2mqtt.conexao;

import tcpip2mqtt.interfaces.Ouvinte;

/**
 * https://github.com/pcollares/tcpip2mqtt
 *
 * @author Paulo Collares
 */
public class Conexao {

    public enum Protocolo {
        TCP,
        UDP
    }

    private final Protocolo protocolo;
    private final int porta;
    private final Ouvinte ouvinte;
    private final int tamanhoPacote;

    public Conexao(Protocolo protocolo, int porta, Ouvinte ouvinte, int tamanhoPacote) {
        this.protocolo = protocolo;
        this.porta = porta;
        this.ouvinte = ouvinte;
        this.tamanhoPacote = tamanhoPacote;
    }

    public void iniciar() {
        switch (protocolo) {
            case TCP:
                new Thread(new ReceptorTCP(porta, ouvinte)).start();
                break;
            case UDP:
                new Thread(new ReceptorUDP(porta, tamanhoPacote, ouvinte)).start();
                break;
        }
    }

}
