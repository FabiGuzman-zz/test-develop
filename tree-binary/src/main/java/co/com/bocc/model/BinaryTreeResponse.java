package co.com.bocc.model;

import co.com.adl.commons.errors.dto.MessageCode;
import co.com.adl.commons.errors.dto.Response;
import java.util.List;

public class BinaryTreeResponse extends Response {

  public List<String> treeData;

  public BinaryTreeResponse() {
  }

  public BinaryTreeResponse(final MessageCode messageCode) {
    super(messageCode);
  }


  public List<String> getTreeData() {
    return treeData;
  }

  public void setTreeData(final List<String> treeData) {
    this.treeData = treeData;
  }
}
