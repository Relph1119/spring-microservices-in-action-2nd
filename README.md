# 《Spring微服务实战》（第二版）学习笔记

该书的源码地址：https://github.com/ihuaylupo/manning-smia

## 项目结构


## 注意事项
以下主要是Windows的本地调试与部署的配置
1. 在`hosts`文件中添加
```text
127.0.0.1 eurekaserver
127.0.0.1 keycloak
```
2. 第9章需要执行docker-compose命令，启动Keycloak，正常启动后，可以访问链接：http://keycloak:8080/auth
```shell
docker-compose -f ch09/docker-compose.yml up
```

## 学习总结

