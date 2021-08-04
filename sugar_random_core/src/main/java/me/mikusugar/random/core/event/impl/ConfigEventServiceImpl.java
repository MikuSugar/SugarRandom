package me.mikusugar.random.core.event.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import me.mikusugar.random.core.bean.ConfigSave;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.service.AbstractRandomService;
import me.mikusugar.random.core.service.ConfigSavaRepository;
import me.mikusugar.random.core.utils.SugarJsonNodeSerialization;
import me.mikusugar.random.core.event.ConfigEventService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * @author mikusugar
 */
@Service
@Slf4j
public class ConfigEventServiceImpl implements ConfigEventService {

    private final ConfigSavaRepository configSavaRepository;

    private final Map<String, AbstractRandomService> randomServiceMap;

    public ConfigEventServiceImpl(ConfigSavaRepository configSavaRepository,
                                  Map<String, AbstractRandomService> randomServiceMap) {
        this.configSavaRepository = configSavaRepository;
        this.randomServiceMap = randomServiceMap;
    }

    @Override
    public SugarJsonNode getSugarJsonNode(String configName) {
        final Optional<ConfigSave> savaRepositoryById =
                configSavaRepository.findById(configName);
        if (savaRepositoryById.isPresent()) {
            try {
                return SugarJsonNodeSerialization.read(
                        savaRepositoryById.get().getJson(), randomServiceMap);
            } catch (JsonProcessingException e) {
                log.error(savaRepositoryById.get().getJson() + e);
            }
        }
        return null;
    }

    @Override
    public void saveConfig(String configName, SugarJsonNode rootNode) throws JsonProcessingException {
        final String json = SugarJsonNodeSerialization.write(rootNode);
        ConfigSave configSave = new ConfigSave();
        configSave.setId(configName);
        configSave.setJson(json);
        configSavaRepository.save(configSave);
    }

    @Override
    public void delConfig(String configName) {
        configSavaRepository.deleteById(configName);
    }


}