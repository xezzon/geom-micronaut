package io.github.xezzon.geom.crypto;

import io.github.xezzon.geom.crypto.service.KeyLoader;
import io.github.xezzon.tao.exception.ServerException;
import io.micronaut.context.annotation.Bean;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

/**
 * 读/写 PEM 文件中的公钥/私钥
 */
@Bean
public class FileKeyLoader implements KeyLoader {

  @Override
  public byte[] read(String id) {
    try (FileReader fileReader = new FileReader(getResourceFile(id))) {
      PemReader pemReader = new PemReader(fileReader);
      PemObject pemObject = pemReader.readPemObject();
      return pemObject.getContent();
    } catch (IOException e) {
      throw new ServerException("读取密钥失败：" + id, e);
    }
  }

  @Override
  public void write(String id, byte[] content, String header) {
    try (FileWriter fileWriter = new FileWriter(getResourceFile(id))) {
      PemWriter pemWriter = new PemWriter(fileWriter);
      pemWriter.writeObject(new PemObject(header, content));
    } catch (IOException e) {
      throw new ServerException("写入密钥文件失败：" + id, e);
    }
  }

  private File getResourceFile(String id) {
    try {
      URL classpath = getClass().getClassLoader().getResource("");
      assert classpath != null;
      Path privateKeyPath = Path.of(id);
      return Path.of(classpath.toURI())
          .resolve(privateKeyPath)
          .toFile();
    } catch (URISyntaxException e) {
      throw new ServerException("获取不到有效的 classpath", e);
    }
  }
}
