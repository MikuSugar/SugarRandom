package me.mikusugar.randomsugar.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.mikusugar.randomsugar.app.bean.RandomConfig;
import me.mikusugar.randomsugar.app.bean.SugarJsonNode;
import me.mikusugar.randomsugar.app.service.AbstractRandomService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class SugarJsonNodeSerialization {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static String write(SugarJsonNode node) throws JsonProcessingException {
    RandomConfig randomConfig = toRandomConfig(node);
    return mapper.writeValueAsString(randomConfig);
  }

  private static RandomConfig toRandomConfig(SugarJsonNode node) {
    List<RandomConfig> next = new ArrayList<>();
    if (node.getNexts().size() > 0) {
      next.addAll(
          node.getNexts().stream()
              .map(SugarJsonNodeSerialization::toRandomConfig)
              .collect(Collectors.toList()));
    }
    String input = "";
    if (node.getRandomService() != null && node.getRandomService().getInput() != null) {
      input = node.getRandomService().getInput();
    }
    return RandomConfig.builder()
        .name(node.getName())
        .desc(node.getDesc())
        .type(node.getType())
        .randomServiceName(node.getRandomServiceName())
        .input(input)
        .nexts(next)
        .build();
  }

  public static SugarJsonNode read(String str, Map<String, AbstractRandomService> randomServiceMap)
      throws JsonProcessingException {
    final RandomConfig randomConfig = mapper.readValue(str, RandomConfig.class);
    return toSugarJsonNode(randomConfig, randomServiceMap);
  }

  private static SugarJsonNode toSugarJsonNode(
      RandomConfig randomConfig, Map<String, AbstractRandomService> randomServiceMap) {
    List<SugarJsonNode> next = new ArrayList<>();
    if (randomConfig.getNexts().size() > 0) {
      next.addAll(
          randomConfig.getNexts().stream()
              .map(r -> toSugarJsonNode(r, randomServiceMap))
              .collect(Collectors.toList()));
    }
    String input = "";
    if (randomConfig.getInput() != null) input = randomConfig.getInput();
    return SugarJsonNode.builder()
        .name(randomConfig.getName())
        .desc(randomConfig.getDesc())
        .randomServiceName(randomConfig.getRandomServiceName())
        .nexts(next)
        .randomService(
            randomServiceMap
                .get(randomConfig.getRandomServiceName())
                .createRandomCoreService(input))
        .type(randomConfig.getType())
        .build();
  }
}
