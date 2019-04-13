package tcpip2mqtt;

import tcpip2mqtt.roteador.Roteador;
import tcpip2mqtt.mqtt.ClienteMQTT;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import tcpip2mqtt.conexao.Conexao.Protocolo;

/**
 * https://github.com/pcollares/tcpip2mqtt
 *
 * @author Paulo Collares
 */
public class Main {

    private static final String PROPERTIES = "config.properties";
    private static Properties properties;

    private static List<Roteador> roteadores;

    public static void main(String[] args) {

        properties = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(PROPERTIES);
            properties.load(input);
        } catch (IOException ex) {
            Logger.getLogger(ClienteMQTT.class.getName()).log(Level.SEVERE, null, ex);
        }

        roteadores = new ArrayList<>();

        try {
            String conexoes = properties.getProperty("CONNECTIONS");
            String c[] = conexoes.split(";");

            for (String conexao : c) {
                String cc[] = conexao.split(",");
                Protocolo protocolo = Protocolo.valueOf(cc[0]);
                int porta = Integer.parseInt(cc[1]);
                String topico = cc[2];
                int qos = Integer.parseInt(cc[3]);
                int tamanho = Integer.parseInt(cc[4]);

                roteadores.add(new Roteador(protocolo, porta, topico, qos, tamanho));
            }
        } catch (Exception e) {
            Logger.getLogger(ClienteMQTT.class.getName()).log(Level.SEVERE, null, e);
        }

        for (Roteador roteador : roteadores) {
            roteador.iniciar();
        }

    }

    public static Properties getProperties() {
        return properties;
    }

}
