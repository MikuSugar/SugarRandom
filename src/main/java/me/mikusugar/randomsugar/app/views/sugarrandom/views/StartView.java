package me.mikusugar.randomsugar.app.views.sugarrandom.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.event.GodService;
import me.mikusugar.random.core.utils.GenerateCodeUtil;
import me.mikusugar.random.core.utils.SugarJsonUtils;
import me.mikusugar.randomsugar.app.utils.ViewUtils;
import me.mikusugar.randomsugar.app.func.GetValueFunc;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

/**
 * @author mikusugar
 */
@Getter
@Slf4j
public class StartView {

    private final NumberField number;
    private final TextArea area;
    private final Dialog resPreDialog;
    private final TextArea resPreTextArea;
    private final Button resPreButton;
    private final Dialog generateCodeDialog;
    private final TextArea generateCodeArea;
    private final Button generateCodeButton;

    public StartView(
            VerticalLayout layout,
            GodService godService,
            GetValueFunc<SugarJsonNode> getRootNode
    ) {
        //view
        HorizontalLayout startLayout = ViewUtils.createHorizontalLayout();
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
        StreamResource href = new StreamResource("download.json", (InputStreamFactory) () -> {
            val res = new StringBuilder();
            int size = number.getValue().intValue();
            System.out.println(size);
            while (size-- > 0) {
                SugarJsonUtils.toJsonStr(null, getRootNode.getValue(), res);
                res.append(",").append("\n");
            }
            res.deleteCharAt(res.lastIndexOf(","));
            try {
                return IOUtils.toInputStream(res.toString(), "utf-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
        href.setCacheTime(0);
        Anchor download = new Anchor(href, "");
        download.getElement().setAttribute("开始", true);
        download.add(start);
        resPreDialog = new Dialog();
        resPreTextArea = new TextArea("结果：");
        ViewUtils.helpDialogAndTextArea(resPreTextArea, resPreDialog);
        resPreButton = new Button("预览", e -> resPreDialog.open());

        generateCodeDialog = new Dialog();
        generateCodeArea = new TextArea("Code");
        ViewUtils.helpDialogAndTextArea(generateCodeArea, generateCodeDialog);
        generateCodeButton = new Button("生成Java代码", e -> generateCodeDialog.open());

        startLayout.add(area, resPreButton, number, download, generateCodeButton);
        startLayout.setVerticalComponentAlignment(
                FlexComponent.Alignment.END,
                area,
                resPreButton,
                number,
                download,
                generateCodeButton
        );
        layout.add(startLayout);

        //event
        resPreButton.addClickListener(
                e -> resPreTextArea.setValue(godService.getPrettyJson(getRootNode.getValue()))
        );

        generateCodeButton.addClickListener(e -> {
            try {
                generateCodeArea.setValue(GenerateCodeUtil.getCode(getRootNode.getValue()));
            } catch (Exception exception) {
                exception.printStackTrace();
                log.error(exception.toString());
                ViewUtils.defaultNotion(exception.toString());
            }
        });
    }


}
