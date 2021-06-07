package me.mikusugar.randomsugar.app.service.impl;

import lombok.val;
import me.mikusugar.randomsugar.app.bean.SugarJsonNode;
import me.mikusugar.randomsugar.app.constant.ServiceName;
import me.mikusugar.randomsugar.app.service.AbstractRandomService;
import me.mikusugar.randomsugar.app.utils.RandomUtil;
import me.mikusugar.randomsugar.app.utils.RandomUtilInterface;
import org.springframework.stereotype.Service;

@Service(ServiceName.RANDOM_LONG)
public class RandomLong extends AbstractRandomService<Long> {

  @Override
  protected RandomUtilInterface<Long> createRandomUtilInterface(String input) {
    val args = parse(input);
    if (args[2] == 0) return RandomUtil.getRandomLong(args[0], args[1]);
    return RandomUtil.getRandomGaussianLong(args[0], args[1]);
  }

  @Override
  public String helpText() {
    return "请输入 x,y,z x代表开始值，y代表结束值，z=0代表平均随机，z=1代表正态分布\n例如 1,2,1";
  }

  @Override
  public boolean check(String type, String input) {
    if (!SugarJsonNode.TYPE.LONG.toString().equals(type)) return false;
    try {
      val in = parse(input);
      if (in.length != 3) return false;
      if (in[0] > in[1]) return false;
      if (in[2] != 0 && in[2] != 1) return false;
    } catch (Exception e) {
      log.warn(e + " " + ServiceName.RANDOM_LONG + " " + input);
      return false;
    }
    return true;
  }

  private long[] parse(String input) {
    val strs = input.trim().split(",");
    val res = new long[strs.length];
    for (int i = 0; i < strs.length; i++) {
      res[i] = Long.parseLong(strs[i]);
    }
    return res;
  }
}
