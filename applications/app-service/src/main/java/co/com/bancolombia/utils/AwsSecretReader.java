package co.com.bancolombia.utils;

import co.com.bancolombia.secretsmanager.api.GenericManager;
import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import co.com.bancolombia.secretsmanager.connector.AWSSecretManagerConnector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



@Service
public class AwsSecretReader {

    private final Logger logger;
    private final GenericManager manager;

    @Autowired
    public AwsSecretReader(@Value("${aws.region}") String awsRegion, @Value("${localstack}") String localstack) {
        logger = LogManager.getLogger(AwsSecretReader.class);
        manager = new AWSSecretManagerConnector(awsRegion, localstack);
    }

    public AwsSecretReader(GenericManager manager) {
        logger = LogManager.getLogger(AwsSecretReader.class);
        this.manager = manager;
    }

    public <T> T readSecret(String secretName, Class<T> returnClassType) throws SecretException {
        try {
            return manager.getSecret(secretName, returnClassType);
        } catch (SecretException ex) {
            logger.error("Error leyendo un secreto");
            throw ex;
        }
    }
}

