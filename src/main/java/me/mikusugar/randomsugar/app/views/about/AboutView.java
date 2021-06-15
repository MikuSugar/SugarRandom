package me.mikusugar.randomsugar.app.views.about;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
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
//
//        final HorizontalLayout log = createHorizontalLayout();
//        final Image logImage = new Image();
//        logImage.setSrc("https://cdn.jsdelivr.net/gh/mikusugar/PictureBed@master/uPic/2021/06/logo.gif");
//        log.add(logImage);
//        add(log);

        final HorizontalLayout imageMiku = createHorizontalLayout();
        final Image miku = new Image();
        miku.setSrc("https://cdn.jsdelivr.net/gh/mikusugar/PictureBed@master/uPic/2021/06/p6jHN7.jpg");
        imageMiku.add(miku);
        add(imageMiku);
        add(new Text("by mikusugar"));
    }

    private HorizontalLayout createHorizontalLayout() {
        HorizontalLayout res = new HorizontalLayout();
        res.setWidthFull();
        return res;
    }

}
