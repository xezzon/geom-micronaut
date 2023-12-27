package io.github.xezzon.geom.domain;

/**
 * @author xezzon
 */
public record Id(String id) {

  public static Id of(String id) {
    return new Id(id);
  }
}
