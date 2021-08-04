package me.mikusugar.random.core.event.impl;

import me.mikusugar.random.core.bean.SugarJsonNode;
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
        final StringBuilder res = new StringBuilder();
        SugarJsonUtils.toJsonStr(null, rootNode, res);
        return SugarJsonUtils.json2PrettyFormat(res.toString());
    }
}
