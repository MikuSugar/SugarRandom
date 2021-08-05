package me.mikusugar.random.core.event;

import me.mikusugar.random.core.bean.SugarJsonNode;

import java.util.Map;

/**
 * @author mikusugar
 */
public interface ConfigEventService {

    /**
     * 通过配置得到根节点
     *
     * @param configName 配置名
     */
    SugarJsonNode getSugarJsonNode(String configName) throws Exception;

    /**
     * 存储配置
     *
     * @param configName 配置名
     * @param rootNode   待存储的根节点
     */
    void saveConfig(String configName, SugarJsonNode rootNode) throws Exception;

    /**
     * 通过配置名删除配置
     *
     * @param configName 配置名
     */
    void delConfig(String configName) throws Exception;


    /**
     * 读取alias配置
     */
    void readAlias(String name, Map<String, String> aliasMap) throws Exception;

    /**
     * 存储alias配置
     */
    void saveAlias(String name, Map<String, String> aliasMap) throws Exception;

    /**
     * 删除alias配置
     */
    void delAlias(String name) throws Exception;

}
