package me.mikusugar.random.core.event;

import me.mikusugar.random.core.bean.SugarJsonNode;

/**
 * @author mikusugar
 */
public interface FileEventService {
  void out(SugarJsonNode sugarJsonNode,int num,String path) throws Exception;
}
