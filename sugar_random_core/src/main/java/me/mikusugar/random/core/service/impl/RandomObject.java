package me.mikusugar.random.core.service.impl;


import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.service.AbstractRandomService;
import me.mikusugar.random.core.utils.RandomUtilInterface;
import org.springframework.stereotype.Service;


@Service(ServiceName.RANDOM_OBJ)
public class RandomObject extends AbstractRandomService<Object> {

    @Override
    protected RandomUtilInterface<Object> createRandomUtilInterface(String input) {
        return null;
    }

    @Override
    public String helpText() {
        return "什么都不需要输入";
    }
    @Override
    public boolean check(String type, String input) {
        return true;
    }
}
