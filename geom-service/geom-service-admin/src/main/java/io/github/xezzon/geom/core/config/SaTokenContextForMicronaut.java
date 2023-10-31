package io.github.xezzon.geom.core.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaTokenContext;
import cn.dev33.satoken.context.model.SaRequest;
import cn.dev33.satoken.context.model.SaResponse;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.servlet.model.SaRequestForServlet;
import cn.dev33.satoken.servlet.model.SaResponseForServlet;
import cn.dev33.satoken.servlet.model.SaStorageForServlet;
import io.micronaut.context.annotation.Context;
import io.micronaut.core.util.AntPathMatcher;
import io.micronaut.http.context.ServerRequestContext;
import io.micronaut.servlet.http.ServletExchange;
import io.micronaut.servlet.http.ServletHttpRequest;
import io.micronaut.servlet.http.ServletHttpResponse;
import jakarta.inject.Singleton;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author xezzon
 */
@Singleton
@Context
public class SaTokenContextForMicronaut implements SaTokenContext {

  private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

  public SaTokenContextForMicronaut() {
    super();
    SaManager.setSaTokenContext(this);
  }

  public static Optional<HttpServletRequest> getServletRequest() {
    return ServerRequestContext.currentRequest()
        .map((source) -> (ServletExchange<?, ?>) source)
        .map(ServletExchange::getRequest)
        .map(ServletHttpRequest::getNativeRequest)
        .map((request) -> (HttpServletRequest) request);
  }

  public static Optional<HttpServletResponse> getServletResponse() {
    return ServerRequestContext.currentRequest()
        .map((source) -> ((ServletExchange<?, ?>) source))
        .map(ServletExchange::getResponse)
        .map(ServletHttpResponse::getNativeResponse)
        .map((response) -> ((HttpServletResponse) response));
  }

  @Override
  public SaRequest getRequest() {
    return getServletRequest()
        .map(SaRequestForServlet::new)
        .orElse(null);
  }

  @Override
  public SaResponse getResponse() {
    return getServletResponse()
        .map(SaResponseForServlet::new)
        .orElse(null);
  }

  @Override
  public SaStorage getStorage() {
    return getServletRequest()
        .map(SaStorageForServlet::new)
        .orElse(null);
  }

  @Override
  public boolean matchPath(String pattern, String path) {
    return ANT_PATH_MATCHER.matches(pattern, path);
  }
}
