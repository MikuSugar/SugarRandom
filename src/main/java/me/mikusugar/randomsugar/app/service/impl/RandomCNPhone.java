package me.mikusugar.randomsugar.app.service.impl;

import me.mikusugar.randomsugar.app.bean.SugarJsonNode;
import me.mikusugar.randomsugar.app.constant.ServiceName;
import me.mikusugar.randomsugar.app.service.AbstractRandomService;
import me.mikusugar.randomsugar.app.utils.RandomUtilInterface;
import org.springframework.stereotype.Service;

@Service(ServiceName.RANDOM_CN_PHONE)
public class RandomCNPhone extends AbstractRandomService<String> {
  @Override
  protected RandomUtilInterface<String> createRandomUtilInterface(String input) {
    return RandomCNPhone::getTel;
  }

  @Override
  public String helpText() {
    return "随机生成国内手机号，无需输入";
  }

  @Override
  public boolean check(String type, String input) {
    return SugarJsonNode.TYPE.STRING.toString().equals(type);
  }

  private static final String[] telFirst =
      "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");

  public static int getNum(int start, int end) {
    return (int) (Math.random() * (end - start + 1) + start);
  }

  public static String getTel() {
    int index = getNum(0, telFirst.length - 1);
    String first = telFirst[index];
    String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
    String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
    return first + second + third;
  }
}
