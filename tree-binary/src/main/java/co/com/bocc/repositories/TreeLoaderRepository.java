package co.com.bocc.repositories;

import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.TableWriteItems;
import com.google.inject.Inject;
import io.vavr.control.Try;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TreeLoaderRepository {

  private static final Logger LOGGER = LogManager.getLogger(TreeLoaderRepository.class);

  private DynamoDB dynamoDB;
  private String tableName;

  @Inject
  public TreeLoaderRepository(
      final DynamoDB dynamoDB, final String tableName) {
    this.dynamoDB = dynamoDB;
    this.tableName = tableName;
  }

  public void saveItems(final Item items) {

    LOGGER.info("Writing item tree: {}", items.asMap().toString());
    BatchWriteItemOutcome outcome;
    final TableWriteItems tableWriteItems =
        new TableWriteItems(tableName).withItemsToPut(items);
    outcome = dynamoDB.batchWriteItem(tableWriteItems);
    while (outcome.getUnprocessedItems().size() > 0) {
      LOGGER
          .info("Processing " + outcome.getUnprocessedItems().size() + " unprocessed items");
      outcome = dynamoDB.batchWriteItemUnprocessed(outcome.getUnprocessedItems());
    }
  }

  public Item getItem(final String idTree) {
    LOGGER.debug("********** findTreeById started **********");
    return Try.of(() -> dynamoDB.getTable(tableName))
        .map(table -> Optional
            .ofNullable(table.getItem(getPrimaryKey(idTree))))
        .peek(item -> LOGGER.debug("is the key present in teh database? {},", item.isPresent()))
        .get().get();
  }

  private PrimaryKey getPrimaryKey(final String idTree) {

    final PrimaryKey primaryKey = new PrimaryKey();
    primaryKey.addComponent("idNumber", idTree);
    primaryKey.addComponent("idType", "tree");
    return primaryKey;
  }

}
