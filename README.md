# 《Spring微服务实战》（第二版）学习笔记

该书的源码地址：https://github.com/ihuaylupo/manning-smia

## 项目结构

## 注意事项
以下主要是Windows的本地调试与部署的配置
1. 需要搭建一个Postgres，执行data文件下的脚本

2. 在`hosts`文件中添加
```text
127.0.0.1 eurekaserver
127.0.0.1 keycloak
127.0.0.1 kafka
127.0.0.1 redis
127.0.0.1 gateway
```

3. 第9章需要执行docker-compose命令，启动Keycloak，正常启动后，可以访问链接：http://keycloak:8080/auth
```shell
docker-compose -f docker/docker-compose.yml up
```

4. 第10章中直接使用docker-compose命令可以完成zookeeper、kafak和redis的搭建
```shell
docker-compose -f docker/docker-compose.yml up
```

## 学习总结

