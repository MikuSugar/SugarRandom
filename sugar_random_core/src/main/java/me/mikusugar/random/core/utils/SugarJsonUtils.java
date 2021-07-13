package me.mikusugar.random.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.mikusugar.random.core.bean.SugarJsonNode;

public class SugarJsonUtils {
    public static void toJsonStr(SugarJsonNode father, SugarJsonNode node, StringBuilder sb) {
        if (node.getType() == SugarJsonNode.TYPE.OBJECT) {
            if (node.getName().equals("root")) sb.append("{");
            else sb.append(helpName(father, node.getName())).append("{");
            node.getNexts()
                    .forEach(
                            n -> {
                                toJsonStr(node, n, sb);
                                sb.append(",");
                            });
            if (node.getNexts().size() > 0) sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append("}").append("\n");
        } else if (node.getType() == SugarJsonNode.TYPE.STRING) {
            sb.append(helpName(father, node.getName()))
                    .append("\"")
                    .append(node.getRandomService().getRandomUtilInterface().next())
                    .append("\"");
        } else if (node.getType() == SugarJsonNode.TYPE.LONG
                || node.getType() == SugarJsonNode.TYPE.INT
                || node.getType() == SugarJsonNode.TYPE.DOUBLE
                || node.getType() == SugarJsonNode.TYPE.BOOLEAN) {
            sb.append(helpName(father, node.getName())).append(node.getRandomService().getRandomUtilInterface().next().toString());
        } else if (node.getType() == SugarJsonNode.TYPE.NULL) {
            sb.append(helpName(father, node.getName())).append("null");
        } else if (node.getType() == SugarJsonNode.TYPE.ARRAY) {
            sb.append(helpName(father, node.getName())).append("[");
            int size = (int) node.getRandomService().getRandomUtilInterface().next();
            while (size-- > 0) {
                toJsonStr(node, node.getNexts().get(0), sb);
                sb.append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append("]");
        }
    }

    private static String helpName(SugarJsonNode father, String name) {
        if (father != null && SugarJsonNode.TYPE.ARRAY.equals(father.getType())) return "";
        return "\"" + name + "\":";
    }

    public static String json2PrettyFormat(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(
                            mapper.readValue(json, Object.class)
                    );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return json;
        }
    }
}
