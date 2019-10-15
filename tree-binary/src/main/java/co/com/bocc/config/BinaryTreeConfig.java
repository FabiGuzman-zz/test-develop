package co.com.bocc.config;

import co.com.bocc.consumer.BinaryTreeConsumer;
import co.com.bocc.consumer.BinaryTreeConsumerImpl;
import co.com.bocc.services.BinaryTreeService;
import co.com.bocc.services.BinaryTreeServiceImpl;
import co.com.bocc.validator.RequestValidator;
import co.com.bocc.validator.RequestValidatorImpl;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BinaryTreeConfig extends AbstractModule {

  public static final String DYNAMO_DB_TABLE = "dynamoTreeTable";
  private static final Logger LOGGER = LogManager.getLogger(BinaryTreeConfig.class);

  @Override
  protected void configure() {

    LOGGER.debug("Initing configuration of clases");

    bind(BinaryTreeConsumer.class).to(BinaryTreeConsumerImpl.class);
    bind(BinaryTreeService.class).to(BinaryTreeServiceImpl.class);
    bind(RequestValidator.class).to(RequestValidatorImpl.class);
  }

  @Provides
  String tableName() {
    return System.getenv(DYNAMO_DB_TABLE);
  }

  @Provides
  AmazonDynamoDB clientDynamoDB() {
    return AmazonDynamoDBClientBuilder.standard().build();
  }

  @Provides
  DynamoDB dynamoDB() {
    final AmazonDynamoDB clientDynamoDB = AmazonDynamoDBClientBuilder.standard().build();
    final DynamoDB dynamoDB = new DynamoDB(clientDynamoDB);
    return dynamoDB;
  }

}
