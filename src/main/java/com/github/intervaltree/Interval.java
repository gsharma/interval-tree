package com.github.intervaltree;

/**
 * This object represents a 1-dimensional immutable interval.
 * 
 * @author gaurav
 */
public final class Interval {
  private final int low;
  private final int high;

  // let the tree validate intervals
  public Interval(final int low, final int high) {
    this.low = low;
    this.high = high;
  }

  public int getLow() {
    return low;
  }

  public int getHigh() {
    return high;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Interval[low:");
    builder.append(low);
    builder.append(",high:");
    builder.append(high);
    builder.append("]");
    return builder.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + high;
    result = prime * result + low;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Interval)) {
      return false;
    }
    Interval other = (Interval) obj;
    if (high != other.high) {
      return false;
    }
    if (low != other.low) {
      return false;
    }
    return true;
  }
}
