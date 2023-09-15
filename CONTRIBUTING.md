# 写给项目维护者

首先，向所有参与本项目的开发者致谢。

在本文档中，我将向您介绍如何参与 geom 项目后端部分的开发。

## 项目结构

```
geom
├── geom-app  # 服务目录
│   ├── geom-app-admin  # 后台管理服务
│   └── geom-app-openapi  # 开放接口服务
├── geom-common  # 各服务共享的 bean
├── geom-core  # 用于存放一些共享的内容（如全局常量、DTO）
└── geom-sdk  # 提供给系统内其他服务调用 geom 服务的客户端
```

### 模块间依赖关系

```mermaid
flowchart LR
    geom-app-admin-->geom-common
    geom-app-openapi-->geom-common
    geom-app-openapi-->geom-sdk
    geom-common-->geom-core
    geom-sdk--> geom-core
```

### Java package

一个服务可能会提供多种功能，所以我们将会像这样组织项目的包：

```
geom-app-admin
├── src
│   ├── main
│   │   ├── java
│   │   │   └── io/github/xezzon/geom
│   │   │       ├── auth  # 认证功能
│   │   │       │   ├── adaptor  # 服务接口
│   │   │       │   ├── domain  # 充血模型
│   │   │       │   │   └── convert  # MapStruct 接口
│   │   │       │   ├── repository  # 基础 DAO 接口
│   │   │       │   │   └── wrapper  # 增强的 DAO 类
│   │   │       │   └── service  # 控制逻辑接口
│   │   │       │       └── impl  # 承担绝大部分业务逻辑
│   │   │       ├── dict  # 字典功能
│   │   │       └── AdminApplication.java  # 启动类
│   │   └── resources
│   │       └── config
│   └── test  # 单元测试代码
│       ├── java
│       └── resources
└── target
    └── generated-sources  # 由 Maven 插件生成的代码，是成品的一部分，请不要试图改动
```

其余部分与之类似，会先按功能分包，再按层级分包。

## 如何运行与测试

`TODO`

## 代码风格

`TODO`

## 如何提交您的代码

`TODO`
