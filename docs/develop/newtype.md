# 新的随机类型开发

**以下所有改动均在 `sugar_random_core`上**

## 前言
目前已有的随机类型有限

<img src="https://cdn.jsdelivr.net/gh/mikusugar/PictureBed@master/uPic/2021/06/ieSCOt.png" alt="ieSCOt" style="zoom:50%;" />

虽然这些类型依然在不断更新中，但依然有可能无法满足需求。

新的类型开发非常简单，只需要两步！

## 开发步骤

### 随机类型名

为你的随机类型起个独一无二的名字吧

在`me.mikusugar.random.core.constant`包下有一个类名为`ServiceName`

```java
package me.mikusugar.random.core.constant;


/** author: fangjie email: syfangjie@live.cn date: 2021/4/9 11:48 上午 */
public class ServiceName {

  public static final String DEFAULT_CHINA_CITY = "默认国内城市";

  public static final String RANDOM_INT = "随机范围的整数（Int）";

  public static final String RANDOM_LONG = "随机范围的整数（Long）";

  public static final String RANDOM_STR = "指定列表的随机字符值（String）";

  public static final String RANDOM_OBJ = "随机对象";

  public static final String RANDOM_ARRAY_LEN = "随机数组长度";

  public static final String RANDOM_CN_NAME = "随机中文姓名";

  public static final String RANDOM_EMAIL = "随机邮箱";

  public static final String RANDOM_CN_PHONE = "随机中国大陆手机号";

  public static final String RANDOM_INT_LIST = "随机指定指定列表的整数（Int）";

  public static final String RANDOM_LONG_LIST = "随机指定指定列表的整数（Long）";

  public static final String RANDOM_IP = "随机生成IP";

}

```

依葫芦画瓢为你的随机类型起个名字

### 随机逻辑

只需要实现`me.mikusugar.random.core.service`下面的`AbstractRandomService`抽象类就👌了。

```java
package me.mikusugar.random.core.service;

import me.mikusugar.random.core.utils.RandomUtilInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author: fangjie email: syfangjie@live.cn date: 2021/4/9 11:35 上午
 */
public  abstract class  AbstractRandomService<T> {

    protected Logger log = LoggerFactory.getLogger(getClass());


    private RandomUtilInterface<T> random;

    /**
     * 生成随机造数核心
     */
    public  RandomCoreService<T> createRandomCoreService(String input){
        return new RandomCoreService<T>(input,createRandomUtilInterface(input));
    }


    protected abstract RandomUtilInterface<T> createRandomUtilInterface(String input);

    /**
     * 提示信息
     */
    public abstract String helpText();


    /**
     * 检查合法
     * @param type 类型检查
     * @param input 输入检查
     * @return
     */
    public abstract boolean check(String type,String input);



}


```

将实现类放在`me.mikusugar.random.core.service.impl`下。

例如

```java
package me.mikusugar.random.core.service.impl;

import lombok.val;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.service.AbstractRandomService;
import me.mikusugar.random.core.utils.RandomUtil;
import me.mikusugar.random.core.utils.RandomUtilInterface;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service(ServiceName.RANDOM_STR)
public class RandomString extends AbstractRandomService<String> {

  @Override
  protected RandomUtilInterface<String> createRandomUtilInterface(String input) {
    val strs = input.split(",");
    Map<String, Integer> map = new HashMap<>();
    for (String str : strs) {
      if (str.contains(":")) {
        val split = str.split(":");
        map.put(split[0], Integer.parseInt(split[1]));
      } else map.put(str,1);
    }
    return RandomUtil.getRandomWeightData(map);
  }

  @Override
  public String helpText() {
    return "请按下列格式输入，例如：a:1,b:4,c:5,d   代表的含义是在{a,b,c,d}中随机取值" + "其中它们的权重依次是1,4,5,1  默认权重1可不输入";
  }

  @Override
  public boolean check(String type, String input) {
    if (!SugarJsonNode.TYPE.STRING.toString().equals(type)) return false;
    try {
      val strs = input.split(",");
      for (String s : strs) {
        if (s.contains(":")) {
          val strings = s.split(":");
          if (strings.length != 2) return false;
          int v = Integer.parseInt(strings[1]);
        }
      }
    } catch (Exception e) {
      log.warn(e.toString());
      return false;
    }
    return true;
  }
}

```

**记得在Service注解里面绑定上一步取的随机类型名**～

🎉恭喜你，到这一步已经自定义了一个随机类型，快重启试试吧。

## 随机工具类

`me.mikusugar.random.core.utils`包下有一个随机工具类（RandomUtil），如上面的例子就用了随机工具类中的权重随机生成方法（RandomUtil.getRandomWeightData）。

希望这个工具类可以帮助到你～

## 最后

希望你添加的新的随机类，能pull到SugarRandom,期待你的pull~。

