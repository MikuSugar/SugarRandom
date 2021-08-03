# 想在自己的代码中直接引用造数的服务类吗？

很简单，只要两步。

## one

依赖导入

```
<dependency>
  <groupId>me.mikusugar.random</groupId>
  <artifactId>sugar_random_core</artifactId>
  <version>1.2-SNAPSHOT</version>
</dependency>
```

> 版本后请选择最新版

⚠️目前依赖在GitHub package，并没有在中央仓库。[github 仓库使用参考](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry)

## two

有一个类`me.mikusugar.random.core.utils.GetAllService`

里面`Map<String, AbstractRandomService> getAllService() `这个静态方法，使用参考这个类的main方法。

这个方法返回的map包含了所有的随机造数服务。