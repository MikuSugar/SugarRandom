package me.mikusugar.random.core.event.impl;

import lombok.extern.slf4j.Slf4j;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.event.DelEventService;
import org.springframework.stereotype.Service;

/**
 * @author mikusugar
 */
@Service
@Slf4j
public class DelEventServiceImpl implements DelEventService {
  @Override
  public void del(SugarJsonNode delNode, SugarJsonNode rootNode) {
    if (delNode.equals(rootNode)) {
      delNode.getNexts().forEach(node -> node.setFather(null));
      rootNode.getNexts().clear();
    } else {
      delNode.getFather().getNexts().remove(delNode);
      delNode.setFather(null);
    }
  }
}
