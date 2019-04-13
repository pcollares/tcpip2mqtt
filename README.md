# TCP/IP 2 MQTT

Sistema simples para recebimento de pacotes TCP e UDP e publicar em um tópico MQTT

# Como usar

Edite o config.properties conforme descrito abaixo

SERVER_URI: URI do servidor MQTT, ex: tcp://127.0.0.1:1883

TIMEOUT_SUBSCRIBE: Configuração de timeout ao se inscrever

TIMEOUT_PUBLISH: Configuração de timeout ao publicar

MAX_INFLIGHT: Maximo de mensagens que o cliente irá manter localmente

USER_NAME: Usuário para conexão ao MQTT (Se houver)

USER_PASS: Senha para conexão ao MQTT (Se houver)

CONNECTIONS = Conexões para cada porta/Tópico MQTT, no seguinte formato: "protocolo,porta,tópico,qos,tamanho_pacote;", ex: TCP,4444,teste/sinal/4444,2,0;UDP,4445,teste/sinal/4445,2,100;TCP,4446,teste/sinal/4446,0,0;

# Observações

- O tamanho do pacote é é usado no protocolo UDP, mas deve ser preencido mesmo se for o TCP, preencha com qualquer valor inteiro
- Qualquer erro no arquivo o programa não será carregado
