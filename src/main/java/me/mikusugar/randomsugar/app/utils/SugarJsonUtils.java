package me.mikusugar.randomsugar.app.utils;

import me.mikusugar.randomsugar.app.bean.SugarJsonNode;

public class SugarJsonUtils {
    public static void toJsonStr(SugarJsonNode father, SugarJsonNode node, StringBuilder sb) {
        if (node.getType() == SugarJsonNode.TYPE.OBJECT) {
            if (node.getName().equals("root")) sb.append("{");
            else sb.append(helpName(father,node.getName())).append("{");
            node.getNexts()
                    .forEach(
                            n -> {
                                toJsonStr(node,n, sb);
                                sb.append(",");
                            });
            if (node.getNexts().size() > 0) sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append("}").append("\n");
        } else if (node.getType() == SugarJsonNode.TYPE.STRING) {
            sb.append(helpName(father,node.getName()))
                    .append("\"")
                    .append(node.getRandomService().next())
                    .append("\"");
        } else if (node.getType() == SugarJsonNode.TYPE.LONG
                || node.getType() == SugarJsonNode.TYPE.INT
                || node.getType() == SugarJsonNode.TYPE.DOUBLE
                || node.getType() == SugarJsonNode.TYPE.BOOLEAN) {
            sb.append(helpName(father,node.getName())).append(node.getRandomService().next().toString());
        } else if (node.getType() == SugarJsonNode.TYPE.NULL) {
            sb.append(helpName(father,node.getName())).append("null");
        } else if (node.getType() == SugarJsonNode.TYPE.ARRAY) {
            sb.append(helpName(father,node.getName())).append("[");
            int size = (int) node.getRandomService().next();
            while (size-- > 0) {
                toJsonStr(node,node.getNexts().get(0), sb);
                sb.append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append("]");
        }
    }

    private static String helpName(SugarJsonNode father,String name) {
        if(father!=null&& SugarJsonNode.TYPE.ARRAY.equals(father.getType()))return "";
        return "\"" + name + "\":";
    }
}
