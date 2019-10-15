package co.com.bocc.validator;

import co.com.bocc.model.BinaryTreeModel;
import io.vavr.Tuple2;
import java.util.Map;

public interface RequestValidator {

  Tuple2<BinaryTreeModel, Map<String, Object>>
  validateRequest(
      BinaryTreeModel request, Map<String, Object> headers);

}
