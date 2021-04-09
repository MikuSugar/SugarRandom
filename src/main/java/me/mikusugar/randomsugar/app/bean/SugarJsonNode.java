package me.mikusugar.randomsugar.app.bean;

import lombok.Data;
import me.mikusugar.randomsugar.app.utils.RandomService;

/**
 * author: fangjie email: syfangjie@live.cn date: 2021/4/1 3:27 下午
 */
@Data
public class SugarJsonNode {

  public enum TYPE{
    NULL, LONG, DOUBLE, BOOLEAN, STRING, ARRAY, OBJECT,INT
  }

  private TYPE type;

  private String name;

  private RandomService randomService;

}
