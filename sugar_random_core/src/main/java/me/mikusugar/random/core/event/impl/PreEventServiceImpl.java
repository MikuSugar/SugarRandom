package me.mikusugar.random.core.event.impl;

import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.utils.SugarJsonUtils;
import me.mikusugar.random.core.event.PreEventService;
import org.springframework.stereotype.Service;

/**
 * @author mikusugar
 */
@Service
public class PreEventServiceImpl implements PreEventService {

    @Override
    public String getPrettyJson(SugarJsonNode rootNode) {
        SugarJsonNode tempNode = rootNode;

        //TODO: ugly code
        //为了解决格式化问题.
        if(rootNode.getFather()!=null){
            tempNode = SugarJsonNode.builder()
                    .name("root")
                    .type(SugarJsonNode.TYPE.OBJECT)
                    .desc("默认根节点")
                    .randomServiceName(ServiceName.RANDOM_OBJ)
                    .build();
            tempNode.getNexts().add(rootNode);
        }


        final StringBuilder res = new StringBuilder();
        SugarJsonUtils.toJsonStr(null, tempNode, res);
        return SugarJsonUtils.json2PrettyFormat(res.toString());


    }
}
