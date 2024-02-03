package io.github.xezzon.geom.openapi;

import io.github.xezzon.geom.domain.Id;
import io.github.xezzon.geom.openapi.model.AddOpenapiQuery;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

/**
 * @author xezzon
 */
@Controller("/openapi")
public class OpenapiRestController {

  protected final transient OpenapiService openapiService;

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
}
