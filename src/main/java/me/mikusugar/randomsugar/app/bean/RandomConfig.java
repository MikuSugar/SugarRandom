package me.mikusugar.randomsugar.app.bean;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Data
@SuperBuilder
@SuppressWarnings("ALL")
@NoArgsConstructor
public class RandomConfig {
  private String name;
  private SugarJsonNode.TYPE type;
  private String desc;
  private String randomServiceName;
  private String input;

  @Builder.Default private List<RandomConfig> nexts = new ArrayList<>();
}
