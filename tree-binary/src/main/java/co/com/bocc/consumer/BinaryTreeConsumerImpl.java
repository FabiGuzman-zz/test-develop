package co.com.bocc.consumer;

import co.com.adl.commons.errors.dto.MessageCode;
import co.com.bocc.model.BinaryTreeModel;
import co.com.bocc.model.BinaryTreeNode;
import co.com.bocc.model.BinaryTreeResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BinaryTreeConsumerImpl implements BinaryTreeConsumer {

  private static final Logger LOGGER = LogManager.getLogger(BinaryTreeConsumerImpl.class);

  @Override
  public BinaryTreeResponse binaryTree(
      final BinaryTreeModel request,
      final Map<String, Object> headers) {

    LOGGER.info("Initing process consumer service of binary tree");

    final BinaryTreeResponse binaryTreeResponse = new BinaryTreeResponse();
    binaryTreeResponse.setTreeData(request.getTreeData());
    binaryTreeResponse.setCode(MessageCode.SUCCESSFUL.getCode());
    binaryTreeResponse.setMessage("Binary tree was saved successfully");
    return binaryTreeResponse;

  }

  @Override
  public BinaryTreeResponse lowestCommonAncestor(
      final BinaryTreeNode binaryTree, final BinaryTreeNode nodeOne, final BinaryTreeNode nodeTwo) {

    LOGGER.info("Initing process of getting lowest common ancestor");

    final BinaryTreeNode<Integer> commonAncestor = binaryTree
        .lowestCommonAncestor(binaryTree, nodeOne, nodeTwo);
    final List<String> ancestor = new ArrayList<String>();
    ancestor.add(commonAncestor.data.toString());
    final BinaryTreeModel binaryTreeModel = new BinaryTreeModel();
    binaryTreeModel.setTreeData(ancestor);

    final BinaryTreeResponse binaryTreeResponse = new BinaryTreeResponse();
    binaryTreeResponse.setTreeData(binaryTreeModel.getTreeData());
    binaryTreeResponse.setCode(MessageCode.SUCCESSFUL.getCode());
    binaryTreeResponse.setMessage("The lowest ancestor of the nodes was found successfully");
    return binaryTreeResponse;

  }
}
