package me.mikusugar.random.core.service.impl;

import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.service.AbstractRandomService;
import me.mikusugar.random.core.utils.RandomUtilInterface;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author mikusugar
 */
@Service(ServiceName.LONLAT)
public class RandomLonLat extends AbstractRandomService<String> {

    @Override
    protected RandomUtilInterface<String> createRandomUtilInterface(String input) {
        final String[] strs = input.trim().split(",");
        double[] in = new double[strs.length];
        for (int i = 0; i < strs.length; i++) {
            in[i] = Double.parseDouble(strs[i]);
        }
        return () -> randomLonLat(in[0], in[1], in[2], in[3]);
    }

    @Override
    public String helpText() {
        return "输入4位数字，如 112.1212, 134.1212, 43.12, 65.123 分别是 最小经度 最大经度 最小纬度 最大纬度 将返回这个结果内的值";
    }

    /**
     * @param MinLon 最小经度
     * @param MaxLon 最大经度
     * @param MinLat 最小纬度
     * @param MaxLat 最大纬度
     * @return [lon, lat]
     */
    private static String randomLonLat(double MinLon, double MaxLon, double MinLat, double MaxLat) {
        BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
        String lon = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
        String lat = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        return lon + "," + lat;
    }


    @Override
    public boolean check(String type, String input) {
        if (!SugarJsonNode.TYPE.STRING.toString().equals(type)) return false;
        try {
            final String[] strs = input.trim().split(",");
            if (strs.length != 4) return false;
            for (String str : strs) {
                Double.parseDouble(str);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
