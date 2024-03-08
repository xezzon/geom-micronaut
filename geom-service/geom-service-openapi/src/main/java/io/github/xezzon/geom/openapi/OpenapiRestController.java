package io.github.xezzon.geom.openapi;


import io.github.xezzon.geom.domain.CommonQueryBean;
import io.github.xezzon.geom.domain.Id;
import io.github.xezzon.geom.openapi.model.AddOpenapiQuery;
import io.github.xezzon.geom.openapi.model.ModifyOpenapiQuery;
import io.github.xezzon.geom.openapi.model.PublishOpenapiQuery;
import io.micronaut.data.model.Page;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.RequestBean;

/**
 * @author xezzon
 */
@Controller("/openapi")
public class OpenapiRestController {

  private final transient OpenapiService openapiService;

  public OpenapiRestController(OpenapiService openapiService) {
    this.openapiService = openapiService;
  }

  /**
   * 新增对外接口
   */
  @Post()
  public Id addOpenapi(@Body AddOpenapiQuery query) {
    Openapi openapi = query.into();
    openapiService.addOpenapi(openapi);
    return Id.of(openapi.getId());
  }

  /**
   * 查询对外接口（分页）
   */
  @Get()
  public Page<Openapi> queryOpenapiPage(@RequestBean CommonQueryBean params) {
    return openapiService.queryOpenapiPage(params.into());
  }

  /**
   * 修改对外接口
   */
  @Put()
  public Id modifyOpenapi(@Body ModifyOpenapiQuery query) {
    Openapi openapi = query.into();
    openapiService.modifyOpenapi(openapi);
    return Id.of(openapi.getId());
  }

  /**
   * 删除对外接口
   */
  @Delete("/{id}")
  public void removeOpenapi(@PathVariable String id) {
    openapiService.removeOpenapi(id);
  }

  @Put("/publish")
  public void publishOpenapi(@Body PublishOpenapiQuery query) {
    openapiService.modifyOpenapi(query.into());
  }

  @Post("/{apiId}/instance/{ownerId}")
  public void subscribeOpenapi(@PathVariable String apiId, @PathVariable String ownerId) {
    OpenapiInstance openapiInstance = new OpenapiInstance();
    openapiInstance.setApiId(apiId);
    openapiInstance.setOwnerId(ownerId);
    openapiService.subscribeOpenapi(openapiInstance);
  }
}
