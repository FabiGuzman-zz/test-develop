package co.com.bocc.services;

import co.com.bocc.consumer.BinaryTreeConsumer;
import co.com.bocc.mapper.CustomerTreeMapper;
import co.com.bocc.model.BinaryTreeModel;
import co.com.bocc.model.BinaryTreeNode;
import co.com.bocc.model.BinaryTreeResponse;
import co.com.bocc.repositories.TreeLoaderRepository;
import co.com.bocc.validator.RequestValidator;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import io.vavr.Tuple2;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BinaryTreeServiceImpl implements BinaryTreeService {

  private static final Logger LOGGER = LogManager.getLogger(BinaryTreeServiceImpl.class);

  private final String keyIdTree = "X-TreeId";
  private RequestValidator requestValidator;
  private BinaryTreeConsumer consumer;
  private CustomerTreeMapper customerTreeMapper;
  private TreeLoaderRepository treeLoaderRepository;

  @Inject
  public BinaryTreeServiceImpl(
      final RequestValidator requestValidator,
      final BinaryTreeConsumer consumer, final TreeLoaderRepository treeLoaderRepository,
      final CustomerTreeMapper customerTreeMapper) {
    this.consumer = consumer;
    this.requestValidator = requestValidator;
    this.treeLoaderRepository = treeLoaderRepository;
    this.customerTreeMapper = customerTreeMapper;
  }

  @Override
  public BinaryTreeResponse binaryTree(final BinaryTreeModel request,
      final Map<String, Object> headers) {

    final Tuple2<BinaryTreeModel, Map<String, Object>> tuple2 = requestValidator
        .validateRequest(request, headers);

    saveTree(request.getTreeData(), String.valueOf(headers.get(keyIdTree)));

    return consumer.binaryTree(tuple2._1, tuple2._2);
  }

  @Override
  public BinaryTreeResponse lowestCommonAncestor(final BinaryTreeModel request,
      final String idTree) {

    final ObjectMapper objectMapper = new ObjectMapper();
    BinaryTreeResponse binaryTreeResponse = null;

    try {
      final Item item = treeLoaderRepository.getItem(idTree);
      final List<String> dataTree = item.getList("dataTree");

      final BinaryTreeNode<Integer> binaryTree = objectMapper
          .readValue(dataTree.toString(),
              new TypeReference<BinaryTreeNode<Integer>>() {
              });

      final BinaryTreeNode<Integer> nodeOne = new BinaryTreeNode<Integer>(
          Integer.parseInt(request.getTreeData().get(0)));

      final BinaryTreeNode<Integer> nodeTwo = new BinaryTreeNode<Integer>(
          Integer.parseInt(request.getTreeData().get(1)));

      binaryTreeResponse = consumer
          .lowestCommonAncestor(binaryTree, nodeOne, nodeTwo);

    } catch (IOException ioException) {
      LOGGER.info("Error parsing data of tree binary {} :", ioException);
    }

    return binaryTreeResponse;
  }


  public void saveTree(final List<String> dataTree, final String keyIdTree) {

    final Item item = customerTreeMapper.toMap(dataTree, keyIdTree);
    treeLoaderRepository.saveItems(item);
  }

}
