# 第1阶段
# 从包含Java运行时的基本镜像开始
FROM openjdk:11-slim as build

# 添加维护人员信息
LABEL maintainer="Illary Huaylupo <illaryhs@gmail.com>"

# 应用程序的JAR文件
ARG JAR_FILE

# 将应用程序的JAR文件添加到容器中
COPY ${JAR_FILE} app.jar

# 解药JAR文件
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

# 阶段2
# 相同的JAVA运行时
FROM openjdk:11-slim

# 添加指向/tmp的数据卷
VOLUME /tmp

# 将未打包的应用程序复制到新的容器
ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# 执行应用程序
ENTRYPOINT ["java","-cp","app:app/lib/*","com.optimagrowth.license.LicenseServiceApplication"]