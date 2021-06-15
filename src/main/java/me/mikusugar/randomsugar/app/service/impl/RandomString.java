package me.mikusugar.randomsugar.app.service.impl;

import lombok.val;
import me.mikusugar.randomsugar.app.bean.SugarJsonNode;
import me.mikusugar.randomsugar.app.constant.ServiceName;
import me.mikusugar.randomsugar.app.service.AbstractRandomService;
import me.mikusugar.randomsugar.app.utils.RandomUtil;
import me.mikusugar.randomsugar.app.utils.RandomUtilInterface;
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
      } else map.put(str, 1);
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
