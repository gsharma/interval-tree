[![Licence](https://img.shields.io/hexpm/l/plug.svg)](https://github.com/gsharma/interval-tree/blob/master/LICENSE)

# Interval Tree

Interval tree is a tree designed to hold intervals with the goal of quickly finding overlapping intervals (with any given interval or point). It is typically used for windowing queries.

## Interval Tree API

### Insert an interval into the tree
void insert(final Interval interval);

### Delete an interval, if it exists, from the tree
boolean delete(final Interval interval);

### Determine if this interval exists in the tree
boolean findExactMatch(final Interval interval);

### Find all overlapping intervals for this interval
List<Interval> findOverlaps(final Interval interval);

### Get total node count in the tree
int nodeCount();

### Get the root node of this tree
IntervalTreeNode getRoot();

### Get number of levels in the tree
int levelCount();

### Print tree level-ordered
String printTree();

