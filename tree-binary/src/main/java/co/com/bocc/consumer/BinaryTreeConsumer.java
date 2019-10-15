package co.com.bocc.consumer;

import co.com.bocc.model.BinaryTreeModel;
import co.com.bocc.model.BinaryTreeNode;
import co.com.bocc.model.BinaryTreeResponse;
import java.util.Map;

public interface BinaryTreeConsumer {

  BinaryTreeResponse binaryTree(BinaryTreeModel binaryTreeModel,
      Map<String, Object> headers);

  BinaryTreeResponse lowestCommonAncestor(BinaryTreeNode binaryTree, BinaryTreeNode nodeOne,
      BinaryTreeNode nodeTwo);

}
