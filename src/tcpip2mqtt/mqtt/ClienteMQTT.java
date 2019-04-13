package tcpip2mqtt.mqtt;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import tcpip2mqtt.Main;

/**
 * https://github.com/pcollares/tcpip2mqtt
 *
 * @author Paulo Collares
 */
public class ClienteMQTT implements MqttCallback {

    private static ClienteMQTT instancia;

    private final String serverURI;
    private MqttClient clientSubscribe;
    private final MqttConnectOptions mqttOptionsSubscribe, mqttOptionsPublish;
    private final AtomicInteger contadorClientPublish = new AtomicInteger();

    //ler estes parametros do arquivo de configuração
    private ClienteMQTT() {

        this.serverURI = Main.getProperties().getProperty("SERVER_URI");

        mqttOptionsSubscribe = new MqttConnectOptions();
        mqttOptionsSubscribe.setConnectionTimeout(Integer.parseInt(Main.getProperties().getProperty("TIMEOUT_SUBSCRIBE")));
        mqttOptionsSubscribe.setAutomaticReconnect(true);

        mqttOptionsPublish = new MqttConnectOptions();
        mqttOptionsPublish.setMaxInflight(Integer.parseInt(Main.getProperties().getProperty("MAX_INFLIGHT")));
        mqttOptionsPublish.setConnectionTimeout(Integer.parseInt(Main.getProperties().getProperty("TIMEOUT_PUBLISH")));

        String usuario = Main.getProperties().getProperty("USER_NAME");
        String senha = Main.getProperties().getProperty("USER_PASS");
        if (usuario != null && !usuario.isEmpty() && senha != null && !senha.isEmpty()) {
            mqttOptionsSubscribe.setUserName(usuario);
            mqttOptionsPublish.setUserName(usuario);
            mqttOptionsPublish.setPassword(senha.toCharArray());
            mqttOptionsSubscribe.setPassword(senha.toCharArray());
        }
    }

    public static ClienteMQTT getInstancia() {
        if (instancia == null) {
            instancia = new ClienteMQTT();
        }
        return instancia;
    }

    public void iniciar() {

        try {
            clientSubscribe = new MqttClient(serverURI, "tcpip2mqtt_" + System.currentTimeMillis(), new MqttDefaultFilePersistence(System.getProperty("java.io.tmpdir")));
            clientSubscribe.connect(mqttOptionsSubscribe);
            clientSubscribe.setCallback(this);
        } catch (MqttException ex) {
            Logger.getLogger("Erro ao iniciar cliente MQTT" + ClienteMQTT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void encerrar() {
        try {
            if (clientSubscribe.isConnected()) {
                clientSubscribe.disconnect();
                clientSubscribe.close();
            }
        } catch (MqttException ex) {
            Logger.getLogger("Erro ao desconectar do broker mqtt" + ClienteMQTT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Publica uma mensagem ao broker
     *
     * @param payload
     * @param topic
     */
    public void publicar(byte[] payload, String topic) {
        publicar(payload, 0, topic, false);
    }

    /**
     * Publica uma mensagem ao broker
     *
     * @param payload
     * @param qos
     * @param topic
     */
    public void publicar(byte[] payload, int qos, String topic) {
        publicar(payload, qos, topic, false);
    }

    /**
     * Publica uma mensagem ao broker
     *
     * @param payload
     * @param qos
     * @param topic
     * @param retained
     */
    public void publicar(byte[] payload, int qos, String topic, boolean retained) {

        MqttClient clientPublish = null;

        try {
            clientPublish = new MqttClient(serverURI, "tcpip2mqtt_pub" + contadorClientPublish.incrementAndGet());
            clientPublish.connect(mqttOptionsPublish);
            clientPublish.publish(topic, payload, qos, retained);
        } catch (MqttException ex) {
            Logger.getLogger("Erro ao publicar mensagem no broker MQTT no tópico %s", topic + ClienteMQTT.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (clientPublish != null && clientPublish.isConnected()) {
                try {
                    clientPublish.disconnect();
                    clientPublish.close();
                } catch (MqttException ex) {
                    Logger.getLogger("Erro ao encerrar conexão de publicação MQTT" + ClienteMQTT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        throw new UnsupportedOperationException("Perda de conexão com o broker");
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
        throw new UnsupportedOperationException("Mensagem recebida mas não há nenhum ouvinte para trata-la: %s" + string);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        throw new UnsupportedOperationException("Entrega da mensagem MQTT completa, id: %d" + imdt.getMessageId());
    }

}
