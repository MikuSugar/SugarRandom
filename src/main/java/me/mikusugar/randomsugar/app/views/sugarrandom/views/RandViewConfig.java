package me.mikusugar.randomsugar.app.views.sugarrandom.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import lombok.Getter;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.constant.ServiceNameValues;
import me.mikusugar.random.core.event.GodService;
import me.mikusugar.random.core.sugarenum.RANDOM_MODE;
import me.mikusugar.randomsugar.app.utils.ViewUtils;
import me.mikusugar.randomsugar.app.func.GetValueFunc;
import me.mikusugar.randomsugar.app.func.SetValueFunc;


/**
 * @author mikusugar
 */
@Getter
public class RandViewConfig {

    private final Select<String> selectMode;
    private final TextField fieldName;
    private final Select<String> randomType;
    private final Button next;
    private final Button delNode;
    private final TextArea randomInfo;
    private final TreeGrid<SugarJsonNode> treeGrid;

    public RandViewConfig(
            VerticalLayout layout,
            GodService godService,
            GetValueFunc<SugarJsonNode> getRootNode,
            GetValueFunc<SugarJsonNode> getCurNode,
            GetValueFunc<TextArea> getStartViewTextArea,
            SetValueFunc<SugarJsonNode> setCurNode,
            SetValueFunc<RANDOM_MODE> setRandMod,
            GetValueFunc<RANDOM_MODE> getRandMod
    ) {

        //view
        HorizontalLayout fieldLayout = ViewUtils.createHorizontalLayout();
        selectMode = new Select<>();
        selectMode.setItems(RANDOM_MODE.JSON.getData(), RANDOM_MODE.CSV.getData());
        selectMode.setValue(RANDOM_MODE.JSON.getData());
        selectMode.setLabel("随机模式");
        fieldName = new TextField();
        fieldName.setLabel("字段名");
        randomType = new Select<>();
        randomType.setLabel("随机类型");
        randomType.setItems(ServiceNameValues.getValues());
        next = new Button("下一个");
        delNode = new Button("删除");
        fieldLayout.add(selectMode, fieldName, randomType, next, delNode);
        fieldLayout.setVerticalComponentAlignment(
                FlexComponent.Alignment.END, selectMode, fieldName, randomType, next, delNode);
        layout.add(fieldLayout);

        HorizontalLayout randomLayout = ViewUtils.createHorizontalLayout();
        randomInfo = new TextArea();
        randomInfo.setLabel("随机参数");
        randomInfo.setWidthFull();
        randomLayout.add(randomInfo);
        randomLayout.setVerticalComponentAlignment(FlexComponent.Alignment.END, randomInfo);
        layout.add(randomLayout);

        final HorizontalLayout treeName = ViewUtils.createHorizontalLayout();
        Label label = new Label("结构预览");
        label.setWidthFull();
        treeName.add(label);
        treeName.setVerticalComponentAlignment(FlexComponent.Alignment.END, label);
        layout.add(treeName);

        HorizontalLayout treeLayout = ViewUtils.createHorizontalLayout();
        treeGrid = new TreeGrid<>();
        treeGrid.setWidthFull();
        treeLayout.add(treeGrid);
        treeLayout.setVerticalComponentAlignment(FlexComponent.Alignment.END, treeGrid);
        layout.add(treeLayout);

        //event
        next.addClickListener(
                (ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
                    try {
                        godService.check(
                                randomType.getValue(),
                                getCurNode.getValue(),
                                randomInfo.getValue(),
                                fieldName.getValue());
                        godService.add(
                                fieldName.getValue(),
                                randomType.getValue(),
                                randomInfo.getValue(),
                                getCurNode.getValue());
                        ViewUtils.defaultNotion("字段配置添加成功～");
                        ViewUtils.flushTree(treeGrid, getRootNode, setCurNode);
                    } catch (Exception e) {
                        ViewUtils.defaultNotion(e.getMessage());
                    }
                });

        delNode.addClickListener(e -> {
            godService.del(getCurNode.getValue(), getRootNode.getValue());
            setCurNode.setValue(getRootNode.getValue());
            ViewUtils.flushTree(treeGrid, getRootNode, setCurNode);
        });

        randomType.addBlurListener(
                event -> randomInfo.setHelperText(
                        godService.getHelpStr(randomType.getValue())
                ));

        treeGrid.addItemClickListener(
                e -> getStartViewTextArea.getValue().setValue("参数说明：" + e.getItem().getDesc())
        );

        treeGrid.addItemClickListener(
                event -> {
                    setCurNode.setValue(event.getItem());
                    ViewUtils.defaultNotion("当前选中的节点为：" + event.getItem().getName());
                }
        );

        selectMode.addValueChangeListener(event -> {

            Dialog dialog = new Dialog();
            dialog.add(new Text("更改模式将重置配置，是否继续？"));
            dialog.setCloseOnEsc(false);
            dialog.setCloseOnOutsideClick(false);
            Span message = new Span();

            Button confirmButton = new Button("继续", e -> {
                message.setText("yes");
                dialog.close();
            });
            Button cancelButton = new Button("取消", e -> {
                message.setText("no");
                dialog.close();
            });

            dialog.add(new Div(confirmButton, cancelButton));
            dialog.open();
            if (message.getText().equals("yes")) {
                setRandMod.setValue(RANDOM_MODE.valueOf(event.getValue()));
                ViewUtils.defaultNotion("随机模式更换为:" + event.getValue());
            } else {
                getSelectMode().setValue(getRandMod.getValue().getData());
                ViewUtils.defaultNotion("随机模式不更换:" + event.getValue());
            }
        });

    }


}
