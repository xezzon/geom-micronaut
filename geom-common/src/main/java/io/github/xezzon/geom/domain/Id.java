package io.github.xezzon.geom.domain;

/**
 * 将ID封装成对象
 * @author xezzon
 */
public record Id(String id) {

  public static Id of(String id) {
    return new Id(id);
  }
}
