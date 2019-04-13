package tcpip2mqtt.interfaces;

/**
 * https://github.com/pcollares/tcpip2mqtt
 *
 * @author Paulo Collares
 */
public interface Ouvinte {

    public void notificar(byte[] b);

}
