package io.github.xezzon.geom.trait;

public interface IConverter<S, T> {

  T convert(S source);
}
