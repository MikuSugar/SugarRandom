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
@Service(ServiceName.IPV6)
public class RandomIpv6 extends AbstractRandomService<String> {
    @Override
    protected RandomUtilInterface<String> createRandomUtilInterface(String input) {
        return RandomIpv6::getIpv6;
    }


    private final static char[] mapping = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    private final static Random random = new Random();

    private static String getIpv6() {
        //IPv6 地址大小为 128 位。首选 IPv6 地址表示法为 x:x:x:x:x:x:x:x，
        // 其中每个 x 是地址的 8 个 16 位部分的十六进制值。
        // IPv6 地址范围从 0000:0000:0000:0000:0000:0000:0000:0000 至 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff。
        StringBuilder sb = new StringBuilder();
        for (int i=0;i<8;i++){
            for (int k=0;k<4;k++){
                sb.append(mapping[random.nextInt(mapping.length)]);
            }
            if(i!=7)sb.append(":");
        }
        return sb.toString();
    }

    @Override
    public String helpText() {
        return "无需输入";
    }

    @Override
    public SugarJsonNode.TYPE getType() {
        return SugarJsonNode.TYPE.STRING;
    }

    @Override
    public boolean check(String input) {
        return true;
    }
}
