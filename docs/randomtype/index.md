# 随机服务类概述

SugarRandom 的造数服务类就是**me.mikusugar.random.core.service.AbstractRandomService**的实现类

```java
package me.mikusugar.random.core.service;

import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.utils.RandomUtilInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author: fangjie email: syfangjie@live.cn date: 2021/4/9 11:35 上午
 */
public  abstract class  AbstractRandomService<T> {

    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 生成随机造数核心
     */
    public  RandomCoreService<T> createRandomCoreService(String input){
        return new RandomCoreService<>(input, createRandomUtilInterface(input));
    }


    protected abstract RandomUtilInterface<T> createRandomUtilInterface(String input);

    /**
     * 提示信息
     */
    public abstract String helpText();

    /**
     * 获取数据类型
     * @return SugarJsonNode.TYPE
     */
    public abstract SugarJsonNode.TYPE getType(String input);

    /**
     * 检查合法
     * @param input 输入检查
     */
    public abstract boolean check(String input);

}

```

其中的核心为**me.mikusugar.random.core.service.RandomCoreService**

```java
package me.mikusugar.random.core.service;


import me.mikusugar.random.core.utils.RandomUtilInterface;

public class RandomCoreService<T> {

    private final String input;

    private final RandomUtilInterface<T> randomUtilInterface;

    public RandomCoreService(String input, RandomUtilInterface<T> randomUtilInterface) {
        this.input = input;
        this.randomUtilInterface = randomUtilInterface;
    }

    public String getInput() {
        return input;
    }

    public RandomUtilInterface<T> getRandomUtilInterface() {
        return randomUtilInterface;
    }

}

```

里面的**randomUtilInterface**会有一个next()方法，就是通过这个next方法源源不断的随机生成数据。

```java
package me.mikusugar.random.core.utils;

public interface RandomUtilInterface<T> {
    T next();
}

```

下面会介绍各个类型。