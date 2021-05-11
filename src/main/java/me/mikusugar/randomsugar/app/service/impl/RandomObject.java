package me.mikusugar.randomsugar.app.service.impl;

import me.mikusugar.randomsugar.app.constant.ServiceName;
import me.mikusugar.randomsugar.app.service.AbstractRandomService;
import me.mikusugar.randomsugar.app.utils.RandomUtilInterface;
import org.springframework.stereotype.Service;

@Service(ServiceName.RANDOM_OBJ)
public class RandomObject extends AbstractRandomService<Object> {

    @Override
    public RandomUtilInterface<Object> createRandomUtilInterface(String input) {
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
