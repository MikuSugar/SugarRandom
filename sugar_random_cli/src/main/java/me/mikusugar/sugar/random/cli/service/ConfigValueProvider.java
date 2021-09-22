package me.mikusugar.sugar.random.cli.service;

import me.mikusugar.random.core.bean.ConfigSave;
import me.mikusugar.random.core.service.ConfigSavaRepository;
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
public class ConfigValueProvider extends ValueProviderSupport {

    private final ConfigSavaRepository configSavaRepository;

    public ConfigValueProvider(ConfigSavaRepository configSavaRepository) {
        this.configSavaRepository = configSavaRepository;
    }

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter,
                                             CompletionContext completionContext,
                                             String[] hints) {
        return configSavaRepository.findAll()
                .stream()
                .map(ConfigSave::getId)
                .map(CompletionProposal::new)
                .collect(Collectors.toList());
    }
}
