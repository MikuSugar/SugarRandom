package me.mikusugar.random.core.event.impl;

import lombok.extern.slf4j.Slf4j;
import me.mikusugar.random.core.bean.AliasConfig;
import me.mikusugar.random.core.bean.ConfigSave;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.service.AbstractRandomService;
import me.mikusugar.random.core.service.AliasConfigRepository;
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
    private final AliasConfigRepository aliasConfigRepository;
    private final Map<String, AbstractRandomService> randomServiceMap;

    public ConfigEventServiceImpl(ConfigSavaRepository configSavaRepository,
                                  AliasConfigRepository aliasConfigRepository,
                                  Map<String, AbstractRandomService> randomServiceMap) {
        this.configSavaRepository = configSavaRepository;
        this.aliasConfigRepository = aliasConfigRepository;
        this.randomServiceMap = randomServiceMap;
    }

    @Override
    public SugarJsonNode getSugarJsonNode(String configName) throws Exception {
        final Optional<ConfigSave> savaRepositoryById =
                configSavaRepository.findById(configName);
        if (savaRepositoryById.isPresent()) return SugarJsonNodeSerialization.read(
                savaRepositoryById.get().getJson(), randomServiceMap);
        else throw new Exception("not found config: " + configName);
    }

    @Override
    public void saveConfig(String configName, SugarJsonNode rootNode) throws Exception {
        final String json = SugarJsonNodeSerialization.write(rootNode);
        ConfigSave configSave = new ConfigSave();
        configSave.setId(configName);
        configSave.setJson(json);
        configSavaRepository.save(configSave);
    }

    @Override
    public void delConfig(String configName) throws Exception {
        configSavaRepository.deleteById(configName);
    }

    @Override
    public void readAlias(String name, Map<String, String> aliasMap) throws Exception {
        final Optional<AliasConfig> byId = aliasConfigRepository.findById(name);
        if (byId.isPresent()) {
            final Map<String, String> map = byId.get().getAliasMap();
            map.forEach((k, v) -> {
                if (!randomServiceMap.containsKey(k)) {
                    aliasMap.put(k, v);
                }
            });
        } else throw new Exception("找不到 alias config " + name);
    }

    @Override
    public void saveAlias(String name, Map<String, String> aliasMap) throws Exception {
        final AliasConfig aliasConfig = new AliasConfig();
        aliasConfig.setId(name);
        aliasConfig.setAliasMap(aliasMap);
        aliasConfigRepository.save(aliasConfig);
    }

    @Override
    public void delAlias(String name) throws Exception {
        aliasConfigRepository.deleteById(name);
    }


}
