package me.mikusugar.random.core.event.impl;

import me.mikusugar.random.core.event.AliasEventService;
import me.mikusugar.random.core.service.AbstractRandomService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author mikusugar
 */
@Service
public class AliasEventServiceImpl implements AliasEventService {

    private final Map<String, AbstractRandomService> randomServiceMap;

    public AliasEventServiceImpl(Map<String, AbstractRandomService> randomServiceMap) {
        this.randomServiceMap = randomServiceMap;
    }

    @Override
    public boolean checkAliasEventService(String name) {
        return !randomServiceMap.containsKey(name);
    }
}
