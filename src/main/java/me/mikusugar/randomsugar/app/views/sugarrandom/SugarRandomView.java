package me.mikusugar.randomsugar.app.views.sugarrandom;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.event.GodService;
import me.mikusugar.random.core.sugarenum.RANDOM_MODE;
import me.mikusugar.randomsugar.app.utils.ViewUtils;
import me.mikusugar.randomsugar.app.views.main.MainView;
import me.mikusugar.randomsugar.app.views.sugarrandom.views.ConfigView;
import me.mikusugar.randomsugar.app.views.sugarrandom.views.RandViewConfig;
import me.mikusugar.randomsugar.app.views.sugarrandom.views.StartView;

@Route(value = "sugarrandom", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("SugarRandom")
@CssImport("./views/sugarrandom/sugar-random-view.css")
@Slf4j
public class SugarRandomView extends HorizontalLayout {

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private SugarJsonNode rootNode;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private SugarJsonNode curNode;

    @Getter(AccessLevel.PRIVATE)
    @Setter(AccessLevel.PRIVATE)
    private RANDOM_MODE mode;

    public SugarRandomView(GodService godService) {
        this.rootNode =
                SugarJsonNode.builder()
                        .name("root")
                        .type(SugarJsonNode.TYPE.OBJECT)
                        .desc("默认根节点")
                        .randomServiceName(ServiceName.RANDOM_OBJ)
                        .build();
        this.curNode = rootNode;
        this.mode=RANDOM_MODE.JSON;

        val startLayout = new VerticalLayout();
        startLayout.setWidthFull();
        StartView startView = new StartView(startLayout, godService, this::getRootNode);

        val randViewLayout = new VerticalLayout();
        randViewLayout.setWidthFull();
        val randViewConfig = new RandViewConfig(randViewLayout, godService, this::getRootNode, this::getCurNode,
                startView::getArea, this::setCurNode, this::setMode, this::getMode);

        val saveConfigLayout = new VerticalLayout();
        saveConfigLayout.setWidthFull();
        new ConfigView(saveConfigLayout, godService, this::getRootNode, this::setRootNode,
                () -> ViewUtils.flushTree(randViewConfig.getTreeGrid(), this::getRootNode, this::setCurNode)
        );

        add(randViewLayout, startLayout, saveConfigLayout);
        addClassName("sugar-random-view");
        setVerticalComponentAlignment(Alignment.END, randViewLayout, startLayout, saveConfigLayout);
        ViewUtils.flushTree(randViewConfig.getTreeGrid(), this::getRootNode, this::setCurNode);
    }

}
