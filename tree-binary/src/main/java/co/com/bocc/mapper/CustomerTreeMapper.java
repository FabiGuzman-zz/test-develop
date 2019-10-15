package co.com.bocc.mapper;

import com.amazonaws.services.dynamodbv2.document.Item;
import java.util.List;

public class CustomerTreeMapper {

  public Item toMap(final List<String> listData, final String idTree) {

    final Item item = new Item()
        .withPrimaryKey(
            "idNumber", idTree);
    item.withString("idType", "tree");
    item.withList("dataTree", listData);

    return item;
  }
}
