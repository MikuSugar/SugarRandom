package me.mikusugar.sugar.random.cli.service;

import me.mikusugar.random.core.service.AbstractRandomService;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 显示所有随机类型补全
 *
 * @author mikusugar
 */
@SuppressWarnings("ALL")
@Component
public class RTypeValueProvider extends ValueProviderSupport {

    private final Map<String, AbstractRandomService> randomServiceMap;

    public RTypeValueProvider(Map<String, AbstractRandomService> randomServiceMap) {
        this.randomServiceMap = randomServiceMap;
    }

    @Override
    public List<CompletionProposal> complete(MethodParameter methodParameter, CompletionContext completionContext, String[] strings) {
        return randomServiceMap.keySet().stream().map(CompletionProposal::new).collect(Collectors.toList());
    }
}
