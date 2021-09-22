package me.mikusugar.sugar.random.cli.service;

import me.mikusugar.random.core.bean.AliasConfig;
import me.mikusugar.random.core.service.AliasConfigRepository;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mikusugar
 */
@Component
public class AliasValueProvider extends ValueProviderSupport {

    private final AliasConfigRepository aliasConfigRepository;

    public AliasValueProvider(AliasConfigRepository aliasConfigRepository) {
        this.aliasConfigRepository = aliasConfigRepository;
    }

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter,
                                             CompletionContext completionContext,
                                             String[] hints) {
        return aliasConfigRepository.findAll()
                .stream()
                .map(AliasConfig::getId)
                .map(CompletionProposal::new)
                .collect(Collectors.toList());
    }
}
