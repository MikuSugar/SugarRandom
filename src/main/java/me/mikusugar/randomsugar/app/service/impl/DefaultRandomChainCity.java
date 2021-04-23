package me.mikusugar.randomsugar.app.service.impl;

import java.util.HashMap;
import java.util.Map;

import me.mikusugar.randomsugar.app.bean.SugarJsonNode;
import me.mikusugar.randomsugar.app.constant.ServiceName;
import me.mikusugar.randomsugar.app.service.AbstractRandomService;
import me.mikusugar.randomsugar.app.utils.RandomUtil;
import me.mikusugar.randomsugar.app.utils.RandomUtilInterface;
import me.mikusugar.randomsugar.app.utils.SugarPair;
import org.springframework.stereotype.Service;

/**
 * author: fangjie email: syfangjie@live.cn date: 2021/4/9 11:48 上午
 */
@Service(ServiceName.DEFAULT_CHINA_CITY)
public class DefaultRandomChainCity extends AbstractRandomService<String> {

  private static final RandomUtilInterface<String> random;
  static {
    SugarPair<String, Integer>[] citys = new SugarPair[]{
        new SugarPair<>("ShangHai", 4),
        new SugarPair<>("NeiMengGu", 1),
        new SugarPair<>("AnHui", 1),
        new SugarPair<>("ChongQing", 2),
        new SugarPair<>("JiangSu", 2),
        new SugarPair<>("HeiLongJiang", 1),
        new SugarPair<>("XiangGang", 2),
        new SugarPair<>("NingXia", 1),
        new SugarPair<>("QingHai", 1),
        new SugarPair<>("JiLin", 1),
        new SugarPair<>("GuangDong", 3),
        new SugarPair<>("HeNan", 1),
        new SugarPair<>("TaiWan", 1),
        new SugarPair<>("HeBei", 1),
        new SugarPair<>("HaiNan", 1),
        new SugarPair<>("GuiZhou", 1),
        new SugarPair<>("LiaoNing", 1),
        new SugarPair<>("ShanDong", 1),
        new SugarPair<>("GuangXi", 1),
        new SugarPair<>("XiZang", 1),
        new SugarPair<>("HuNan", 1),
        new SugarPair<>("XinJiang", 1),
        new SugarPair<>("JiangXi", 1),
        new SugarPair<>("TianJin", 2),
        new SugarPair<>("BeiJing", 4),
        new SugarPair<>("HuBei", 1),
        new SugarPair<>("FuJian", 1),
        new SugarPair<>("ZheJiang", 2),
        new SugarPair<>("Aomen", 1),
        new SugarPair<>("YunNan", 1),
        new SugarPair<>("GanSu", 1),
        new SugarPair<>("ShanXi", 1),
        new SugarPair<>("SiChuan", 1)
    };
    final Map<String, Integer> map = new HashMap<>();
    for (SugarPair<String, Integer> city : citys) {
      map.put(city._1, city._2);
    }
    random = RandomUtil.getRandomWeightData(map);
  }

  @Override
  public RandomUtilInterface<String> createRandomUtilInterface(String input) {
    return random;
  }

  @Override
  public String helpText() {
    return "随机生成国内城市，无需输入～ (北上广等概率大)";
  }

  @Override
  public boolean check(String type, String input) {
    return SugarJsonNode.TYPE.STRING.toString().equals(type);
  }


}
