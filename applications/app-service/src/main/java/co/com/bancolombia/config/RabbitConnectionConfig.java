package co.com.bancolombia.config;

import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import co.com.bancolombia.utils.AwsSecretReader;
import com.rabbitmq.client.ConnectionFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.reactivecommons.async.rabbit.config.ConnectionFactoryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.net.ssl.SSLContext;
import java.security.SecureRandom;

@Configuration
@AllArgsConstructor
@Data
public class RabbitConnectionConfig {
    private final AwsSecretReader awsSecretReader;

    @Bean
    @Primary
    @Profile("local")
    public RabbitConnectionProperties getConnectionPropertiesLocal(
            @Value("${adapter.rabbitmq.host}") String host,
            @Value("${adapter.rabbitmq.port}") int port,
            @Value("${adapter.rabbitmq.username}") String user,
            @Value("${adapter.rabbitmq.password}") String password,
            @Value("${adapter.rabbitmq.virtualhost}") String virtualHost,
            @Value("${adapter.rabbitmq.ssl}") boolean ssl) {

        return RabbitConnectionProperties.builder()
                .hostname(host)
                .password(password)
                .port(port)
                .ssl(ssl)
                .username(user)
                .virtualhost(virtualHost)
                .build();
    }

    @Bean
    @Primary
    @Profile("!local")
    public RabbitConnectionProperties getConnectionProperties(@Value("${aws.secret-manager.svp}") String secretName)
            throws SecretException {
        return awsSecretReader.readSecret(secretName,
                RabbitConnectionProperties.class);
    }

//    @Bean
//    @Primary
//    @Profile("!local")
//    public AsyncRabbitPropsDomainProperties domainProperties(@Value("${aws.secret-manager.svp}") String secretRabbitSvp,
//                                                             @Value("${aws.secret-manager.app}") String secretRabbitApp)
//            throws SecretException {
//        RabbitProperties propertiesSvp = getConnectionProperties(secretRabbitSvp);
//        RabbitProperties propertiesApp = getConnectionProperties(secretRabbitApp);
//
//        return AsyncRabbitPropsDomainProperties.builder()
//                .withDomain("svp", AsyncProps.builder()
//                        .connectionProperties(propertiesSvp)
//                        .build())
//                .withDomain("app", AsyncProps.builder()
//                        .connectionProperties(propertiesApp)
//                        .build())
//                .build();
//    }
//
//
//    @Bean
//    @Primary
//    public AsyncPropsDomain.SecretFiller customFiller() {
//        return (domain, asyncProps) -> {
//
//        };
//    }


    @Bean
    @Primary
    @Profile("!local")
    public ConnectionFactoryProvider connection(RabbitConnectionProperties properties) {
        var factory = new ConnectionFactory();
        var map = PropertyMapper.get();
        map.from(properties::getHostname).whenNonNull().to(factory::setHost);
        map.from(properties::getPort).to(factory::setPort);
        map.from(properties::getUsername).whenNonNull().to(factory::setUsername);
        map.from(properties::getPassword).whenNonNull().to(factory::setPassword);
        map.from(properties::getVirtualhost).whenNonNull().to(factory::setVirtualHost);
        map.from(properties::isSsl).whenTrue().as(ssl -> factory).to(this::configureSsl);
        return () -> factory;
    }


    @SneakyThrows
    private void configureSsl(ConnectionFactory connectionFactory) {
        var sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, null, new SecureRandom());
        connectionFactory.useSslProtocol(sslContext);
    }
}

