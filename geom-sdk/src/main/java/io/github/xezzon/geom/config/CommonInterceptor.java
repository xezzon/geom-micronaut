package io.github.xezzon.geom.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * HTTP请求 Java SDK 通用拦截器
 * @author xezzon
 */
public class CommonInterceptor implements RequestInterceptor {

  @Override
  public void apply(RequestTemplate requestTemplate) {
    requestTemplate
        .header("Content-Type", "application/json");
  }
}
