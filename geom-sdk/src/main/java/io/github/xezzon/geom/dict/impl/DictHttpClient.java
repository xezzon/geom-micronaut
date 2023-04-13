package io.github.xezzon.geom.dict.impl;

import com.dtflys.forest.Forest;
import io.github.xezzon.geom.core.ServiceResolver;
import io.github.xezzon.geom.dict.DictClient;
import io.github.xezzon.geom.dict.DictDTO;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author xezzon
 */
@Service
public class DictHttpClient implements DictClient {

  private final transient ServiceResolver resolver;

  public DictHttpClient(ServiceResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public List<DictDTO> dictListByTag(String tag) {
    return Forest
        .config()
        .get(url("/" + tag))
        .executeAsList();
  }

  @Override
  public DictDTO dictByTagAndCode(String tag, String code) {
    return Forest
        .config()
        .get(url(String.format("/%s/%s", tag, code)))
        .execute(DictDTO.class);
  }

  private String url(String uri) {
    return this.resolver.adminUrl() + "/rpc/dict" + uri;
  }
}
