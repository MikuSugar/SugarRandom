package me.mikusugar.random.core.event;

import me.mikusugar.random.core.bean.SugarJsonNode;

/**
 * @author mikusugar
 */
public interface NextEventService {

    /***
     * 检查参数是否正确
     */
    void check(String randomType, SugarJsonNode curNode, String randomInfo, String fieldName) throws Exception;

    /**
     *  添加节点配置
     */
    void add(String name, String randomType, String randomInfo, SugarJsonNode curNode) throws Exception;
}
