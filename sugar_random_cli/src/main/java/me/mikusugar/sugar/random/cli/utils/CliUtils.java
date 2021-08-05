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
            if (node.getName().equals("root")) {
                res.append("🍭 root 根节点").append(System.lineSeparator());
            } else {
                res.append("🍭 ")
                        .append(node.getName())
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

    /**
     * 通过jsonNode，获取完整路径
     *
     * @param jsonNode json节点
     * @return path
     */
    public static String gePath(final SugarJsonNode jsonNode) {
        if (jsonNode == null) return "";
        return gePath(jsonNode.getFather()) + "/" + jsonNode.getName();
    }

    /**
     * cd 获取到某个路径
     *
     * @param curNode  当前节点
     * @param path     路径
     * @param rootNode 跟节点
     * @return 目标节点
     */
    public static SugarJsonNode getPathNode(SugarJsonNode curNode,
                                            String path,
                                            SugarJsonNode rootNode) throws Exception {
        String[] nodes = path.split("/");
        int idx = 0;
        if (nodes[0].trim().isEmpty() && nodes.length >= 2) {
            if (nodes[1].trim().equals("root")) {
                idx = 2;
                curNode = rootNode;
            } else throw new Exception(path + " 路径不存在");
        }

        for (; idx < nodes.length; idx++) {
            final String str = nodes[idx].trim();
            if (str.equals("") || str.equals(".")) {
                continue;
            }
            if (str.equals("..")) {
                curNode = curNode.getFather();
                continue;
            }
            SugarJsonNode next = null;
            for (SugarJsonNode n : curNode.getNexts()) {
                if (n.getName().equals(str)) {
                    next = n;
                    break;
                }
            }
            if (next == null) throw new Exception(path + " 路径不存在");
            else curNode = next;
        }
        return curNode;
    }
}
