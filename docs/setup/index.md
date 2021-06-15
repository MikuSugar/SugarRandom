# 安装文档

## 下载源码

```shell
git clone https://github.com/MikuSugar/SugarRandom.git
```

## [可选]设置端口

```shell
cd SugarRandom
vim src/main/resources/application.properties
```

将port 改成你想要的端口

## 编译

确保已在源码目录

```shell
mvn clean package -Pproduction
```

## 运行

将xx替换成版本号

```shell
java -jar target/sugarrandom-xx.jar
```

