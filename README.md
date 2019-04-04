# TCP/IP 2 MQTT

Sistema simples para recebimento de pacotes UDP e publicar em um tópico MQTT

# Como usar

Edite o config.properties conforme descrito abaixo

SERVER_URI: URI do servidor MQTT, ex: tcp://127.0.0.1:1883

TIMEOUT_SUBSCRIBE: Configuração de timeout ao se inscrever

TIMEOUT_PUBLISH: Configuração de timeout ao publicar

MAX_INFLIGHT: Maximo de mensagens que o cliente irá manter localmente

USER_NAME: Usuário para conexão ao MQTT (Se houver)

USER_PASS: Senha para conexão ao MQTT (Se houver)

CONNECTIONS = Conexões para cada porta UDP/Tópico MQTT, no seguinte formato: "porta,tópico,qos,tamanho_pacote;", ex: 4444,teste/sinal/4444,2,30;4445,teste/sinal/4445,2,100;4446,teste/sinal/4446,0,30;
