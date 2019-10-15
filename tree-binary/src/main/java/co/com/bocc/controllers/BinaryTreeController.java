package co.com.bocc.controllers;

import co.adl.commons.rest.JsonUtil.JsonUtilBuilder;
import co.adl.commons.util.GenericBuilder;
import co.com.adl.commons.errors.dto.ErrorLayer;
import co.com.adl.commons.errors.dto.MessageCode;
import co.com.adl.commons.errors.dto.Response;
import co.com.adl.commons.errors.dto.Status;
import co.com.bocc.config.BinaryTreeConfig;
import co.com.bocc.model.BinaryTreeModel;
import co.com.bocc.services.BinaryTreeService;
import com.adl.bc.common.controller.lambda.ProxyRequest;
import com.adl.bc.common.controller.lambda.ProxyResponse;
import com.adl.bc.common.controller.lambda.ProxyResponseBuilder;
import com.adl.bc.common.util.GuiceApplication.ApplicationBuilder;
import com.adl.bc.common.util.ResponseWrapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.Tuple;
import io.vavr.control.Try;
import java.util.Arrays;
import java.util.Objects;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BinaryTreeController implements RequestHandler<ProxyRequest, ProxyResponse> {

  private static final Logger LOGGER = LogManager.getLogger(BinaryTreeController.class);
  private final String keyIdTree = "X-TreeId";
  private BinaryTreeService service;

  @Override
  public ProxyResponse handleRequest(final ProxyRequest proxyRequest, final Context context) {

    final ObjectMapper objectMapper = new ObjectMapper();

    final Try<BinaryTreeModel> treeModel = Try
        .of(() -> objectMapper.readValue(proxyRequest.getBodyString(), BinaryTreeModel.class));

    LOGGER
        .info("Init processing binary tree service. proxyRequest is : {} ",
            proxyRequest.getBodyString());

    return treeModel.map(mapper -> mapper)
        .peek(treeModelRequest -> LOGGER.info("treeModel request :{}",
            JsonUtilBuilder.create(treeModelRequest).build()))
        .map(treeModelRequest -> Tuple.of(treeModelRequest, proxyRequest.getHeaders()))
        .map(tuple -> {
          if (tuple._1.getTreeData().size() == 2) {
            return getService()
                .lowestCommonAncestor(tuple._1, String.valueOf(tuple._2.get(keyIdTree)));
          } else {
            return getService().binaryTree(tuple._1, tuple._2);
          }
        })
        .map(response -> {

          return ProxyResponseBuilder.create()
              .statusCode(
                  response.getCode().compareTo("0") == 0 ? 200 : 500)
              .isBase64Encoded(false)
              .body(response)
              .build();

        }).getOrElseGet(error -> ResponseWrapper.withStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
            .withResponse(GenericBuilder.of(() -> new Response(MessageCode.UNEXPECTED))
                .with(Response::setDetails, Arrays.asList(GenericBuilder.of(Status::new)
                    .with(Status::setCode, MessageCode.UNEXPECTED.getCode())
                    .with(Status::setMessage, error.getMessage())
                    .with(Status::setErrorLayer, ErrorLayer.SERVER)
                    .build()
                ))
                .build())
            .build());
  }


  public BinaryTreeService getService() {
    return Objects.isNull(service) ? ApplicationBuilder.create(BinaryTreeService.class)
        .modules(new BinaryTreeConfig())
        .build()
        .getBean() : service;
  }
}