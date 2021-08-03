package me.mikusugar.randomsugar.app.views.sugarrandom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ItemClickEvent;
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
import me.mikusugar.random.core.bean.ConfigSave;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.constant.ServiceNameValues;
import me.mikusugar.random.core.service.AbstractRandomService;
import me.mikusugar.random.core.service.ConfigSavaRepository;
import me.mikusugar.random.core.utils.GenerateCodeUtil;
import me.mikusugar.random.core.utils.SugarJsonNodeSerialization;
import me.mikusugar.random.core.utils.SugarJsonUtils;
import me.mikusugar.randomsugar.app.utils.NotionUtils;
import me.mikusugar.randomsugar.app.views.main.MainView;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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

    private TextField configName;
    private Button readConfig;
    private Button saveConfig;
    private Button delConfig;

    private TextField fieldName;
    private Select<String> randomType;
    private TextArea randomInfo;
    private Button next;
    private Button delNode;
    private Label label;
    private TreeGrid<SugarJsonNode> treeGrid;
    private NumberField number;
    private Button start;
    private SugarJsonNode rootNode;
    private SugarJsonNode curNode;
    private TextArea area;

    // 结果预览
    private Dialog resPreDialog;
    private TextArea resPreTextArea;
    private Button resPreButton;

    //生成代码
    private Dialog generateCodeDialog;
    private TextArea generateCodeArea;
    private Button generateCodeButton;

    @Autowired
    private Map<String, AbstractRandomService> randomServiceMap;

    @Autowired
    private ConfigSavaRepository configSavaRepository;

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
                    if (StringUtils.isEmpty(configName.getValue())) NotionUtils.defaultNotion("配置名为空！！！无法读取");
                    else {
                        final Optional<ConfigSave> savaRepositoryById =
                                configSavaRepository.findById(configName.getValue());
                        if (savaRepositoryById.isPresent()) {
                            try {
                                rootNode =
                                        SugarJsonNodeSerialization.read(
                                                savaRepositoryById.get().getJson(), randomServiceMap);
                                flushTree();
                                NotionUtils.defaultNotion("配置[" + configName.getValue() + "]读取成功");
                            } catch (JsonProcessingException e) {
                                NotionUtils.defaultNotion("配置存在问题");
                                log.error(savaRepositoryById.get().getJson() + e);
                            }
                        } else NotionUtils.defaultNotion("没有找到配置");
                    }
                });

        saveConfig.addClickListener(
                buttonClickEvent -> {
                    if (StringUtils.isEmpty(configName.getValue())) NotionUtils.defaultNotion("配置名为空！！！无法存储");
                    else {
                        try {
                            final String json = SugarJsonNodeSerialization.write(rootNode);
                            ConfigSave configSave = new ConfigSave();
                            configSave.setId(configName.getValue());
                            configSave.setJson(json);
                            configSavaRepository.save(configSave);
                            NotionUtils.defaultNotion("配置[" + configName.getValue() + "]存储成功");
                        } catch (JsonProcessingException e) {
                            log.error(e.toString());
                        }
                    }
                });

        delConfig.addClickListener(
                buttonClickEvent -> {
                    if (StringUtils.isEmpty(configName.getValue())) NotionUtils.defaultNotion("配置名为空！！！无法删除");
                    else {
                        configSavaRepository.deleteById(configName.getValue());
                        NotionUtils.defaultNotion("配置[" + configName.getValue() + "]删除成功");
                    }
                });

        randomType.addBlurListener(
                event -> {
                    AbstractRandomService randomService = randomServiceMap.get(randomType.getValue());
                    if (randomService == null) return;
                    randomInfo.setHelperText(randomService.helpText());
                });

        next.addClickListener(
                new ComponentEventListener<ClickEvent<Button>>() {
                    @Override
                    public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                        if (!checkNext()) {
                            NotionUtils.defaultNotion("请检查参数是否合法！");
                            return;
                        }
                        treeGrid.getSelectedItems();
                        val node =
                                SugarJsonNode.builder()
                                        .name(fieldName.getValue().trim())
                                        .type(randomServiceMap.get(randomType.getValue()).getType())
                                        .randomServiceName(randomType.getValue())
                                        .randomService(
                                                randomServiceMap
                                                        .get(randomType.getValue())
                                                        .createRandomCoreService(randomInfo.getValue()))
                                        .desc(randomServiceMap.get(randomType.getValue()).helpText())
                                        .father(curNode)
                                        .build();
                        curNode.getNexts().add(node);
                        flushTree();
                        NotionUtils.defaultNotion("字段配置添加成功～");
                    }

                    private boolean checkNext() {
                        if (!randomServiceMap
                                .get(randomType.getValue())
                                .check(randomInfo.getValue().trim())) return false;

                        if (!(curNode.getType().equals(SugarJsonNode.TYPE.OBJECT) ||
                                curNode.getType().equals(SugarJsonNode.TYPE.ARRAY))) {
                            return false;
                        }

                        if (curNode.getType().equals(SugarJsonNode.TYPE.ARRAY)) {
                            if (curNode.getNexts().size() > 0) return false;
                        }

                        for (SugarJsonNode node : curNode.getNexts()) {
                            if (node.getName().equals(fieldName.getValue().trim())) return false;
                        }
                        return true;
                    }
                });

        delNode.addClickListener(e -> {
            if (curNode.equals(rootNode)) {
                this.rootNode.getNexts().forEach(node -> node.setFather(null));
                this.rootNode.getNexts().clear();
                this.curNode = this.rootNode;
            } else {
                curNode.getFather().getNexts().remove(curNode);
                curNode.setFather(null);
            }
            flushTree();
        });

        treeGrid.addItemClickListener(e -> area.setValue("参数说明：" + e.getItem().getDesc()));

        resPreButton.addClickListener(e -> {
            final StringBuilder res = new StringBuilder();
            SugarJsonUtils.toJsonStr(null, rootNode, res);
            final String prettyFormatJson = SugarJsonUtils.json2PrettyFormat(res.toString());
            resPreTextArea.setValue(prettyFormatJson);
        });

        generateCodeButton.addClickListener(e -> {
            try {
                generateCodeArea.setValue(GenerateCodeUtil.getCode(rootNode));
            } catch (Exception exception) {
                exception.printStackTrace();
                NotionUtils.defaultNotion(exception.toString());
            }
        });

        treeGrid.addItemClickListener(
                (ComponentEventListener<ItemClickEvent<SugarJsonNode>>) event -> curNode = event.getItem()
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
        label = new Label("结构预览");
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
        start = new Button("开始");
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
            return IOUtils.toInputStream(res.toString(), "GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

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
                                    || sugarJsonNode.getRandomService().getInput() == null) return "没有配置";
                            return sugarJsonNode.getRandomService().getInput();
                        })
                .setHeader("参数配置");
        treeGrid.addColumn(SugarJsonNode::getDesc).setHeader("参数说明");
        this.curNode = rootNode;
    }

    private HorizontalLayout createHorizontalLayout() {
        HorizontalLayout res = new HorizontalLayout();
        res.setWidth("98%");
        return res;
    }


}
