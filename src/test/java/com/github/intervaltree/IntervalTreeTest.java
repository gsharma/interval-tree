package com.github.intervaltree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for ensuring IntervalTree implementations stay honest and correct.
 * 
 * @author gaurav
 */
public class IntervalTreeTest {
  {
    System.setProperty("log4j.configurationFile", "log4j.properties");
  }

  @Test
  public void testTree() {
    // construct and seed a tree
    final IntervalTree tree = IntervalTree.IntervalTreeBuilder.newBuilder().build();
    Interval interval1 = new Interval(17, 19);
    tree.insert(interval1);
    Interval interval2 = new Interval(21, 24);
    tree.insert(interval2);
    Interval interval3 = new Interval(5, 8);
    tree.insert(interval3);
    Interval interval4 = new Interval(4, 8);
    tree.insert(interval4);
    Interval interval5 = new Interval(15, 18);
    tree.insert(interval5);
    Interval interval6 = new Interval(7, 10);
    tree.insert(interval6);
    Interval interval7 = new Interval(16, 22);
    tree.insert(interval7);

    // validate if node and level count is accurate
    assertEquals(7, tree.nodeCount());
    assertEquals(4, tree.levelCount());

    tree.printTree();

    // walk down the root and tally where children should be
    // validate 1st level
    assertEquals(interval1, tree.getRoot().getInterval());

    // validate 2nd level
    assertEquals(interval2, tree.getRoot().getRightChild().getInterval());
    assertEquals(interval3, tree.getRoot().getLeftChild().getInterval());

    // validate 3rd level
    assertNull(tree.getRoot().getRightChild().getLeftChild());
    assertNull(tree.getRoot().getRightChild().getRightChild());
    assertEquals(interval4, tree.getRoot().getLeftChild().getLeftChild().getInterval());
    assertEquals(interval5, tree.getRoot().getLeftChild().getRightChild().getInterval());

    // validate 4th level
    assertNull(tree.getRoot().getLeftChild().getLeftChild().getLeftChild());
    assertNull(tree.getRoot().getLeftChild().getLeftChild().getRightChild());
    assertEquals(interval6,
        tree.getRoot().getLeftChild().getRightChild().getLeftChild().getInterval());
    assertEquals(interval7,
        tree.getRoot().getLeftChild().getRightChild().getRightChild().getInterval());

    // test exact match search works
    assertTrue(tree.findExactMatch(interval1));
    assertTrue(tree.findExactMatch(interval2));
    assertTrue(tree.findExactMatch(interval3));
    assertTrue(tree.findExactMatch(interval4));
    assertTrue(tree.findExactMatch(interval5));
    assertTrue(tree.findExactMatch(interval6));
    assertTrue(tree.findExactMatch(interval7));

    // test overlaps
    assertEquals(0, tree.findOverlaps(new Interval(25, 26)).size());
    assertEquals(1, tree.findOverlaps(new Interval(24, 26)).size());
    assertEquals(3, tree.findOverlaps(new Interval(3, 10)).size());
    assertEquals(5, tree.findOverlaps(new Interval(10, 25)).size());
    assertEquals(7, tree.findOverlaps(new Interval(3, 25)).size());
  }

}
