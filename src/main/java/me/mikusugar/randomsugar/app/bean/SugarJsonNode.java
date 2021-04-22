package me.mikusugar.randomsugar.app.bean;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import me.mikusugar.randomsugar.app.utils.RandomUtilInterface;

import java.util.ArrayList;
import java.util.List;

/** author: fangjie email: syfangjie@live.cn date: 2021/4/1 3:27 下午 */
@Data
@SuperBuilder
public class SugarJsonNode {

  public enum TYPE {
    NULL,
    LONG,
    DOUBLE,
    BOOLEAN,
    STRING,
    ARRAY,
    OBJECT,
    INT
  }

  private TYPE type;

  private String name;

  private String desc;

  private RandomUtilInterface randomService;

  @Builder.Default private List<SugarJsonNode> nexts = new ArrayList<>();
}
