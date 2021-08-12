package me.mikusugar.random.core.service.impl;

import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.service.AbstractRandomService;
import me.mikusugar.random.core.utils.RandomUtilInterface;
import org.springframework.stereotype.Service;

@Service(ServiceName.RANDOM_CN_PHONE)
public class RandomCNPhone extends AbstractRandomService<String> {
    @Override
    protected RandomUtilInterface<String> createRandomUtilInterface(String input) {
        return RandomCNPhone::getTel;
    }

    @Override
    public String helpText() {
        return "随机生成国内手机号，无需输入";
    }

    @Override
    public SugarJsonNode.TYPE getType(String input) {
        return SugarJsonNode.TYPE.STRING;
    }

    @Override
    public boolean check(String input) {
        return true;
    }

    private static final String[] telFirst =
            "134,135,136,137,138,139,150,151,152,157,158,159,130,131,132,155,156,133,153".split(",");

    public static int getNum(int start, int end) {
        return (int) (Math.random() * (end - start + 1) + start);
    }

    public static String getTel() {
        int index = getNum(0, telFirst.length - 1);
        String first = telFirst[index];
        String second = String.valueOf(getNum(1, 888) + 10000).substring(1);
        String third = String.valueOf(getNum(1, 9100) + 10000).substring(1);
        return first + second + third;
    }

    @Override
    public String getAliasName() {
        return "cnphone";
    }
}
