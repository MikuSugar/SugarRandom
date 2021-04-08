package me.mikusugar.randomsugar.app.bean;

import java.util.Map;
import lombok.Data;
import me.mikusugar.randomsugar.app.utils.RandomService;

/**
 * author: fangjie email: syfangjie@live.cn date: 2021/4/1 3:27 下午
 */
@Data
public class SugarJsonNode {

  public enum TYPE{
    NULL, NUMBER, BOOLEAN, STRING, ARRAY, OBJECT
  }

  private Map<String,SugarJsonNode> fieldMap;

  private String Value;

  private TYPE type;

  private String name;

  private RandomService randomService;

}
