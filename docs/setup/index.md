# 安装文档

## 一键编译

```shell
git clone https://github.com/MikuSugar/SugarRandom.git && cd SugarRandom && sh Build.sh
```

👇是手动安装和运行步骤

## 下载源码

```shell
git clone https://github.com/MikuSugar/SugarRandom.git
```

## WEB 安装

### [可选]设置端口

```shell
cd SugarRandom
vim src/main/resources/application.properties
```

将port 改成你想要的端口

### 编译

确保已在`SugarRandom/`下

```shell
mvn clean package -Pproduction
```

### 运行

将xx替换成版本号

```shell
java -jar target/sugarrandom-xx.jar
```
## CLI 安装

### 更改配置【可选】

````shell
cd SugarRandom/sugar_random_cli
#改成你想要的配置
vim src/main/resources/application.properties 
````

### 编译

确保已在`SugarRandom/sugar_random_cli/`下

```shell
 mvn clean package
```

### 运行

```shell
java -jar target/sugar_random_cli-xx.jar
```

## 可能会遇到的问题🤔

### sugar_random_core 依赖

这个在GitHub package上，未来可能会上传到中央仓库。

请仔细阅读这个文档[https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)

如果不想配置，可以进入`SugarRandom/sugar_random_core`目录下，手动install到本地仓库。

**你可能会赶到疑惑，为什么这几个模块没有配置父子依赖关系？**

因为vaadin框架必须在根目录，然后我又需要共享核心代码，现在的结构是我能找到的最好方式，如果有好的解决方法，欢迎联系我。
