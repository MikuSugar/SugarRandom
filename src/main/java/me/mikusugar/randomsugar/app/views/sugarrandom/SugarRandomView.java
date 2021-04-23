package me.mikusugar.randomsugar.app.views.sugarrandom;

import com.vaadin.flow.component.BlurNotifier.BlurEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.*;

import lombok.val;
import me.mikusugar.randomsugar.app.bean.SugarJsonNode;
import me.mikusugar.randomsugar.app.bean.SugarJsonNode.TYPE;
import me.mikusugar.randomsugar.app.constant.ServiceName;
import me.mikusugar.randomsugar.app.service.AbstractRandomService;
import me.mikusugar.randomsugar.app.utils.NotionUtils;
import me.mikusugar.randomsugar.app.views.main.MainView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "sugarrandom", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("SugarRandom")
@CssImport("./views/sugarrandom/sugar-random-view.css")
public class SugarRandomView extends HorizontalLayout {

  protected Logger log = LoggerFactory.getLogger(getClass());

  private TextField fieldName;
  private TextField fieldFather;
  private Select<String> filedType;
  private Select<String> randomType;
  private TextArea randomInfo;
  private Button next;
  private TreeGrid<SugarJsonNode> treeGrid;
  private NumberField number;
  private Button start;
  private Map<String, SugarJsonNode> map;
  private SugarJsonNode rootNode;

  @Autowired private Map<String, AbstractRandomService> randomServiceMap;

  public SugarRandomView() {
    this.rootNode = SugarJsonNode.builder().name("root").type(TYPE.OBJECT).desc("默认根节点").build();
    this.map = new HashMap<>();
    this.map.put("root", rootNode);
    initView();
    even();
  }

  private void even() {

    randomType.addBlurListener(
        (ComponentEventListener<BlurEvent<Select<String>>>)
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
            val node =
                SugarJsonNode.builder()
                    .name(fieldName.getValue())
                    .type(TYPE.valueOf(filedType.getValue()))
                    .randomService(
                        randomServiceMap
                            .get(randomType.getValue())
                            .createRandomUtilInterface(randomInfo.getValue()))
                    .desc(randomServiceMap.get(randomType.getValue()).helpText())
                    .build();
            map.get(fieldFather.getValue()).getNexts().add(node);
            map.put(node.getName(), node);
            flushTree();
          }

          private boolean checkNext() {
            if (!randomServiceMap
                .get(randomType.getValue())
                .check(filedType.getValue(), randomInfo.getValue().trim())) return false;
            return !map.containsKey(fieldName.getValue()) && map.containsKey(fieldFather.getValue());
          }
        });
  }

  /** 界面初始化 */
  private void initView() {
    HorizontalLayout top = createHorizontalLayout();
    add(top);
    HorizontalLayout fieldLayout = createHorizontalLayout();
    fieldName = new TextField();
    fieldName.setLabel("字段名");
    fieldFather = new TextField();
    fieldFather.setLabel("字段父亲");
    fieldFather.setValue("root");
    filedType = new Select<>();
    filedType.setLabel("字段类型");
    filedType.setItems(Arrays.stream(TYPE.values()).map(String::valueOf));
    randomType = new Select<>();
    randomType.setLabel("随机类型");
    randomType.setItems(ServiceName.values);
    next = new Button("下一个");
    fieldLayout.add(fieldName, fieldFather, filedType, randomType, next);
    fieldLayout.setVerticalComponentAlignment(
        Alignment.END, fieldName, fieldFather, filedType, randomType, next);
    add(fieldLayout);

    HorizontalLayout randomLayout = createHorizontalLayout();
    randomInfo = new TextArea();
    randomInfo.setLabel("随机参数");
    randomInfo.setWidthFull();
    randomLayout.add(randomInfo);
    randomLayout.setVerticalComponentAlignment(Alignment.END, randomInfo);
    add(randomLayout);

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
    startLayout.add(number, start);
    startLayout.setVerticalComponentAlignment(Alignment.END, number, start);
    add(startLayout);

    addClassName("sugar-random-view");
    setVerticalComponentAlignment(
        Alignment.END, top, fieldLayout, randomLayout, treeLayout, startLayout);
    flushTree();
  }

  private void flushTree() {
    treeGrid.removeAllColumns();
    treeGrid.setItems(Collections.singletonList(rootNode), SugarJsonNode::getNexts);
    treeGrid.addHierarchyColumn(SugarJsonNode::getName).setHeader("字段名");
    treeGrid.addColumn(SugarJsonNode::getType).setHeader("类型");
    treeGrid.addColumn(SugarJsonNode::getDesc).setHeader("描述");
  }

  private HorizontalLayout createHorizontalLayout() {
    HorizontalLayout res = new HorizontalLayout();
    res.setWidthFull();
    return res;
  }
}
