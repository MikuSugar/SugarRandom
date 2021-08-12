package me.mikusugar.random.core.event.impl;

import me.mikusugar.random.core.event.ShowEventService;
import me.mikusugar.random.core.service.AbstractRandomService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author mikusugar
 */
@Service
public class ShowEventServiceImpl implements ShowEventService {

    private final Map<String, AbstractRandomService> randomServiceMap;

    public ShowEventServiceImpl(Map<String, AbstractRandomService> randomServiceMap) {
        this.randomServiceMap = randomServiceMap;
    }

    @Override
    public String getAllTypeInfo() {
        StringBuilder res = new StringBuilder();
        randomServiceMap.forEach(
                (k, v) ->
                        res.append(k)
                                .append("\t 描述:")
                                .append(v.helpText().replace(System.lineSeparator(), " "))
                                .append(System.lineSeparator())
        );
        return res.toString();
    }

    @Override
    public String echo(String name, Map<String, String> aliasMap) {
        if (name == null || name.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            aliasMap.forEach((k, v) -> sb.append(k).append("     \t").append(v).append(System.lineSeparator()));
            return sb.toString();
        }
        return name + " " + aliasMap.get(name);
    }

}
