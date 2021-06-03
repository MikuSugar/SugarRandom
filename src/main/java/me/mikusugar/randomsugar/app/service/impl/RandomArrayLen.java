package me.mikusugar.randomsugar.app.service.impl;

import lombok.val;
import me.mikusugar.randomsugar.app.bean.SugarJsonNode;
import me.mikusugar.randomsugar.app.constant.ServiceName;
import me.mikusugar.randomsugar.app.service.AbstractRandomService;
import me.mikusugar.randomsugar.app.utils.RandomUtil;
import me.mikusugar.randomsugar.app.utils.RandomUtilInterface;
import org.springframework.stereotype.Service;

@Service(ServiceName.RANDOM_ARRAY_LEN)
public class RandomArrayLen extends AbstractRandomService<Integer> {
  @Override
  public RandomUtilInterface<Integer> createRandomUtilInterface(String input) {
    val ins=parse(input);
    if(ins.length==1)return () -> ins[0];
    else return RandomUtil.getRandomInt(ins[0], ins[1]);
  }

  @Override
  public String helpText() {
    return "设置数组长度,输入 2 代表数组长度为2 ,输入 2,3 代表数组长度为 2到3随机";
  }

  @Override
  public boolean check(String type, String input) {
    if (!SugarJsonNode.TYPE.ARRAY.toString().equals(type)) return false;
    try {
      int[] ins = parse(input);
      if (ins.length > 2) return false;
      for (int i : ins) if (i <= 0) return false;
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  private int[] parse(String input) {
    if (input.contains(",")) {
      val strs = input.split(",");
      int[] res = new int[strs.length];
      for (int i = 0; i < strs.length; i++) {
        res[i] = Integer.parseInt(strs[i]);
      }
      return res;
    } else return new int[] {Integer.parseInt(input)};
  }
}
