package com.github.intervaltree;

import java.util.List;

/**
 * Framework for building an interval tree with 1-dimension intervals. Also, baked in the assumption
 * of non-degeneracy implying that no two intervals have identical {low, high} tuple.
 * 
 * @author gaurav
 */
public interface IntervalTree {
  boolean insert(final Interval interval);

  // Delete an interval, if it exists, from the tree.
  boolean delete(final Interval interval);

  // Determine if this interval exists in the tree.
  boolean findExactMatch(final Interval interval);

  // Find all overlapping intervals for this interval.
  List<Interval> findOverlaps(final Interval interval);

  // Get total node count in the tree.
  int nodeCount();

  // Get the root node of this tree
  IntervalTreeNode getRoot();

  // Get number of levels in the tree
  int levelCount();

  // Print tree level-ordered.
  String printTree();

  public final static class IntervalTreeBuilder {
    public static IntervalTreeBuilder newBuilder() {
      return new IntervalTreeBuilder();
    }

    public IntervalTree build() {
      return new IntervalTreeImpl();
    }

    private IntervalTreeBuilder() {}
  }
}
