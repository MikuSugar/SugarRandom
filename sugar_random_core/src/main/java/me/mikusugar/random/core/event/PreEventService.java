package me.mikusugar.random.core.event;

import me.mikusugar.random.core.bean.SugarJsonNode;

/**
 * @author mikusugar
 */
public interface PreEventService {
    String getPrettyJson(SugarJsonNode rootNode);
}
