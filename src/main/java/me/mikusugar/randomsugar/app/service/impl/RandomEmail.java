package me.mikusugar.randomsugar.app.service.impl;

import me.mikusugar.randomsugar.app.bean.SugarJsonNode;
import me.mikusugar.randomsugar.app.constant.ServiceName;
import me.mikusugar.randomsugar.app.service.AbstractRandomService;
import me.mikusugar.randomsugar.app.utils.RandomUtilInterface;
import org.springframework.stereotype.Service;

@Service(ServiceName.RANDOM_EMAIL)
public class RandomEmail extends AbstractRandomService<String> {
  @Override
  protected RandomUtilInterface<String> createRandomUtilInterface(String input) {
    return () -> getEmail(3,15);
  }

  @Override
  public String helpText() {
    return "返回随机生成的邮箱,无需输入";
  }

  @Override
  public boolean check(String type, String input) {
    return SugarJsonNode.TYPE.STRING.toString().equals(type);
  }

  private static final String[] email_suffix =
      ("@gmail.com,@yahoo.com,@msn.com,@hotmail.com,@aol.com,@ask.com,@live.com,@qq.com,@0355.net,@163.com,@163.ne"
              + "t,@263.net,@3721.net,@yeah.net,@googlemail.com,@126.com,@sina.com,@sohu.com,@yahoo.com.cn")
          .split(",");
  public static String base = "abcdefghijklmnopqrstuvwxyz0123456789";

  public static int getNum(int start, int end) {
    return (int) (Math.random() * (end - start + 1) + start);
  }

  public static String getEmail(int lMin, int lMax) {
    int length = getNum(lMin, lMax);
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      int number = (int) (Math.random() * base.length());
      sb.append(base.charAt(number));
    }
    sb.append(email_suffix[(int) (Math.random() * email_suffix.length)]);
    return sb.toString();
  }
}
