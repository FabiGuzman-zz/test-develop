package co.com.bocc.services;

import co.com.bocc.model.BinaryTreeModel;
import co.com.bocc.model.BinaryTreeResponse;
import java.util.Map;

public interface BinaryTreeService {

  BinaryTreeResponse binaryTree(BinaryTreeModel request, Map<String, Object> headers);

  BinaryTreeResponse lowestCommonAncestor(BinaryTreeModel request, String idTree);

}
