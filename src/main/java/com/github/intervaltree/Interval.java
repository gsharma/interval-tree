package com.github.intervaltree;

/**
 * This object represents a 1-dimensional immutable interval.
 * 
 * Note that I deliberately didn't go down the path to have an interval typed like <? extends Number
 * & Comparable, ? extends Number & Comparable, Object>. Number is a super for even mutable numeric
 * types and it was historically intentionally not made to extend Comparable - so, no point in going
 * down the crazy rabbit-hole of using BigInteger, etc to muscle all Number child types to provide a
 * somewhat warped implementation for Comparable's compareTo().
 * 
 * @author gaurav
 */
public final class Interval {
  private final int low;
  private final int high;
  private final Object data;

  // let the tree validate intervals
  public Interval(final int low, final int high, final Object data) {
    this.low = low;
    this.high = high;
    this.data = data;
  }

  public int getLow() {
    return low;
  }

  public int getHigh() {
    return high;
  }

  public Object getData() {
    return data;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Interval[low:");
    builder.append(low);
    builder.append(",high:");
    builder.append(high);
    builder.append(",data:");
    builder.append(data);
    builder.append("]");
    return builder.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((data == null) ? 0 : data.hashCode());
    result = prime * result + high;
    result = prime * result + low;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || !(obj instanceof Interval)) {
      return false;
    }
    Interval other = (Interval) obj;
    if (data == null || other.data != null) {
      return false;
    } else if (!data.equals(other.data)) {
      return false;
    }
    if (high != other.high) {
      return false;
    }
    if (low != other.low) {
      return false;
    }
    return true;
  }


}
