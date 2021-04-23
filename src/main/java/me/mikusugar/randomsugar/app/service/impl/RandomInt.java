package me.mikusugar.randomsugar.app.service.impl;

import lombok.val;
import me.mikusugar.randomsugar.app.bean.SugarJsonNode;
import me.mikusugar.randomsugar.app.constant.ServiceName;
import me.mikusugar.randomsugar.app.service.AbstractRandomService;
import me.mikusugar.randomsugar.app.utils.RandomUtil;
import me.mikusugar.randomsugar.app.utils.RandomUtilInterface;
import org.springframework.stereotype.Service;

@Service(ServiceName.RANDOM_INT)
public class RandomInt extends AbstractRandomService<Integer> {

  @Override
  public RandomUtilInterface<Integer> createRandomUtilInterface(String input) {
    val arr = parse(input);
    int x = arr[0], y = arr[1], z = arr[2];
    if (z == 0) return RandomUtil.getRandomInt(x, y);
    return RandomUtil.getRandomGaussianInt(x, y);
  }

  @Override
  public String helpText() {
    return "请输入 x,y,z x代表开始值，y代表结束值，z=0代表平均随机，z=1代表正态分布\n例如 1,2,1";
  }

  @Override
  public boolean check(String type, String input) {
    if (!SugarJsonNode.TYPE.INT.toString().equals(type)) return false;
    try {
      int[] arr = parse(input);
      if (arr.length != 3) return false;
      if (arr[0] > arr[1]) return false;
      if (arr[2] != 0 && arr[2] != 1) return false;
    } catch (Exception e) {
      log.warn(e + ServiceName.RANDOM_INT + " " + input);
      return false;
    }
    return true;
  }

  private int[] parse(String input) {
    val strs = input.trim().split(",");
    val res = new int[strs.length];
    for (int i = 0; i < strs.length; i++) {
      res[i] = Integer.parseInt(strs[i]);
    }
    return res;
  }
}