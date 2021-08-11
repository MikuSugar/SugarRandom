package me.mikusugar.random.core.service.impl;

import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.service.AbstractRandomService;
import me.mikusugar.random.core.utils.RandomUtilInterface;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author mikusugar
 */
@Service(ServiceName.UUID)
public class RandomUUID extends AbstractRandomService<String> {
    @Override
    protected RandomUtilInterface<String> createRandomUtilInterface(String input) {
        return () -> UUID.randomUUID().toString();
    }

    @Override
    public String helpText() {
        return "java 原生的UUID，无需输入";
    }

    @Override
    public boolean check(String input) {
        return true;
    }

    @Override
    public SugarJsonNode.TYPE getType(String input) {
        return SugarJsonNode.TYPE.STRING;
    }
}
