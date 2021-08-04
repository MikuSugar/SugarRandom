package me.mikusugar.random.core.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.mikusugar.random.core.bean.SugarJsonNode;

/**
 * @author mikusugar
 */
public interface ConfigEventService {

    /**
     * 通过配置得到根节点
     *
     * @param configName 配置名
     */
    SugarJsonNode getSugarJsonNode(String configName);

    /**
     * 存储配置
     *
     * @param configName 配置名
     * @param rootNode 待存储的根节点
     */
    void saveConfig(String configName, SugarJsonNode rootNode) throws JsonProcessingException;

    /**
     *  通过配置名删除配置
     * @param configName 配置名
     */
    void delConfig(String configName);
}
