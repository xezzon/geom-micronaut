# 认证

## 用户认证（前端）

```mermaid
---
title: 认证流程
---
sequenceDiagram
    participant client AS 客户端
    participant gateway AS 网关
    participant auth AS 认证中心
    participant service AS 服务端
    
    client ->> auth: 携带 Basic Token 的登录请求
    auth -->> client: 返回 X-SESSION-ID
    
    client->> gateway: HTTP请求（携带 X-SESSION-ID Header）
    gateway ->> auth: 转发到认证中心
    auth -->> gateway: 读 X-SESSION-ID 获取 JWT
    gateway ->> service: 将 JWT 和公钥分别写入Header（Authentication 和 X-PUBLIC-KEY）
    
    service -> service: 用公钥和时间戳校验 JWT
    service -> service: 从 JWT 中获取用户信息
    service -->> client: 返回请求结果 
```

## 第三方认证

```mermaid
---
title: 认证流程
---
flowchart TB
    subgraph 第三方系统
        apply-secret-key["申请Key、密钥"]
        store-secret-key["存储Key、密钥"]
        subgraph 发送HTTP请求
            generate-timestamp["生成时间戳"]
            generate-checksum["生成摘要"]
            encrypt-message["用密钥加密消息"]
            send-message["携带Key、时间戳、摘要发送HTTP请求"]
            
            generate-timestamp --- generate-checksum --- encrypt-message --> send-message
        end
        store-secret-key -.-> generate-timestamp
    end
    subgraph 认证中心
        send-secret-key["发放Key、密钥"]
        subgraph 消息解密
            check-timestamp["校验时间戳"]
            decrypt-key["解密Key 获得ID 并查询到对应的组织"]
            decrypt-message["用查询到的该组织的密钥解密消息"]
            check-checksum["校验摘要"]
            
            send-message --> check-timestamp --> decrypt-key --> decrypt-message --> check-checksum
        end
    end
    
    apply-secret-key --> send-secret-key --> store-secret-key
```

## 签发 JWT

本系统签发的 JWT 采用 ES256 算法。

claim 命名参照 [IANA 机构](https://www.iana.org/assignments/jwt/jwt.xhtml)内已注册的 claim，包含以下内容：

| 字段                 | 含义         | 注释                                        |
|--------------------|------------|-------------------------------------------|
| iss                | 机构名称       | 通过 `geom.jwt.issuer` 配置                   |
| sub                | 认证者ID      | 用户/用户组ID                                  |
| iat                | 令牌签发时间戳    |                                           |
| exp                | 令牌过期时间戳    | 如果当前时间戳大于exp，则令牌视为无效。选填。                  |
| nbf                | 令牌生效时间     | 如果当前时间戳小于nbf，则令牌视为无效。选填。                  |
| jti                | 令牌ID       | 生成的UUID。选填。                               |
| preferred_username | 用户名/用户组编码  |                                           |
| nickname           | 用户昵称/用户组名称 |                                           |
| roles              | 持有者角色      | 类型为 String[]。即用户的角色编码集合。用户组该字段为["GROUP"]  |
| groups             | 所在用户组      | 类型为 String[]。即用户所在用户组的编码的集合。用户组该字段为[]     |
| entitlements       | 持有者权限      | 类型为 String[]。即用户所在用户组的编码的集合。或用户组已订阅接口的集合。 |

# 授权

## 菜单权限

## 数据权限
