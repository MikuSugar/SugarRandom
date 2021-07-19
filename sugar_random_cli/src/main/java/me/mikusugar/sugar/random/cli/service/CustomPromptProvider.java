package me.mikusugar.sugar.random.cli.service;

import me.mikusugar.sugar.random.cli.utils.CliUtils;
import org.jline.utils.AttributedString;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

/**
 * @author mikusugar
 */
@Component
public class CustomPromptProvider implements PromptProvider {

    private String path;

    @Override
    public AttributedString getPrompt() {
        if(path!=null){
            return new AttributedString("🍭 "+ CliUtils.getLastPath(path) +" :>");
        }
        return new AttributedString("🍭 root :>");
    }

    @EventListener
    public void handle(PayloadApplicationEvent<String> event) {
        this.path = event.getPayload();
    }

}
