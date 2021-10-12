package me.mikusugar.randomsugar.app.utils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.treegrid.TreeGrid;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.randomsugar.app.func.GetValueFunc;
import me.mikusugar.randomsugar.app.func.SetValueFunc;

import java.util.*;

/**
 * @author mikusugar
 */
public class ViewUtils {

    public static HorizontalLayout createHorizontalLayout() {
        HorizontalLayout res = new HorizontalLayout();
        res.setWidth("98%");
        return res;
    }

    public static void helpDialogAndTextArea(TextArea textArea, Dialog dialog) {
        textArea.setWidthFull();
        textArea.setHeight("90%");
        dialog.add(textArea);
        final Button closePreButton = new Button("关闭", e -> dialog.close());
        closePreButton.setWidthFull();
        dialog.add(closePreButton);
        dialog.setHeight("80%");
        dialog.setWidth("80%");
    }

    public static void defaultNotion(String message) {
        Notification.show(message, 5000, Notification.Position.TOP_CENTER);
    }

    public static void flushTree(
            TreeGrid<SugarJsonNode> treeGrid,
            GetValueFunc<SugarJsonNode> getRootNode,
            SetValueFunc<SugarJsonNode> setCurNode

    ) {
        treeGrid.removeAllColumns();
        treeGrid.setItems(Collections.singletonList(getRootNode.getValue()), SugarJsonNode::getNexts);
        treeGrid.addHierarchyColumn(SugarJsonNode::getName).setHeader("字段名");
        treeGrid.addColumn(SugarJsonNode::getType).setHeader("字段类型");
        treeGrid.addColumn(SugarJsonNode::getRandomServiceName).setHeader("随机类型");
        treeGrid
                .addColumn(
                        sugarJsonNode -> {
                            if (sugarJsonNode == null
                                    || sugarJsonNode.getRandomService() == null
                                    || sugarJsonNode.getRandomService().getInput() == null)
                                return "没有配置";
                            return sugarJsonNode.getRandomService().getInput();
                        })
                .setHeader("参数配置");
        treeGrid.addColumn(SugarJsonNode::getDesc).setHeader("参数说明");
        treeGrid.expand(getList(getRootNode.getValue()));
        setCurNode.setValue(getRootNode.getValue());
    }

    private static List<SugarJsonNode> getList(SugarJsonNode rootNode) {
        final Queue<SugarJsonNode> queue = new ArrayDeque<>();
        final List<SugarJsonNode> res = new ArrayList<>();
        queue.add(rootNode);
        while (!queue.isEmpty()) {
            final SugarJsonNode p = queue.poll();
            res.add(p);
            if (p.getNexts() != null) {
                queue.addAll(p.getNexts());
            }
        }
        return res;
    }
}
