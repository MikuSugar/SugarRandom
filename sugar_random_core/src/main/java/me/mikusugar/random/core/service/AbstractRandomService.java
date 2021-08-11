package me.mikusugar.random.core.service;

import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.utils.RandomUtilInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author: fangjie email: syfangjie@live.cn date: 2021/4/9 11:35 上午
 */
public  abstract class  AbstractRandomService<T> {

    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 生成随机造数核心
     */
    public  RandomCoreService<T> createRandomCoreService(String input){
        return new RandomCoreService<>(input, createRandomUtilInterface(input));
    }


    protected abstract RandomUtilInterface<T> createRandomUtilInterface(String input);

    /**
     * 提示信息
     */
    public abstract String helpText();

    /**
     * 获取数据类型
     * @return SugarJsonNode.TYPE
     */
    public abstract SugarJsonNode.TYPE getType(String input);

    /**
     * 检查合法
     * @param input 输入检查
     */
    public abstract boolean check(String input);



}

