package tcpip2mqtt;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                int porta = Integer.parseInt(cc[0]);
                String topico = cc[1];
                int qos = Integer.parseInt(cc[2]);
                int tamanho = Integer.parseInt(cc[3]);

                roteadores.add(new Roteador(porta, topico, qos, tamanho));
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
