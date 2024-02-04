package io.github.xezzon.geom.openapi;

import io.github.xezzon.geom.exception.NonexistentDataException;
import io.github.xezzon.geom.exception.RepeatDataException;
import io.github.xezzon.geom.openapi.exception.OpenapiPublishedException;
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

  private final transient OpenapiDAO openapiDAO;
  private final transient OpenapiInstanceDAO openapiInstanceDAO;

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
    // 数据不存在
    Optional<Openapi> exist = openapiDAO.get().findById(openapi.getId());
    if (exist.isEmpty()) {
      throw new NonexistentDataException();
    }
    // 只允许更新未发布的接口
    if (exist.get().isPublished()) {
      throw new OpenapiPublishedException("不允许修改已发布的接口");
    }
    /* 持久化 */
    openapiDAO.update(openapi);
  }

  protected void removeOpenapi(String id) {
    /* 前置校验 */
    // 只允许删除未发布的接口
    Optional<Openapi> exist = openapiDAO.get().findById(id);
    // 数据不存在
    if (exist.isEmpty()) {
      throw new NonexistentDataException();
    }
    // 只允许删除未发布的接口
    if (exist.get().isPublished()) {
      throw new OpenapiPublishedException("不允许删除已发布的接口");
    }
    /* 持久化 */
    openapiDAO.get().deleteById(id);
  }

  /**
   * 查询是否有重复的内容
   * @param openapi 请求参数
   */
  private void checkRepeat(Openapi openapi) {
    if (openapi.getCode() == null) {
      return;
    }
    Optional<Openapi> exist = openapiDAO.get().findByCode(openapi.getCode());
    if (exist.isPresent() && !Objects.equals(exist.get().getId(), openapi.getId())) {
      throw new RepeatDataException("接口`" + openapi.getCode() + "`已存在");
    }
  }
}
