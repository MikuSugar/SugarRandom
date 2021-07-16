package me.mikusugar.sugar.random.cli.utils;

import me.mikusugar.random.core.bean.SugarJsonNode;

/**
 * @author mikusugar
 */
public class CliUtils {

    /**
     * 生成一颗树的形状
     *
     * @param rootNode 根节点
     * @return 树形状的字符串
     */
    public static String JsonNodeToTreeStr(SugarJsonNode rootNode) {
        StringBuilder res = new StringBuilder();
        dfs(rootNode, res, 0);
        return res.toString();
    }

    /**
     * 生成一颗🌲
     *
     * @param node  节点对象
     * @param res   🌲的存储
     * @param level 层数
     */
    private static void dfs(SugarJsonNode node, StringBuilder res, final int level) {
        if (node == null) return;
        if (level == 0) {
            res.append("🍭 root 根节点").append(System.lineSeparator());
        } else {
            int num = level;
            while (num-- > 1) res.append("│  ");
            res.append("├──");
            res.append(node.getName())
                    .append(" ")
                    .append(node.getType())
                    .append(" ")
                    .append(node.getRandomServiceName())
                    .append(" ");
            String input;
            if (node.getRandomService() == null
                    || node.getRandomService().getInput() == null) input = "没有配置";
            else input = node.getRandomService().getInput();
            res.append(input).append(System.lineSeparator());
        }
        if (node.getNexts() != null) {
            node.getNexts().forEach(next -> dfs(next, res, level + 1));
        }
    }

    public static String showField(String field) {
        return "【" + field + "】";
    }


    /**
     * 找到字段的父亲
     *
     * @param rootNode 根节点
     * @param field    字段
     * @return 父亲字段
     */
    public static SugarJsonNode findFather(SugarJsonNode rootNode, String field) {
        if (rootNode == null) return null;
        if (rootNode.getNexts() != null) {
            for (SugarJsonNode next : rootNode.getNexts()) {
                if (next.getName().equals(field)) return rootNode;
            }
            for (SugarJsonNode next : rootNode.getNexts()) {
                final SugarJsonNode res = findFather(next, field);
                if (res != null) return res;
            }
        }
        return null;
    }
}
