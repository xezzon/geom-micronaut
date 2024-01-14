# geom

一组面向云原生开发的基础设施服务。帮助开发者专注于业务代码的开发。

## 功能特性

geom 后端提供 2 个服务与 1 个 SDK。

- admin 系统管理服务
  - 认证
  - 菜单权限
  - 数据权限
  - SSO
  - 字典
- openapi 开发平台服务
  - 发布接口
  - 订阅接口
  - WEBHOOK
  - 消息解密/验证
- sdk

## 如何使用

最简单的方式是通过 [docker-compose](./docker-compose.yml) 启动。

```shell
#!/bin/bash
# 只需要运行需要的服务
docker-compose up -d geom-service-admin geom-service-openapi
```

## 技术栈

`TODO`

## [写给项目维护者](./CONTRIBUTING.md)

## [许可证](./LICENSE)

[LGPL-2.1 license](https://www.gnu.org/licenses/lgpl-3.0.html)
