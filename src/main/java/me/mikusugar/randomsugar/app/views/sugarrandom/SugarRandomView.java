package me.mikusugar.randomsugar.app.views.sugarrandom;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import com.vaadin.flow.server.StreamResource;
import lombok.val;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.constant.ServiceNameValues;
import me.mikusugar.random.core.event.*;
import me.mikusugar.random.core.utils.GenerateCodeUtil;
import me.mikusugar.random.core.utils.SugarJsonUtils;
import me.mikusugar.randomsugar.app.utils.NotionUtils;
import me.mikusugar.randomsugar.app.views.main.MainView;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "sugarrandom", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("SugarRandom")
@CssImport("./views/sugarrandom/sugar-random-view.css")
public class SugarRandomView extends HorizontalLayout {


    ///////////////////////////////////////////////////////////////////////////
    // 定义成员变量
    ///////////////////////////////////////////////////////////////////////////

    protected Logger log = LoggerFactory.getLogger(getClass());

    //配置
    private TextField configName;
    private Button readConfig;
    private Button saveConfig;
    private Button delConfig;

    //造数配置
    private TextField fieldName;
    private Select<String> randomType;
    private TextArea randomInfo;
    private Button next;
    private Button delNode;
    private TreeGrid<SugarJsonNode> treeGrid;
    private NumberField number;
    private TextArea area;

    // 结果预览
    private Dialog resPreDialog;
    private TextArea resPreTextArea;
    private Button resPreButton;

    //生成代码
    private Dialog generateCodeDialog;
    private TextArea generateCodeArea;
    private Button generateCodeButton;

    //根节点和当前节点
    private SugarJsonNode rootNode;
    private SugarJsonNode curNode;

    //事件服务
    @Autowired
    private GodService godService;

    public SugarRandomView() {
        this.rootNode =
                SugarJsonNode.builder()
                        .name("root")
                        .type(SugarJsonNode.TYPE.OBJECT)
                        .desc("默认根节点")
                        .randomServiceName(ServiceName.RANDOM_OBJ)
                        .build();
        this.curNode = rootNode;
        initView();
        even();
    }


    ///////////////////////////////////////////////////////////////////////////
    // 页面事件处理
    ///////////////////////////////////////////////////////////////////////////


    private void even() {

        readConfig.addClickListener(
                buttonClickEvent -> {
                    try {
                        val node = godService.getSugarJsonNode(configName.getValue());
                        assert node != null;
                        this.rootNode = node;
                        flushTree();
                        NotionUtils.defaultNotion(configName.getValue() + "读取成功~");
                    } catch (Exception e) {
                        log.error(e.toString());
                        NotionUtils.defaultNotion(e.getMessage());
                    }
                });

        saveConfig.addClickListener(
                buttonClickEvent -> {
                    try {
                        godService.saveConfig(configName.getValue(), rootNode);
                        NotionUtils.defaultNotion("配置[" + configName.getValue() + "]存储成功");
                    } catch (Exception e) {
                        log.error(e.toString());
                        NotionUtils.defaultNotion(e.getMessage());
                    }
                });

        delConfig.addClickListener(
                buttonClickEvent -> {
                    try {
                        godService.delConfig(configName.getValue());
                    } catch (Exception e) {
                        log.error(e.toString());
                        NotionUtils.defaultNotion(e.getMessage());
                    }
                });

        randomType.addBlurListener(
                event -> randomInfo.setHelperText(godService.getHelpStr(randomType.getValue())));

        next.addClickListener(
                (ComponentEventListener<ClickEvent<Button>>) buttonClickEvent -> {
                    try {
                        godService.check(
                                randomType.getValue(),
                                curNode,
                                randomInfo.getValue(),
                                fieldName.getValue());
                        godService.add(
                                fieldName.getValue(),
                                randomType.getValue(),
                                randomInfo.getValue(),
                                curNode);
                        NotionUtils.defaultNotion("字段配置添加成功～");
                        flushTree();
                    } catch (Exception e) {
                        NotionUtils.defaultNotion(e.getMessage());
                    }
                });

        delNode.addClickListener(e -> {
            godService.del(curNode, rootNode);
            this.curNode = this.rootNode;
            flushTree();
        });

        treeGrid.addItemClickListener(e -> area.setValue("参数说明：" + e.getItem().getDesc()));

        resPreButton.addClickListener(e -> resPreTextArea.setValue(godService.getPrettyJson(rootNode)));

        generateCodeButton.addClickListener(e -> {
            try {
                generateCodeArea.setValue(GenerateCodeUtil.getCode(rootNode));
            } catch (Exception exception) {
                exception.printStackTrace();
                log.error(exception.toString());
                NotionUtils.defaultNotion(exception.toString());
            }
        });

        treeGrid.addItemClickListener(
                event -> {
                    this.curNode = event.getItem();
                    NotionUtils.defaultNotion("当前选中的节点为：" + event.getItem().getName());
                }
        );

    }


    ///////////////////////////////////////////////////////////////////////////
    // 页面初始化
    ///////////////////////////////////////////////////////////////////////////


    private void initView() {
        HorizontalLayout top = createHorizontalLayout();
        add(top);

        final HorizontalLayout saveConfigLayout = createHorizontalLayout();
        configName = new TextField("配置名");
        readConfig = new Button("配置读取");
        saveConfig = new Button("配置存储");
        delConfig = new Button("删除配置");
        saveConfigLayout.add(configName, readConfig, saveConfig, delConfig);
        saveConfigLayout.setVerticalComponentAlignment(
                Alignment.END, configName, readConfig, saveConfig, delConfig);
        add(saveConfigLayout);

        HorizontalLayout fieldLayout = createHorizontalLayout();
        fieldName = new TextField();
        fieldName.setLabel("字段名");
        randomType = new Select<>();
        randomType.setLabel("随机类型");
        randomType.setItems(ServiceNameValues.getValues());
        next = new Button("下一个");
        delNode = new Button("删除");
        fieldLayout.add(fieldName, randomType, next, delNode);
        fieldLayout.setVerticalComponentAlignment(
                Alignment.END, fieldName, randomType, next, delNode);
        add(fieldLayout);

        HorizontalLayout randomLayout = createHorizontalLayout();
        randomInfo = new TextArea();
        randomInfo.setLabel("随机参数");
        randomInfo.setWidthFull();
        randomLayout.add(randomInfo);
        randomLayout.setVerticalComponentAlignment(Alignment.END, randomInfo);
        add(randomLayout);

        final HorizontalLayout treeName = createHorizontalLayout();
        Label label = new Label("结构预览");
        label.setWidthFull();
        treeName.add(label);
        treeName.setVerticalComponentAlignment(Alignment.END, label);
        add(treeName);

        HorizontalLayout treeLayout = createHorizontalLayout();
        treeGrid = new TreeGrid<>();
        treeGrid.setWidthFull();
        treeLayout.add(treeGrid);
        treeLayout.setVerticalComponentAlignment(Alignment.END, treeGrid);
        add(treeLayout);

        HorizontalLayout startLayout = createHorizontalLayout();
        number = new NumberField();
        number.setValue(1d);
        number.setHasControls(true);
        number.setMin(1);
        number.setMax(10000);
        number.setLabel("生成条数");
        Button start = new Button("开始");
        area = new TextArea("参数配置说明");
        area.setWidth("60%");
        area.setReadOnly(true);
        StreamResource href = new StreamResource("download.json", this::getInputStream);
        href.setCacheTime(0);
        Anchor download = new Anchor(href, "");
        download.getElement().setAttribute("开始", true);
        download.add(start);
        resPreDialog = new Dialog();
        resPreTextArea = new TextArea("结果：");
        helpDialogAndTextArea(resPreTextArea, resPreDialog);
        resPreButton = new Button("预览", e -> resPreDialog.open());

        generateCodeDialog = new Dialog();
        generateCodeArea = new TextArea("Code");
        helpDialogAndTextArea(generateCodeArea, generateCodeDialog);
        generateCodeButton = new Button("生成Java代码", e -> generateCodeDialog.open());

        startLayout.add(area, resPreButton, number, download, generateCodeButton);
        startLayout.setVerticalComponentAlignment(Alignment.END, area, resPreButton, number, download, generateCodeButton);
        add(startLayout);

        addClassName("sugar-random-view");
        setVerticalComponentAlignment(
                Alignment.END,
                top,
                saveConfigLayout,
                fieldLayout,
                randomLayout,
                treeName,
                treeLayout,
                startLayout);
        flushTree();
    }


    ///////////////////////////////////////////////////////////////////////////
    // 工具方法
    ///////////////////////////////////////////////////////////////////////////

    private void helpDialogAndTextArea(TextArea textArea, Dialog dialog) {
        textArea.setWidthFull();
        textArea.setHeight("90%");
        dialog.add(textArea);
        final Button closePreButton = new Button("关闭", e -> dialog.close());
        closePreButton.setWidthFull();
        dialog.add(closePreButton);
        dialog.setHeight("80%");
        dialog.setWidth("80%");
    }

    private InputStream getInputStream() {
        val res = new StringBuilder();
        int size = number.getValue().intValue();
        System.out.println(size);
        while (size-- > 0) {
            SugarJsonUtils.toJsonStr(null, rootNode, res);
            res.append(",").append("\n");
        }
        res.deleteCharAt(res.lastIndexOf(","));
        try {
            return IOUtils.toInputStream(res.toString(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 刷新结构预览界面
     */
    private void flushTree() {
        treeGrid.removeAllColumns();
        treeGrid.setItems(Collections.singletonList(rootNode), SugarJsonNode::getNexts);
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
        treeGrid.expand(getList(rootNode));
        this.curNode = rootNode;
    }

    /**
     * 扁平化展开
     *
     * @param rootNode 根节点
     * @return List
     */
    private List<SugarJsonNode> getList(SugarJsonNode rootNode) {
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

    private HorizontalLayout createHorizontalLayout() {
        HorizontalLayout res = new HorizontalLayout();
        res.setWidth("98%");
        return res;
    }


}
