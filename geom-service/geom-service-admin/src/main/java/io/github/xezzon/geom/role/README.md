# 角色管理

## 内置角色

- 超级管理员(root): 角色所有者应为技术负责人。拥有所有的权限，后续有新的权限时默认赋予超级管理员。
- 站点管理员(superuser): 角色所有者应为运维者。不可继承。唯一拥有查看超过自己权限范围的菜单的角色。一般拥有权限管理、日志查看等权限。
- 系统管理员(admin): 业务管理员。角色所有者为业务方负责运营网站的相关人员。拥有发布公告、查看报表等权限。

## 角色的继承

`可继承的`角色可以在`角色管理`菜单中，创建其直接子级。

角色可以删除其子级（包括间接子级）。

角色变更为`不可继承的`时，删除其所有子级。

取消某角色的权限会取消其所有子级的相应权限，赋予权限则不会传递。