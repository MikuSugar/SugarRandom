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

@Service(ServiceName.RANDOM_INT_LIST)
public class RandomIntList extends AbstractRandomService<Integer> {

  @Override
  protected RandomUtilInterface<Integer> createRandomUtilInterface(String input) {
      val strs = input.split(",");
      Map<Integer, Integer> map = new HashMap<>();
      for (String str : strs) {
          if (str.contains(":")) {
              val split = str.split(":");
              map.put(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
          } else map.put(Integer.parseInt(str), 1);
      }
      return RandomUtil.getRandomWeightData(map);
  }

  @Override
  public String helpText() {
    return "请按下列格式输入，例如：a:1,b:4,c:5,d   代表的含义是在{a,b,c,d}中随机取值" + "其中它们的权重依次是1,4,5,1  默认权重1可不输入";
  }

  @Override
  public boolean check(String type, String input) {
    if (!SugarJsonNode.TYPE.INT.toString().equals(type)) return false;
    try {
      val strs = input.split(",");
      for (String s : strs) {
        if (s.contains(":")) {
          val strings = s.split(":");
          if (strings.length != 2) return false;
          int v = Integer.parseInt(strings[1]);
          int key = Integer.parseInt(strings[0]);
        } else {
          Integer.parseInt(s);
        }
      }
    } catch (Exception e) {
      log.warn(e.toString());
      return false;
    }
    return true;
  }
}
