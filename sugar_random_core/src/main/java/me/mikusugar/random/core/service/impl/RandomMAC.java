package me.mikusugar.random.core.service.impl;

import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.service.AbstractRandomService;
import me.mikusugar.random.core.utils.RandomUtilInterface;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * @author mikusugar
 */
@Service(ServiceName.MAC)
public class RandomMAC extends AbstractRandomService<String> {

    private final static Random random = new Random();
    private final static String SEPARATOR_OF_MAC = ":";
    private final static String _02X = "%02x";

    @Override
    protected RandomUtilInterface<String> createRandomUtilInterface(String input) {
        return () -> {
            String[] mac = {
                    String.format(_02X, 0x52),
                    String.format(_02X, 0x54),
                    String.format(_02X, 0x00),
                    String.format(_02X, random.nextInt(0xff)),
                    String.format(_02X, random.nextInt(0xff)),
                    String.format(_02X, random.nextInt(0xff))};
            return String.join(SEPARATOR_OF_MAC, mac);
        };
    }

    @Override
    public String helpText() {
        return "随机生成MAC地址,无需输入";
    }

    @Override
    public boolean check(String type, String input) {
        return SugarJsonNode.TYPE.STRING.toString().equals(type);
    }
}
