# 网络抽奖系统

### 概述

项目可以对抽奖人员进行增删改查的操作；可以对各奖品的信息进行增删改查；客户管理员可以在系统中通过手机号自行注册，注册成功后，客户管理员可以针对客户公司的抽奖人员信息、抽奖奖品信息等进行管理；客户可以通过点击抽奖按钮来完成抽奖，抽奖信息可以选择显示姓名或电话号；系统管理员可以管理所有客户，针对不合规或未续费的客户可以禁用等；同时系统能管理和维护多种抽奖算法，客户在使用时可以选择符合自己期望的算法。

### 基础架构

整体架构上采用B/S架构。

表现层基于H5 Vue框架，采用前后端分离的模式，大平台，小前端，确保前端页面调整的便捷高效，支持快速的应用创新。

数据层使用了MySQL作为系统的数据库支撑，使用redis实现验证码以及token的缓存功能。

开发环境通过maven构建持续集成环境，确保开发的稳定高效。通过swagger支撑平台中台化，功能服务化，支持前端开发与后端开发的高效协同。

技术栈：vue，H5，springboot，element-ui，swagger，mysql，mybatis，redis。