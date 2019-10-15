package co.com.bocc.validator;

import co.com.adl.commons.errors.validators.ValidatorUtil;
import co.com.bocc.model.BinaryTreeModel;
import io.vavr.collection.List;
import java.util.Optional;

public final class BinaryTreeValidator {


  public static List<Optional<String>> validate(final BinaryTreeModel request) {

    return List.of(
        ValidatorUtil.listIsNotNullOrEmpty
            .validate(request.getTreeData())
            .getFieldNameIfInvalid(
                "The list nodes must contains more than one element"));
  }
}
