package me.mikusugar.randomsugar.app.views.about;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import me.mikusugar.randomsugar.app.utils.Version;
import me.mikusugar.randomsugar.app.views.main.MainView;
import com.vaadin.flow.component.dependency.CssImport;

@Route(value = "about", layout = MainView.class)
@PageTitle("About")
@CssImport("./views/about/about-view.css")
public class AboutView extends Div {

    public AboutView() {
        addClassName("about-view");
        final HorizontalLayout top = createHorizontalLayout();
        add(top);

        final VerticalLayout infoLayout = new VerticalLayout();
        infoLayout.setWidthFull();
        infoLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        add(infoLayout);
        infoLayout.add(new Label("Version："+ Version.getVersion()));
        infoLayout.add(new Anchor("https://mikusugar.me/SugarRandom/","SugarRandom 官网"));

        final HorizontalLayout imageMiku = createHorizontalLayout();
        imageMiku.setWidthFull();
        imageMiku.add(new Label("  "));
        final Image miku = new Image();
        miku.setSrc("https://cdn.jsdelivr.net/gh/mikusugar/PictureBed@master/uPic/2021/06/p6jHN7.jpg");
        imageMiku.add(miku);
        imageMiku.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        add(imageMiku);


    }

    private HorizontalLayout createHorizontalLayout() {
        HorizontalLayout res = new HorizontalLayout();
        res.setWidthFull();
        return res;
    }

}
