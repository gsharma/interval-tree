package com.github.intervaltree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * An Interval Tree for storing and working with 1-Dimension intervals.
 *
 * notes:<br>
 * 1. this implementation is presently thread-unsafe<br>
 * 2. can just use Red-Black BST for O(logN) operations except find-all intervals<br>
 * 
 * @author gaurav
 */
final class IntervalTreeImpl implements IntervalTree {
  private static final Logger logger = LogManager.getLogger(IntervalTreeImpl.class.getSimpleName());

  private IntervalTreeNode root;

  @Override
  public void insert(final Interval interval) {
    validateInterval(interval);
    root = insert(root, interval);
  }

  private IntervalTreeNode insert(IntervalTreeNode where, final Interval interval) {
    if (where == null) {
      where = new IntervalTreeNode(interval);
      where.setMaxInSubtree(interval.getHigh());
    } else {
      if (interval.getLow() < where.getInterval().getLow()) {
        where.setLeftChild(insert(where.getLeftChild(), interval));
      } else {
        where.setRightChild(insert(where.getRightChild(), interval));
      }
      if (where.getMaxInSubtree() < interval.getHigh()) {
        where.setMaxInSubtree(interval.getHigh());
      }
    }
    return where;
  }

  @Override
  public boolean delete(final Interval interval) {
    validateInterval(interval);
    // TODO
    return false;
  }

  // compute lazily, don't pay upfront cost
  @Override
  public int nodeCount() {
    return nodeCount(root);
  }

  private int nodeCount(final IntervalTreeNode current) {
    if (current == null) {
      return 0;
    }
    return 1 + nodeCount(current.getLeftChild()) + nodeCount(current.getRightChild());
  }

  @Override
  public boolean findExactMatch(final Interval whatToFind) {
    validateInterval(whatToFind);
    // execute bfs to search an interval in the tree
    boolean found = false;
    if (whatToFind != null && root != null) {
      final LinkedList<IntervalTreeNode> queue = new LinkedList<>();
      queue.offerLast(root);
      while (!queue.isEmpty()) {
        final IntervalTreeNode current = queue.removeLast();
        if (whatToFind.equals(current.getInterval())) {
          found = true;
          break;
        }
        if (current.getLeftChild() != null) {
          queue.offerLast(current.getLeftChild());
        }
        if (current.getRightChild() != null) {
          queue.offerLast(current.getRightChild());
        }
      }
    }
    return found;
  }

  /**
   * To search for any one interval that overlaps with query interval:<br>
   * 1. if interval in node overlaps query interval, return it<br>
   * 2. else-if left subtree is null, go right<br>
   * 3. else-if max in left subtree is less than interval.low, go right<br>
   * 4. else go left<br>
   */
  @Override
  public List<Interval> findOverlaps(final Interval interval) {
    validateInterval(interval);
    final List<Interval> overlaps = new ArrayList<>();
    findOverlaps(interval, root, overlaps);
    logger
        .info(String.format("Scanned tree, found %d overlaps with %s, Overlaps:%s", overlaps.size(),
            interval, Arrays.deepToString(overlaps.toArray(new Interval[overlaps.size()]))));
    return Collections.unmodifiableList(overlaps);
  }

  private void findOverlaps(final Interval interval, final IntervalTreeNode current,
      final List<Interval> overlaps) {
    if (current == null) {
      return;
    }
    // 1. if interval in node overlaps query interval, return it
    if (determineOverlap(interval, current.getInterval())) {
      overlaps.add(current.getInterval());
      // great, found and added an overlap, keep scanning for more
      findOverlaps(interval, current.getLeftChild(), overlaps);
      findOverlaps(interval, current.getRightChild(), overlaps);
    }
    // 2. else-if left subtree is null, go right
    else if (current.getLeftChild() == null) {
      findOverlaps(interval, current.getRightChild(), overlaps);
    }
    // 3. else-if max in left subtree is less than interval.low, go right
    else if (current.getLeftChild().getMaxInSubtree() < interval.getLow()) {
      findOverlaps(interval, current.getRightChild(), overlaps);
    }
    // 4. else go left
    else {
      findOverlaps(interval, current.getLeftChild(), overlaps);
    }
  }

  @Override
  public IntervalTreeNode getRoot() {
    return root;
  }

  @Override
  public String printTree() {
    final StringBuilder builder = new StringBuilder("Printing Interval Tree (level ordered)...\n");
    builder.append("nodes:").append(nodeCount()).append(", levels:").append(levelCount())
        .append("\n");
    final LinkedList<IntervalTreeNode> queue = new LinkedList<>();
    if (root != null) {
      queue.offerLast(root);
    }
    while (!queue.isEmpty()) {
      IntervalTreeNode current = queue.pollFirst();
      builder.append(current.printNode()).append("\n");
      if (current.getLeftChild() != null) {
        queue.offerLast(current.getLeftChild());
      }
      if (current.getRightChild() != null) {
        queue.offerLast(current.getRightChild());
      }
    }
    final String levelOrderedTree = builder.toString();
    logger.info(levelOrderedTree);
    return levelOrderedTree;
  }

  // compute lazily, don't pay upfront cost
  @Override
  public int levelCount() {
    // dfs it to work around the accounting hassle and upfront cost associated with having to
    // maintain and continuously update a global levels variable
    return levelCount(root);
  }

  private int levelCount(final IntervalTreeNode current) {
    if (current == null) {
      return 0;
    }
    return 1 + Math.max(levelCount(current.getLeftChild()), levelCount(current.getRightChild()));
  }

  // validate if the 1-d interval is not a point and its {left,right} tuple are both non-negative
  // and satisfy right>left condition
  private static boolean validateInterval(final Interval interval) {
    return interval != null && interval.getLow() < interval.getHigh() && interval.getLow() >= 0
        && interval.getHigh() > 0;
  }

  // check if two 1-d intervals overlap with each other
  private static boolean determineOverlap(final Interval one, final Interval two) {
    boolean overlap = false;
    if (one != null && two != null) {
      // 1. one and two are same length and exactly overlap
      if ((one.getLow() == two.getLow() && one.getHigh() == two.getHigh()) ||
      // 2. one is longer than two but they overlap. note that 2 is a superset of 1, explicitly
      // called out here just for clarity
          (one.getLow() <= two.getLow() && one.getHigh() >= two.getHigh()) ||
          // 3. one is shorter than two but they overlap
          (one.getLow() >= two.getLow() && one.getHigh() <= two.getHigh()) ||
          // 4. one is to the left of two but they overlap
          (one.getHigh() >= two.getLow() && one.getHigh() <= two.getHigh()) ||
          // 5. one is to the right of two but they overlap
          (one.getLow() >= two.getLow() && one.getLow() <= two.getHigh())) {
        overlap = true;
      }
    }
    if (logger.isDebugEnabled()) {
      logger.debug("Overlap:" + overlap + ":: " + one + ", " + two);
    }
    return overlap;
  }

  IntervalTreeImpl() {}

}
