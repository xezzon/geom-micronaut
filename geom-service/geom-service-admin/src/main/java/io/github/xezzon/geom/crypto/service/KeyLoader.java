package io.github.xezzon.geom.crypto.service;

public interface KeyLoader {

  /**
   * 根据给定的id读取数据，返回对应的字节数组。
   * @param id 数据的标识符
   * @return 对应的字节数组
   */
  byte[] read(String id);

  /**
   * 将给定的字节数组写入与给定id相关联的数据源中。
   * @param id 数据的标识符
   * @param content 要写入数据的字节数组
   * @param header PEM文件头
   */
  void write(String id, byte[] content, String header);
}
