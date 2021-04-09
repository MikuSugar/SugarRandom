package me.mikusugar.randomsugar.app.views.sugarrandom;

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
import java.util.Arrays;
import me.mikusugar.randomsugar.app.bean.SugarJsonNode;
import me.mikusugar.randomsugar.app.bean.SugarJsonNode.TYPE;
import me.mikusugar.randomsugar.app.views.main.MainView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  public SugarRandomView() {

    HorizontalLayout top = createHorizontalLayout();
    add(top);
    HorizontalLayout fieldLayout = createHorizontalLayout();
    fieldName=new TextField();
    fieldName.setLabel("字段名");
    fieldFather=new TextField();
    fieldFather.setLabel("字段父亲");
    fieldFather.setValue("root");
    filedType=new Select<>();
    filedType.setLabel("字段类型");
    filedType.setItems(Arrays.stream(TYPE.values()).map(String::valueOf));
    randomType=new Select<>();
    randomType.setLabel("随机类型");
    next=new Button("下一个");
    fieldLayout.add(fieldName,fieldFather,filedType,randomType,next);
    fieldLayout.setVerticalComponentAlignment(Alignment.END,fieldName,fieldFather,filedType,randomType,next);
    add(fieldLayout);

    HorizontalLayout randomLayout = createHorizontalLayout();
    randomInfo=new TextArea();
    randomInfo.setLabel("随机参数");
    randomInfo.setWidthFull();
    randomLayout.add(randomInfo);
    randomLayout.setVerticalComponentAlignment(Alignment.END,randomInfo);
    add(randomLayout);

    HorizontalLayout treeLayout  = createHorizontalLayout();
    treeGrid=new TreeGrid<>();
    treeGrid.setWidthFull();
    treeLayout.add(treeGrid);
    treeLayout.setVerticalComponentAlignment(Alignment.END,treeGrid);
    add(treeLayout);

    HorizontalLayout startLayout = createHorizontalLayout();
    number = new NumberField();
    number.setValue(1d);
    number.setHasControls(true);
    number.setMin(1);
    number.setMax(10000);
    number.setLabel("生成条数");
    start = new Button("开始");
    startLayout.add(number,start);
    startLayout.setVerticalComponentAlignment(Alignment.END,number,start);
    add(startLayout);

    addClassName("sugar-random-view");
    setVerticalComponentAlignment(Alignment.END, top,fieldLayout,randomLayout,treeLayout,startLayout);
  }

  private HorizontalLayout createHorizontalLayout() {
    HorizontalLayout res = new HorizontalLayout();
    res.setWidthFull();
    return res;
  }

}
