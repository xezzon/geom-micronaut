package io.github.xezzon.geom.openapi;

import jakarta.inject.Singleton;

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
}
