package me.mikusugar.randomsugar.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import me.mikusugar.randomsugar.app.bean.SugarJsonNode;
import me.mikusugar.randomsugar.app.bean.SugarJsonNode.TYPE;
import me.mikusugar.randomsugar.app.utils.NotionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * author: fangjie email: syfangjie@live.cn date: 2021/4/1 3:21 下午
 */
@Service
public class AnalysisService {

  protected Logger log = LoggerFactory.getLogger(getClass());

  private ObjectMapper mapper = new ObjectMapper();

  public SugarJsonNode jsonStr2JsonNode(String jsonStr) {
    try {
      JsonNode jsonNode = mapper.readTree(jsonStr);
      return jsonNode2SugarNode(jsonNode,"");
    } catch (JsonProcessingException e) {
      log.error("json 字符串解析失败，json str{}",jsonStr);
      NotionUtils.defaultNotion("字符串解析失败！");
      e.printStackTrace();
    }
    return null;
  }

  private SugarJsonNode jsonNode2SugarNode(JsonNode jsonNode,String name) {
    SugarJsonNode sugarJsonNode = new SugarJsonNode();
    sugarJsonNode.setName(name);
    JsonNodeType nodeType = jsonNode.getNodeType();
    sugarJsonNode.setValue(jsonNode.asText());

    if(nodeType==JsonNodeType.NULL){
      sugarJsonNode.setType(TYPE.NULL);
    }
    else if(nodeType==JsonNodeType.OBJECT){
      sugarJsonNode.setType(TYPE.OBJECT);
      Map<String, SugarJsonNode> map = new HashMap<>();
      sugarJsonNode.setFieldMap(map);
      Iterator<Entry<String, JsonNode>> fields = jsonNode.fields();
      while (fields.hasNext()){
        Entry<String, JsonNode> cur = fields.next();
        map.put(cur.getKey(),jsonNode2SugarNode(cur.getValue(),cur.getKey()));
      }
    }
    else if(nodeType==JsonNodeType.BOOLEAN){
      sugarJsonNode.setType(TYPE.BOOLEAN);
    }
    else if(nodeType==JsonNodeType.NUMBER){
      sugarJsonNode.setType(TYPE.NUMBER);
    }
    else if(nodeType==JsonNodeType.ARRAY){
      sugarJsonNode.setType(TYPE.ARRAY);
      Map<String,SugarJsonNode> map=new HashMap<>();
      map.put("",jsonNode2SugarNode(jsonNode.get(0),""));
      sugarJsonNode.setFieldMap(map);
    }
    else if(nodeType==JsonNodeType.STRING){
      sugarJsonNode.setType(TYPE.STRING);
    }
    return sugarJsonNode;
  }

  public static void main(String[] args) throws JsonProcessingException {
    AnalysisService service = new AnalysisService();
    SugarJsonNode sugarJsonNode = service.jsonStr2JsonNode(
        "{\"username\":\"zhangsan\",\"性别\":\"男\",\"company\":{\"companyName\":\"微软\",\"address\":\"美国\"},\"cars\":[\"电车\",\"单车\"]}");
    System.out.println(sugarJsonNode);
  }
}

