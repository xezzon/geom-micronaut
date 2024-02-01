package io.github.xezzon.geom.trait;

@Deprecated
public interface IConverter<S, T> {

  T convert(S source);
}
