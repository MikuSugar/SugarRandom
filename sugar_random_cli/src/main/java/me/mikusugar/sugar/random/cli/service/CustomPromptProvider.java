package me.mikusugar.sugar.random.cli.service;

import me.mikusugar.random.core.bean.SugarJsonNode;
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

    private SugarJsonNode curNode;

    @Override
    public AttributedString getPrompt() {
        if (curNode != null) {
            return new AttributedString("🍭 " + curNode.getName() + " :>");
        }
        return new AttributedString("🍭 root :>");
    }

    @EventListener
    public void handle(PayloadApplicationEvent<SugarJsonNode> event) {
        this.curNode = event.getPayload();
    }

}
