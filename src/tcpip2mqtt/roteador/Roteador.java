package tcpip2mqtt.roteador;

import tcpip2mqtt.interfaces.Ouvinte;
import tcpip2mqtt.mqtt.ClienteMQTT;
import tcpip2mqtt.conexao.Conexao;
import tcpip2mqtt.conexao.Conexao.Protocolo;

/**
 * https://github.com/pcollares/tcpip2mqtt
 *
 * @author Paulo Collares
 */
public class Roteador implements Ouvinte {

    private final int qos;
    private final Conexao conexao;
    private final String topico;

    public Roteador(Protocolo protocolo, int porta, String topico, int qos, int tamanoPacote) {
        this.topico = topico;
        this.qos = qos;
        conexao = new Conexao(protocolo, porta, this, tamanoPacote);
    }

    public void iniciar() {
        conexao.iniciar();
    }

    @Override
    public void notificar(byte[] b) {
        ClienteMQTT.getInstancia().publicar(b, qos, topico);
    }

    public Conexao getConexao() {
        return conexao;
    }

}
