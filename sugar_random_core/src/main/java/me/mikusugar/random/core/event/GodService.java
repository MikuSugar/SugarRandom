package me.mikusugar.random.core.event;

import me.mikusugar.random.core.bean.SugarJsonNode;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 事件逻辑代理类
 *
 * @author mikusugar
 */
@Service
public class GodService {


    ///////////////////////////////////////////////////////////////////////////
    // init
    ///////////////////////////////////////////////////////////////////////////
    private final PreEventService preEventService;
    private final GetHelpEventService getHelpEventService;
    private final DelEventService delEventService;
    private final ConfigEventService configEventService;
    private final NextEventService nextEventService;
    private final ShowEventService showEventService;
    private final AliasEventService aliasEventService;
    private final FileEventService fileEventService;

    public GodService(
            PreEventService preEventService,
            GetHelpEventService getHelpEventService,
            DelEventService delEventService,
            ConfigEventService configEventService,
            NextEventService nextEventService,
            ShowEventService showEventService,
            AliasEventService aliasEventService,
            FileEventService fileEventService) {
        this.preEventService = preEventService;
        this.getHelpEventService = getHelpEventService;
        this.delEventService = delEventService;
        this.configEventService = configEventService;
        this.nextEventService = nextEventService;
        this.showEventService = showEventService;
        this.aliasEventService = aliasEventService;
        this.fileEventService = fileEventService;
    }

    ///////////////////////////////////////////////////////////////////////////
    // PreEventService
    ///////////////////////////////////////////////////////////////////////////

    public String getPrettyJson(SugarJsonNode rootNode) {
        return preEventService.getPrettyJson(rootNode);
    }

    ///////////////////////////////////////////////////////////////////////////
    // GetHelpEventService
    ///////////////////////////////////////////////////////////////////////////

    public String getHelpStr(String randomType) {
        return getHelpEventService.getHelpStr(randomType);
    }

    ///////////////////////////////////////////////////////////////////////////
    // DelEventService
    ///////////////////////////////////////////////////////////////////////////

    public void del(SugarJsonNode delNode, SugarJsonNode rootNode) {
        delEventService.del(delNode, rootNode);
    }

    ///////////////////////////////////////////////////////////////////////////
    // ConfigEventService
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 通过配置得到根节点
     *
     * @param configName 配置名
     */
    public SugarJsonNode getSugarJsonNode(String configName) throws Exception {
        return configEventService.getSugarJsonNode(configName);
    }

    /**
     * 存储配置
     *
     * @param configName 配置名
     * @param rootNode   待存储的根节点
     */
    public void saveConfig(String configName, SugarJsonNode rootNode) throws Exception {
        configEventService.saveConfig(configName, rootNode);
    }

    /**
     * 通过配置名删除配置
     *
     * @param configName 配置名
     */
    public void delConfig(String configName) throws Exception {
        configEventService.delConfig(configName);
    }

    /**
     * 读取alias配置
     */
    public void readAlias(String name, Map<String, String> aliasMap) throws Exception {
        configEventService.readAlias(name, aliasMap);
    }

    /**
     * 存储alias配置
     */
    public void saveAlias(String name, Map<String, String> aliasMap) throws Exception {
        configEventService.saveAlias(name, aliasMap);
    }

    /**
     * 删除alias配置
     */
    public void delAlias(String name) throws Exception {
        configEventService.delAlias(name);
    }

    ///////////////////////////////////////////////////////////////////////////
    // NextEventService
    ///////////////////////////////////////////////////////////////////////////

    /***
     * 检查参数是否正确
     */
    public void check(String randomType, SugarJsonNode curNode, String randomInfo, String fieldName)
            throws Exception {
        nextEventService.check(randomType, curNode, randomInfo, fieldName);
    }

    /**
     * 添加节点配置
     */
    public void add(String name, String randomType, String randomInfo, SugarJsonNode curNode)
            throws Exception {
        nextEventService.add(name, randomType, randomInfo, curNode);
    }

    ///////////////////////////////////////////////////////////////////////////
    // ShowEventService
    ///////////////////////////////////////////////////////////////////////////

    public String getAllTypeInfo() {
        return showEventService.getAllTypeInfo();
    }

    public String echo(String name, Map<String, String> aliasMap) throws Exception {
        return showEventService.echo(name, aliasMap);
    }

    ///////////////////////////////////////////////////////////////////////////
    // AliasEventService
    ///////////////////////////////////////////////////////////////////////////

    public boolean checkAliasEventService(String name) {
        return aliasEventService.checkAliasEventService(name);
    }

    ///////////////////////////////////////////////////////////////////////////
    // FileEventService
    ///////////////////////////////////////////////////////////////////////////

    public void out(SugarJsonNode sugarJsonNode, int num, String path) throws Exception {
        fileEventService.out(sugarJsonNode, num, path);
    }


}
