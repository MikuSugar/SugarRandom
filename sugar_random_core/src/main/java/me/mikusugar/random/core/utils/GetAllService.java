package me.mikusugar.random.core.utils;

import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.service.AbstractRandomService;
import me.mikusugar.random.core.service.RandomCoreService;
import org.reflections.Reflections;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 得到me.mikusugar.random.core.service 包下的所有实例
 *
 * @author mikusugar
 */
@SuppressWarnings("ALL")
public class GetAllService {

    public final static String SERVICE_PACKAGE = "me.mikusugar.random.core.service.impl";


    //TEST
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        final Map<String, AbstractRandomService> allService = getAllService();
        final AbstractRandomService abstractRandomService = allService.get(ServiceName.UUID);
        final RandomCoreService randomCoreService = abstractRandomService.createRandomCoreService("");
        int x=10;
        while (x-->0) System.out.println(randomCoreService.getRandomUtilInterface().next());
    }


    /**
     *  获取service实例
     *  也许有更好的实现
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Map<String, AbstractRandomService> getAllService() throws InstantiationException, IllegalAccessException {
        final Reflections reflections = new Reflections(SERVICE_PACKAGE);
        final Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Service.class);
        final Map<String, AbstractRandomService> res = new HashMap<>();
        for (Class<?> clz : classes) {
            final String name = clz.getAnnotationsByType(Service.class)[0].value();
            final AbstractRandomService randomService = (AbstractRandomService) clz.newInstance();
            res.put(name, randomService);
        }
        return res;
    }

}
