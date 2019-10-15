package co.com.bocc.model;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;

public class BinaryTreeNode<E> extends AbstractSequentialList<E>
    implements Cloneable, java.io.Serializable {

  public E data;
  public BinaryTreeNode<E> left;
  public BinaryTreeNode<E> right;

  private transient boolean leftIsNull;
  private transient boolean rightIsNull;

  public BinaryTreeNode() {
    data = null;
    left = null;
    right = null;
  }

  public BinaryTreeNode(final E value) {
    if (value == null) {
      throw new IllegalArgumentException("null value");
    }
    this.data = value;
    left = null;
    right = null;
  }

  public BinaryTreeNode(final Collection<? extends E> c) {
    this();
    addAll(c);
  }

  private static <E> void preOrder(final BinaryTreeNode<E> root, final ArrayList<E> result) {
    if (root == null) {
      return;
    }

    result.add(root.data);
    if (Objects.nonNull(root.left)) {
      preOrder(root.left, result);
    }
    if (Objects.nonNull(root.right)) {
      preOrder(root.right, result);
    }
  }

  public int size() {
    if (Objects.isNull(data)) {
      return 0;
    }

    final Queue<BinaryTreeNode<E>> queue = new LinkedList<>();
    queue.add(this);

    int count = 0;
    while (!queue.isEmpty()) {
      final BinaryTreeNode<E> node = queue.remove();
      ++count;
      if (Objects.nonNull(node.left)) {
        queue.add(node.left);
      } else {
        if (node.leftIsNull) {
          ++count;
        }
      }
      if (Objects.nonNull(node.right)) {
        queue.add(node.right);
      } else {
        if (node.rightIsNull) {
          ++count;
        }
      }
    }
    return count;
  }

  private boolean isPositionIndex(final int index) {
    return index >= 0 && index <= size();
  }

  private String outOfBoundsMsg(final int index) {
    return "Index: " + index + ", Size: " + size();
  }

  private void checkPositionIndex(final int index) {
    if (!isPositionIndex(index)) {
      throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
    }
  }

  NodeAndFather node(final int index) {
    checkPositionIndex(index);
    if (Objects.isNull(data)) {
      return null;
    }

    final Queue<NodeAndFather> queue = new LinkedList<>();
    queue.add(new NodeAndFather(this, null, false));

    for (int i = 0; !queue.isEmpty(); ++i) {
      final NodeAndFather nodeAndFather = queue.remove();
      if (i == index) {
        return nodeAndFather;
      }

      if (Objects.nonNull(nodeAndFather.node)) {
        queue.add(generateNodeAndFather(nodeAndFather.node.left, nodeAndFather.node, false));
        queue.add(generateNodeAndFather(nodeAndFather.node.right, nodeAndFather.node, true));
      }
    }
    throw new IllegalArgumentException("Illegal index: " + index);
  }

  public ListIterator<E> listIterator(final int index) {
    checkPositionIndex(index);
    return new ListItr(index);
  }

  ArrayList<E> preOrder() {
    final ArrayList<E> result = new ArrayList<>();
    if (Objects.isNull(this.data)) {
      return result;
    }
    preOrder(this, result);
    return result;
  }

  public BinaryTreeNode lowestCommonAncestor(final BinaryTreeNode root, final BinaryTreeNode a,
      final BinaryTreeNode b) {
    if (root == null) {
      return null;
    }
    if (root.data == a.data || root.data == b.data) {
      return root;
    }

    final BinaryTreeNode left = lowestCommonAncestor(root.left, a, b);
    final BinaryTreeNode right = lowestCommonAncestor(root.right, a, b);

    // If we get left and right not null , it is lca for a and b
    if (Objects.nonNull(left) && Objects.nonNull(right)) {
      return root;
    }
    if (Objects.isNull(left)) {
      return right;
    } else {
      return left;
    }

  }

  private NodeAndFather generateNodeAndFather(final BinaryTreeNode<E> node,
      final BinaryTreeNode<E> father,
      final boolean isRight) {

    return new NodeAndFather(node, father, isRight);
  }

  private class NodeAndFather {

    private BinaryTreeNode<E> node;
    private BinaryTreeNode<E> father;
    private boolean isRight;  // the father is the right child of the father

    private NodeAndFather(final BinaryTreeNode<E> node, final BinaryTreeNode<E> father,
        final boolean isRight) {
      this.node = node;
      this.father = father;
      this.isRight = isRight;
    }
  }

  private class ListItr implements ListIterator<E> {

    private NodeAndFather next;
    private int nextIndex;
    private int expectedModCount = modCount;

    ListItr(final int index) {
      assert isPositionIndex(index);
      next = node(index);
      nextIndex = index;
    }

    public boolean hasNext() {
      final BinaryTreeNode<E> cur = next.node;
      return cur != null || (next.father.leftIsNull || next.father.rightIsNull);
    }

    public E next() {
      checkForComodification();
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      final E result = Objects.nonNull(next.node) ? next.node.data : null;
      next = node(nextIndex + 1);
      nextIndex++;
      return result;
    }

    public boolean hasPrevious() {
      throw new UnsupportedOperationException();
    }

    public E previous() {
      throw new UnsupportedOperationException();
    }

    public int nextIndex() {
      throw new UnsupportedOperationException();
    }

    public int previousIndex() {
      throw new UnsupportedOperationException();
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public void set(final E e) {
      throw new UnsupportedOperationException();
    }

    public void add(final E e) {  // always append at the tail
      checkForComodification();

      if (next == null) { // empty list
        BinaryTreeNode.this.data = e;
        BinaryTreeNode.this.left = null;
        BinaryTreeNode.this.right = null;
      } else {
        final BinaryTreeNode<E> newNode = e != null ? new BinaryTreeNode<>(e) : null;
        if (next.father == null) { // root
          final BinaryTreeNode<E> cur = next.node;
          cur.left = newNode;
          assert cur.right == null;
          throw new UnsupportedOperationException();
        } else {
          if (next.isRight) {
            if (Objects.nonNull(next.father.right)) {
              throw new IllegalStateException();
            }
            next.father.right = newNode;
            if (Objects.isNull(newNode)) {
              next.father.rightIsNull = true;
            }
          } else {
            if (Objects.nonNull(next.father.left)) {
              throw new IllegalStateException();
            }
            next.father.left = newNode;
            if (Objects.isNull(newNode)) {
              next.father.leftIsNull = true;
            }

          }
        }
      }
      modCount++;
      expectedModCount++;
    }

    final void checkForComodification() {
      if (modCount != expectedModCount) {
        throw new ConcurrentModificationException();
      }
    }
  }
}