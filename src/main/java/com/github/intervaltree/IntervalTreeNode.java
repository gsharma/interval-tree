package com.github.intervaltree;

/**
 * Represents a node in a 1-dimensional interval tree.
 * 
 * @author gaurav
 */
public final class IntervalTreeNode {
  // immutable data
  private final Interval interval;

  // computed, modifiable value
  private int maxInSubtree;

  // can change as nodes are added and removed from the tree. We're going to create a binary tree.
  private IntervalTreeNode leftChild, rightChild;

  public IntervalTreeNode(final Interval interval) {
    this.interval = interval;
  }

  public int getMaxInSubtree() {
    return maxInSubtree;
  }

  public void setMaxInSubtree(int maxInSubtree) {
    this.maxInSubtree = maxInSubtree;
  }

  public IntervalTreeNode getLeftChild() {
    return leftChild;
  }

  public IntervalTreeNode getRightChild() {
    return rightChild;
  }

  public void setLeftChild(final IntervalTreeNode leftChild) {
    this.leftChild = leftChild;
  }

  public void setRightChild(final IntervalTreeNode rightChild) {
    this.rightChild = rightChild;
  }

  public Interval getInterval() {
    return interval;
  }

  // prints the node and its immediate children
  public String printNode() {
    StringBuilder builder = new StringBuilder();
    builder.append("current:{").append(interval).append("[maxInSubtree:").append(maxInSubtree)
        .append(']').append("}, ");
    if (leftChild != null) {
      builder.append("leftChild:{").append(leftChild.getInterval()).append("[maxInSubtree:")
          .append(leftChild.getMaxInSubtree()).append(']').append("}, ");
    } else {
      builder.append("leftChild:{null}, ");
    }
    if (rightChild != null) {
      builder.append("rightChild:{").append(rightChild.getInterval()).append("[maxInSubtree:")
          .append(rightChild.getMaxInSubtree()).append(']').append('}');
    } else {
      builder.append("rightChild:{null}");
    }
    return builder.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((interval == null) ? 0 : interval.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || !(obj instanceof IntervalTreeNode)) {
      return false;
    }
    IntervalTreeNode other = (IntervalTreeNode) obj;
    if (interval == null || other.interval != null) {
      return false;
    } else if (!interval.equals(other.interval)) {
      return false;
    }
    return true;
  }


}
