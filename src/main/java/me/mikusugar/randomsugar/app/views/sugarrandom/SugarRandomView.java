package me.mikusugar.randomsugar.app.views.sugarrandom;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import me.mikusugar.randomsugar.app.views.main.MainView;

@Route(value = "sugarrandom", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("SugarRandom")
@CssImport("./views/sugarrandom/sugar-random-view.css")
public class SugarRandomView extends HorizontalLayout {


  private TextArea sampleJson;
  private Button analysisButton;

  private Select<String> field;
  private Select<String> selectType;
  private TextArea detailed;
  private Button next;

  private Button preview;
  private TextArea previewRes;

  private NumberField number;
  private Button start;

  public SugarRandomView() {

    HorizontalLayout top = createHorizontalLayout();
    add(top);

    HorizontalLayout analysisLayout = createHorizontalLayout();
    sampleJson = new TextArea();
    sampleJson.setWidthFull();
    sampleJson.setLabel("样例Json");
    analysisButton = new Button("解析");
    analysisLayout.add(sampleJson, analysisButton);
    analysisLayout.setVerticalComponentAlignment(Alignment.END,sampleJson, analysisButton);
    add(analysisLayout);

    HorizontalLayout settingLayout = createHorizontalLayout();
    field = new Select<>();
    field.setLabel("字段名");
    selectType = new Select<>();
    selectType.setLabel("随机类型");
    detailed = new TextArea("随机细节");
    next = new Button("下一个");
    preview = new Button("预览");
    settingLayout.add(field, selectType, detailed, next,preview);
    settingLayout.setVerticalComponentAlignment(Alignment.END,field,selectType,detailed,next,preview);
    add(settingLayout);

    HorizontalLayout previewLayout = createHorizontalLayout();
    previewRes = new TextArea();
    previewRes.setLabel("预览结果");
    previewRes.setWidthFull();
    previewLayout.add(previewRes);
    previewLayout.setVerticalComponentAlignment(Alignment.END,previewRes);
    add(previewLayout);

    HorizontalLayout startLayout = createHorizontalLayout();
    number = new NumberField();
    number.setValue(1d);
    number.setHasControls(true);
    number.setMin(1);
    number.setMax(10000);
    number.setLabel("生成条数");
    start = new Button("开始");
    startLayout.add(number, start);
    settingLayout.setVerticalComponentAlignment(Alignment.END,number,start);
    add(startLayout);

    addClassName("sugar-random-view");
    setVerticalComponentAlignment(Alignment.END, top, analysisLayout, settingLayout, previewLayout,
        startLayout);
  }

  private HorizontalLayout createHorizontalLayout() {
    HorizontalLayout res = new HorizontalLayout();
    res.setWidthFull();
    return res;
  }

}
