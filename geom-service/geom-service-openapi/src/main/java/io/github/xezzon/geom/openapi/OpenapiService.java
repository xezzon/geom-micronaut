package io.github.xezzon.geom.openapi;

import io.github.xezzon.tao.exception.ClientException;
import io.github.xezzon.tao.retrieval.CommonQuery;
import io.micronaut.data.model.Page;
import jakarta.inject.Singleton;
import java.util.Objects;
import java.util.Optional;

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
    Openapi exist = checkRepeat(openapi);
    // 只允许更新未发布的接口
    if (exist.isPublished()) {
      throw new ClientException("不允许修改已发布的接口");
    }
    /* 持久化 */
    openapiDAO.get().update(openapi);
  }

  protected void removeOpenapi(String id) {
    /* 前置校验 */
    // 只允许删除未发布的接口
    Optional<Openapi> exist = openapiDAO.get().findById(id);
    if (exist.isEmpty()) {
      throw new ClientException("接口不存在或已删除");
    }
    if (exist.get().isPublished()) {
      throw new ClientException("不允许删除已发布的接口");
    }
    /* 持久化 */
    openapiDAO.get().deleteById(id);
  }

  /**
   * 查询是否有重复的内容
   * @param openapi 请求参数
   */
  private Openapi checkRepeat(Openapi openapi) {
    Openapi exist = openapiDAO.get().findByCode(openapi.getCode());
    if (exist != null && !Objects.equals(exist.getId(), openapi.getId())) {
      throw new ClientException("接口`" + openapi.getCode() + "`已存在");
    }
    return exist;
  }
}
