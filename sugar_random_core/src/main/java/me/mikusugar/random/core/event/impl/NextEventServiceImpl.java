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
    public void check(String randomType, SugarJsonNode curNode, String randomInfo, String fieldName) throws Exception{
        if (!randomServiceMap
                .get(randomType.trim())
                .check(randomInfo.trim())) throw new Exception("输入有误!");

        if (!(curNode.getType().equals(SugarJsonNode.TYPE.OBJECT) ||
                curNode.getType().equals(SugarJsonNode.TYPE.ARRAY))) {
            throw new Exception("只有 object 和 array 才有子节点");
        }

        if (curNode.getType().equals(SugarJsonNode.TYPE.ARRAY)) {
            if (curNode.getNexts().size() > 0) throw new Exception("array 子节点只有一个");
        }

        for (SugarJsonNode node : curNode.getNexts()) {
            if (node.getName().equals(fieldName.trim())) throw new Exception("同一层级字段名不能相同");
        }
    }

    @Override
    public void add(String name, String randomType, String randomInfo, SugarJsonNode curNode) throws Exception{
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
