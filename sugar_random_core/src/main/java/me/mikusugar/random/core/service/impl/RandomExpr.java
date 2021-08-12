package me.mikusugar.random.core.service.impl;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorObject;
import com.googlecode.aviator.runtime.type.AviatorString;
import lombok.extern.slf4j.Slf4j;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.service.AbstractRandomService;
import me.mikusugar.random.core.utils.GetAllService;
import me.mikusugar.random.core.utils.RandomUtilInterface;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author mikusugar
 */
@Service(ServiceName.EXPR)
@Slf4j
public class RandomExpr extends AbstractRandomService {

    static Map<String, AbstractRandomService> map;

    static {
        try {
            map = GetAllService.getAllService();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            log.error("SugarFunction 初始化失败");
        }
    }

    //init
    static {
        AviatorEvaluator.addFunction(new SugarFunction());
    }

    static class SugarFunction extends AbstractFunction {

        @Override
        public String getName() {
            return "sugar";
        }

        @Override
        public AviatorObject call(Map<String, Object> env,
                                  AviatorObject arg1, AviatorObject arg2) {
            String type = FunctionUtils.getStringValue(arg1, env);
            String input = FunctionUtils.getStringValue(arg2, env);
            return new AviatorString(
                    map.get(type).createRandomCoreService(input).getRandomUtilInterface().next().toString());

        }

    }

    @Override
    protected RandomUtilInterface createRandomUtilInterface(String input) {
        String in = input.substring(input.indexOf(",") + 1);
        final Expression expression = AviatorEvaluator.compile(in);
        return expression::execute;
    }

    @Override
    public String helpText() {
        return "Aviator 表达式,输入 type,expr 例如：STRING,{'hello SugarRandom'} 与 Aviator 官方相比 添加了 sugar 函数 " +
                " sugar(random_name,input) ,参数为随机类型和输入。 Aviator 参考  https://www.yuque.com/boyan-avfmj/aviatorscript/cpow90 ";
    }

    @Override
    public SugarJsonNode.TYPE getType(String input) {
        String type = input.substring(0, input.indexOf(","));
        return SugarJsonNode.TYPE.valueOf(type);
    }

    @Override
    public boolean check(String input) {
        if (input == null || input.isEmpty()) return false;
        return input.contains(",");
    }
}

