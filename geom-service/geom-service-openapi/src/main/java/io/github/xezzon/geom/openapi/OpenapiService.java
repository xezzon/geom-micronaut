package io.github.xezzon.geom.openapi;

import io.github.xezzon.tao.exception.ClientException;
import io.github.xezzon.tao.retrieval.CommonQuery;
import io.micronaut.data.model.Page;
import jakarta.inject.Singleton;
import java.util.Objects;

/**
 * @author xezzon
 */
@Singleton
public class OpenapiService {

  protected final transient OpenapiDAO openapiDAO;
  protected final transient OpenapiInstanceDAO openapiInstanceDAO;

  public OpenapiService(OpenapiDAO openapiDAO, OpenapiInstanceDAO openapiInstanceDAO) {
    this.openapiDAO = openapiDAO;
    this.openapiInstanceDAO = openapiInstanceDAO;
  }

  protected void addOpenapi(Openapi openapi) {
    /* 前置校验 */
    // 重复性校验
    checkRepeat(openapi);
    /* 持久化 */
    openapiDAO.get().save(openapi);
  }

  protected Page<Openapi> queryOpenapiPage(CommonQuery commonQuery) {
    return openapiDAO.query(commonQuery);
  }

  protected void modifyOpenapi(Openapi openapi) {
    /* 前置校验 */
    // 重复性校验
    checkRepeat(openapi);
    /* 持久化 */
    openapiDAO.get().update(openapi);
  }

  /**
   * 查询是否有重复的内容
   * @param openapi 请求参数
   */
  private void checkRepeat(Openapi openapi) {
    Openapi exist = openapiDAO.get().findByCode(openapi.getCode());
    if (exist != null && !Objects.equals(exist.getId(), openapi.getId())) {
      throw new ClientException("接口`" + openapi.getCode() + "`已存在");
    }
  }
}
