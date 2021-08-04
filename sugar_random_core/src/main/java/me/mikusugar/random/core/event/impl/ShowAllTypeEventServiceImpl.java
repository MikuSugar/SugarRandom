package me.mikusugar.random.core.event.impl;

import me.mikusugar.random.core.event.ShowAllTypeEventService;
import me.mikusugar.random.core.service.AbstractRandomService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author mikusugar
 */
@Service
public class ShowAllTypeEventServiceImpl implements ShowAllTypeEventService {

  private final Map<String, AbstractRandomService> randomServiceMap;

  public ShowAllTypeEventServiceImpl(Map<String, AbstractRandomService> randomServiceMap) {
    this.randomServiceMap = randomServiceMap;
  }

  @Override
  public String getAllTypeInfo() {
    StringBuilder res = new StringBuilder();
    randomServiceMap.forEach(
            (k, v) ->
                    res.append(k)
                            .append("\t 描述:")
                            .append(v.helpText().replace(System.lineSeparator(), " "))
                            .append(System.lineSeparator())
    );
    return res.toString();
  }
}
