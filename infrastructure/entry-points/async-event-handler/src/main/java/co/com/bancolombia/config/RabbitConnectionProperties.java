package co.com.bancolombia.config;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import static com.rabbitmq.client.ConnectionFactory.DEFAULT_AMQP_OVER_SSL_PORT;

@Builder
@Data
public class RabbitConnectionProperties {

    public int port;
    private String virtualhost;
    private String hostname;
    private String username;
    private String password;
    @Getter
    private boolean ssl;

    public int getPort(){
        return isSsl() ? DEFAULT_AMQP_OVER_SSL_PORT: port;
    }
}
