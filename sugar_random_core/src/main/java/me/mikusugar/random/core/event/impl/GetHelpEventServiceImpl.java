package me.mikusugar.random.core.event.impl;

import me.mikusugar.random.core.event.GetHelpEventService;
import me.mikusugar.random.core.service.AbstractRandomService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author mikusugar
 */
@Service
public class GetHelpEventServiceImpl implements GetHelpEventService {

    private final Map<String, AbstractRandomService> randomServiceMap;

    public GetHelpEventServiceImpl(Map<String, AbstractRandomService> randomServiceMap) {
        this.randomServiceMap = randomServiceMap;
    }


    @Override
    public String getHelpStr(String randomType) {
        final AbstractRandomService service = randomServiceMap.get(randomType);
        if (service == null) return null;
        return service.helpText();
    }
}
