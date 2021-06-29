package me.mikusugar.random.core.service;

import me.mikusugar.random.core.utils.RandomUtilInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * author: fangjie email: syfangjie@live.cn date: 2021/4/9 11:35 上午
 */
public  abstract class  AbstractRandomService<T> {

    protected Logger log = LoggerFactory.getLogger(getClass());


    private RandomUtilInterface<T> random;

    /**
     * 生成随机造数核心
     */
    public  RandomCoreService<T> createRandomCoreService(String input){
        return new RandomCoreService<T>(input,createRandomUtilInterface(input));
    }


    protected abstract RandomUtilInterface<T> createRandomUtilInterface(String input);

    /**
     * 提示信息
     */
    public abstract String helpText();


    /**
     * 检查合法
     * @param type 类型检查
     * @param input 输入检查
     * @return
     */
    public abstract boolean check(String type,String input);



}

