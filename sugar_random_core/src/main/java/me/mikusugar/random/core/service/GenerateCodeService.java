package me.mikusugar.random.core.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.utils.GetAllService;
import me.mikusugar.random.core.utils.SugarJsonNodeSerialization;
import me.mikusugar.random.core.utils.SugarJsonUtils;

import java.util.Map;

/**
 * 给生成的造数代码调研
 *
 * @author mikusugar
 */
public class GenerateCodeService {
    private SugarJsonNode sugarJsonNode;

    private Map<String, AbstractRandomService> randomServiceMap;

    public GenerateCodeService(String configJson)
            throws InstantiationException, IllegalAccessException, JsonProcessingException {

        this.randomServiceMap = GetAllService.getAllService();
        this.sugarJsonNode = SugarJsonNodeSerialization.read(configJson, randomServiceMap);
    }

    /**
     * @return 生成一条随机json
     */
    public String getJson(){
        final StringBuilder res = new StringBuilder();
        SugarJsonUtils.toJsonStr(null,sugarJsonNode,res);
        return res.toString();
    }


}
