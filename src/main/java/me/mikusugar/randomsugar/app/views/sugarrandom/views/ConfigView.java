package me.mikusugar.randomsugar.app.views.sugarrandom.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.event.GodService;
import me.mikusugar.randomsugar.app.utils.ViewUtils;
import me.mikusugar.randomsugar.app.func.BehaviorFunc;
import me.mikusugar.randomsugar.app.func.GetValueFunc;
import me.mikusugar.randomsugar.app.func.SetValueFunc;

/**
 * @author mikusugar
 */
@Getter
@Slf4j
public class ConfigView {

    private final TextField configName;
    private final Button readConfig;
    private final Button saveConfig;
    private final Button delConfig;


    public ConfigView(
            VerticalLayout layout,
            GodService godService,
            GetValueFunc<SugarJsonNode> getRootNode,
            SetValueFunc<SugarJsonNode> setRootNode,
            BehaviorFunc flushTree
    ) {

        //view
        HorizontalLayout horizontalLayout = ViewUtils.createHorizontalLayout();
        configName = new TextField("配置名");
        readConfig = new Button("配置读取");
        saveConfig = new Button("配置存储");
        delConfig = new Button("删除配置");
        horizontalLayout.add(configName, readConfig, saveConfig, delConfig);
        horizontalLayout.setVerticalComponentAlignment(
                FlexComponent.Alignment.END, configName, readConfig, saveConfig, delConfig);
        layout.add(horizontalLayout);


        //event
        readConfig.addClickListener(
                buttonClickEvent -> {
                    try {
                        val node = godService.getSugarJsonNode(configName.getValue());
                        assert node != null;
                        setRootNode.setValue(node);
                        flushTree.behavior();
                        ViewUtils.defaultNotion(configName.getValue() + "读取成功~");
                    } catch (Exception e) {
                        log.error(e.toString());
                        ViewUtils.defaultNotion(e.getMessage());
                    }
                });

        saveConfig.addClickListener(
                buttonClickEvent -> {
                    try {
                        godService.saveConfig(configName.getValue(), getRootNode.getValue());
                        ViewUtils.defaultNotion("配置[" + configName.getValue() + "]存储成功");
                    } catch (Exception e) {
                        log.error(e.toString());
                        ViewUtils.defaultNotion(e.getMessage());
                    }
                });

        delConfig.addClickListener(
                buttonClickEvent -> {
                    try {
                        godService.delConfig(configName.getValue());
                    } catch (Exception e) {
                        log.error(e.toString());
                        ViewUtils.defaultNotion(e.getMessage());
                    }
                });

    }

}
