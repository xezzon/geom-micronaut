package io.github.xezzon.geom.openapi;

import io.github.xezzon.geom.exception.NonexistentDataException;
import io.github.xezzon.geom.exception.RepeatDataException;
import io.github.xezzon.geom.openapi.exception.OpenapiPublishedException;
import io.github.xezzon.geom.openapi.exception.OpenapiUnpublishedException;
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

  private final OpenapiDAO openapiDAO;
  private final OpenapiInstanceDAO openapiInstanceDAO;

  public OpenapiService(OpenapiDAO openapiDAO, OpenapiInstanceDAO openapiInstanceDAO) {
    this.openapiDAO = openapiDAO;
    this.openapiInstanceDAO = openapiInstanceDAO;
  }

  /**
   * 添加OpenAPI文档
   * @param openapi 要添加的OpenAPI文档对象
   */
  protected void addOpenapi(Openapi openapi) {
    /* 前置校验 */
    // 重复性校验
    checkRepeat(openapi);
    /* 持久化 */
    openapiDAO.get().save(openapi);
  }

  /**
   * 查询openapi分页信息
   * @param commonQuery 查询条件
   * @return 分页对象，包含openapi信息
   */
  protected Page<Openapi> queryOpenapiPage(CommonQuery commonQuery) {
    return openapiDAO.query(commonQuery);
  }

  /**
   * 修改OpenAPI接口信息
   * @param openapi 要修改的OpenAPI接口对象
   * @throws NonexistentDataException 如果要修改的OpenAPI接口不存在，则抛出此异常
   * @throws OpenapiPublishedException 如果要修改的OpenAPI接口已经发布，则抛出此异常
   */
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

  /**
   * 从数据库中删除指定ID的OpenAPI接口
   * @param id 要删除的OpenAPI接口ID
   * @throws NonexistentDataException 如果指定ID的OpenAPI接口不存在，则抛出此异常
   * @throws OpenapiPublishedException 如果指定ID的OpenAPI接口已发布，则抛出此异常
   */
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
   * 订阅OpenAPI实例
   * @param openapiInstance OpenAPI实例对象
   */
  protected void subscribeOpenapi(OpenapiInstance openapiInstance) {
    /* 前置校验 */
    checkRepeat(openapiInstance);
    /* 持久化 */
    openapiInstanceDAO.get().save(openapiInstance);
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

  /**
   * 检查OpenAPI实例是否重复
   * @param openapiInstance OpenAPI实例
   * @throws NonexistentDataException 如果OpenAPI实例所属的接口不存在
   * @throws OpenapiUnpublishedException 如果OpenAPI实例所属的接口未发布
   * @throws RepeatDataException 如果OpenAPI实例所属的接口已被其他用户订阅
   */
  private void checkRepeat(OpenapiInstance openapiInstance) {
    Optional<Openapi> openapi = openapiDAO.get().findById(openapiInstance.getApiId());
    if (openapi.isEmpty()) {
      throw new NonexistentDataException("接口不存在");
    }
    if (!openapi.get().isPublished()) {
      throw new OpenapiUnpublishedException();
    }
    Optional<OpenapiInstance> exist = openapiInstanceDAO.get()
        .findByApiIdAndOwnerId(openapiInstance.getApiId(), openapiInstance.getOwnerId());
    if (exist.isPresent() && !Objects.equals(exist.get().getId(), openapiInstance.getId())) {
      throw new RepeatDataException("接口`" + openapi.get().getCode() + "`已被订阅");
    }
  }
}
