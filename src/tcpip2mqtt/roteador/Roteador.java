package tcpip2mqtt;

public class Roteador implements Ouvinte {

    private final int qos;
    private final Conexao conexao;
    private final String topico;

    public Roteador(int porta, String topico, int qos, int tamanoPacote) {
        this.topico = topico;
        this.qos = qos;
        conexao = new Conexao(porta, this, tamanoPacote);
    }
    
    public void iniciar(){
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
