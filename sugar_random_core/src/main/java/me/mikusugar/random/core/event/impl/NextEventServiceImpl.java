package me.mikusugar.random.core.event.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.service.AbstractRandomService;
import me.mikusugar.random.core.event.NextEventService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author mikusugar
 */
@Service
@Slf4j
public class NextEventServiceImpl implements NextEventService {


    private final Map<String, AbstractRandomService> randomServiceMap;

    public NextEventServiceImpl(Map<String, AbstractRandomService> randomServiceMap) {
        this.randomServiceMap = randomServiceMap;
    }


    @Override
    public boolean check(String randomType, SugarJsonNode curNode, String randomInfo, String fieldName) {
        if (!randomServiceMap
                .get(randomType.trim())
                .check(randomInfo.trim())) return false;

        if (!(curNode.getType().equals(SugarJsonNode.TYPE.OBJECT) ||
                curNode.getType().equals(SugarJsonNode.TYPE.ARRAY))) {
            return false;
        }

        if (curNode.getType().equals(SugarJsonNode.TYPE.ARRAY)) {
            if (curNode.getNexts().size() > 0) return false;
        }

        for (SugarJsonNode node : curNode.getNexts()) {
            if (node.getName().equals(fieldName.trim())) return false;
        }
        return true;
    }

    @Override
    public void add(String name, String randomType, String randomInfo, SugarJsonNode curNode) {
        val node =
                SugarJsonNode.builder()
                        .name(name.trim())
                        .type(randomServiceMap.get(randomType).getType())
                        .randomServiceName(randomType)
                        .randomService(
                                randomServiceMap
                                        .get(randomType)
                                        .createRandomCoreService(randomInfo))
                        .desc(randomServiceMap.get(randomType).helpText())
                        .father(curNode)
                        .build();
        curNode.getNexts().add(node);
    }
}
