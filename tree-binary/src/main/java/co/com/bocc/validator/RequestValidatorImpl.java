package co.com.bocc.validator;

import co.adl.commons.rest.JsonUtil;
import co.adl.commons.rest.JsonUtil.JsonUtilBuilder;
import co.com.adl.commons.errors.validators.DTOValidator;
import co.com.adl.commons.errors.validators.MapValidator.MapValidatorBuilder;
import co.com.bocc.model.BinaryTreeModel;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestValidatorImpl implements RequestValidator {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(RequestValidatorImpl.class);

  @Override
  public Tuple2<BinaryTreeModel, Map<String, Object>> validateRequest(
      final BinaryTreeModel request, final Map<String, Object> headers) {

    LOGGER.debug("Request {}", JsonUtil.JsonUtilBuilder.create(request).build());

    final BinaryTreeModel binaryTreeModel = JsonUtilBuilder
        .create(BinaryTreeModel.class)
        .fromJson(JsonUtil.JsonUtilBuilder.create(request).build().toString())
        .build()
        .get();

    final BinaryTreeModel requestValidated =
        (BinaryTreeModel) DTOValidator.build(binaryTreeModel)
            .validate(BinaryTreeValidator::validate).get();

    final Map<String, Object> headersValidated = ((Map<String, Object>) MapValidatorBuilder
        .create(headers)
        .addMandatory("X-TreeId", "The mandatory X-TreeId header was not found in the request")
        .build()
        .getMap());

    return Tuple.of(requestValidated, headersValidated);

  }
}
