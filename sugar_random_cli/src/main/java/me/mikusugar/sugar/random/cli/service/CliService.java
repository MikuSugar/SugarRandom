package me.mikusugar.sugar.random.cli.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.event.*;
import me.mikusugar.random.core.utils.GenerateCodeUtil;
import me.mikusugar.sugar.random.cli.utils.CliUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.*;

/**
 * @author mikusugar
 */
@ShellComponent
public class CliService {

    ///////////////////////////////////////////////////////////////////////////
    // init
    ///////////////////////////////////////////////////////////////////////////
    private final ApplicationContext applicationContext;
    private final PreEventService preEventService;
    private final GetHelpEventService getHelpEventService;
    private final DelEventService delEventService;
    private final ConfigEventService configEventService;
    private final NextEventService nextEventService;
    private final ShowAllTypeEventService showAllTypeEventService;
    private final AliasEventService aliasEventService;
    private final FileEventService fileEventService;

    private SugarJsonNode rootNode;


    private SugarJsonNode curNode;

    public CliService(ApplicationContext applicationContext,
                      PreEventService preEventService,
                      GetHelpEventService getHelpEventService,
                      DelEventService delEventService,
                      ConfigEventService configEventService,
                      NextEventService nextEventService,
                      ShowAllTypeEventService showAllTypeEventService,
                      AliasEventService aliasEventService,
                      FileEventService fileEventService) {

        this.rootNode =
                SugarJsonNode.builder()
                        .name("root")
                        .type(SugarJsonNode.TYPE.OBJECT)
                        .desc("默认根节点")
                        .randomServiceName(ServiceName.RANDOM_OBJ)
                        .build();
        this.curNode = rootNode;
        this.aliasMap = new HashMap<>();
        this.applicationContext = applicationContext;
        this.preEventService = preEventService;
        this.getHelpEventService = getHelpEventService;
        this.delEventService = delEventService;
        this.configEventService = configEventService;
        this.nextEventService = nextEventService;
        this.showAllTypeEventService = showAllTypeEventService;
        this.aliasEventService = aliasEventService;
        this.fileEventService = fileEventService;
    }


    ///////////////////////////////////////////////////////////////////////////
    // group show
    ///////////////////////////////////////////////////////////////////////////
    @ShellMethod(value = "展示所有随机类型", group = "show")
    public String showAllR() {
        return showAllTypeEventService.getAllTypeInfo();
    }

    @ShellMethod(value = "预览随机结果", group = "show")
    public String showJson(@ShellOption(defaultValue = "1") int num) {
        StringBuilder ans = new StringBuilder();
        while (num-- > 0) {
            ans.append(preEventService.getPrettyJson(rootNode))
                    .append(System.lineSeparator());
        }
        return ans.toString();
    }

    @ShellMethod(value = "预览随机结构", group = "show")
    public String showTree() {
        return CliUtils.JsonNodeToTreeStr(this.rootNode);
    }

    @ShellMethod(value = "展示某个随机结构的提示", group = "show")
    public String showRType(String randomType) {
        final String helpStr = getHelpEventService.getHelpStr(randomType.trim());
        if (helpStr == null) {
            return "找不到类型【" + randomType + "】!" + " 所有类型:" + System.lineSeparator() + showAllR();
        } else {
            return helpStr;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // group random
    ///////////////////////////////////////////////////////////////////////////


    @ShellMethod(value = "移除所有配置", group = "random")
    public void removeAll() {
        delEventService.del(rootNode, rootNode);
    }

    @ShellMethod(value = "生成文件到本地", group = "random")
    public void outFile(int num, String path) throws Exception {
        fileEventService.out(rootNode, num, path);
    }

    @ShellMethod(value = "生成Java代码", group = "random")
    public String code() throws JsonProcessingException {
        return GenerateCodeUtil.getCode(rootNode);
    }

    ///////////////////////////////////////////////////////////////////////////
    // group config
    ///////////////////////////////////////////////////////////////////////////

    @ShellMethod(value = "配置读取", group = "config")
    public void read(String name) throws Exception {
        val node = configEventService.getSugarJsonNode(name);
        assert node != null;
        this.rootNode = node;
        this.curNode = this.rootNode;
    }


    @ShellMethod(value = "配置存储", group = "config")
    public void save(String name) throws Exception {
        configEventService.saveConfig(name, rootNode);
    }

    @ShellMethod(value = "配置删除", group = "config")
    public void delConfig(String name) throws Exception {
        configEventService.delConfig(name);
    }

    ///////////////////////////////////////////////////////////////////////////
    // unix 文件系统风格命令
    ///////////////////////////////////////////////////////////////////////////

    private final Map<String, String> aliasMap;

    @ShellMethod(value = "当前节点路径", group = "unix-style")
    public String pwd() {
        return CliUtils.gePath(curNode);
    }

    @ShellMethod(value = "进入到某个节点", group = "unix-stayle")
    public void cd(String path) throws Exception {
        val res = CliUtils.getPathNode(curNode, path, rootNode);
        assert res != null;
        if (SugarJsonNode.TYPE.ARRAY.equals(res.getType())
                || SugarJsonNode.TYPE.OBJECT.equals(res.getType())) {
            this.curNode = res;
        } else throw new Exception("cd 只能进入到Array 和 Object");
        applicationContext.publishEvent(curNode);
    }

    @ShellMethod(value = "展示结构", group = "unix-style")
    public String ll(@ShellOption(defaultValue = "") String path) throws Exception {
        val node = CliUtils.getPathNode(curNode, path, rootNode);
        assert node != null;
        return CliUtils.JsonNodeToTreeStr(node);
    }

    @ShellMethod(value = "别名，仅针对随机类型名，会覆盖，优先级大于随机类型名", group = "unix-style")
    public void alias(String rType, String aliasName) throws Exception {
        if (!aliasEventService.checkAliasEventService(aliasName)) throw new Exception("别名不能与随机类型名相同");
        aliasMap.put(aliasName, rType);
    }

    @ShellMethod(
            value = "添加一个数组，input 参考 [show-rtype " + ServiceName.RANDOM_ARRAY_LEN + "]",
            group = "unix-style")
    public void mkarr(String name, String input) throws Exception {
        nextEventService.check(ServiceName.RANDOM_ARRAY_LEN, curNode, input, name);
        nextEventService.add(name, ServiceName.RANDOM_ARRAY_LEN, input, curNode);
    }


    @ShellMethod(
            value = "添加一个object",
            group = "unix-style"
    )
    public void mkobj(String name) throws Exception {
        nextEventService.check(ServiceName.RANDOM_OBJ, curNode, "", name);
        nextEventService.add(name, ServiceName.RANDOM_OBJ, "", curNode);
    }

    @ShellMethod(value = "添加字段", group = "unix-style")
    public void touch(String field, String rtype,
                      @ShellOption(defaultValue = "") String input) throws Exception {

        if (aliasMap.containsKey(rtype)) rtype = aliasMap.get(rtype);

        nextEventService.check(rtype, curNode, input, field);
        nextEventService.add(field, rtype, input, curNode);
    }

    @ShellMethod(value = "删除", group = "unix-style")
    public void rm(String path) throws Exception {
        val delNode = CliUtils.getPathNode(curNode, path, rootNode);
        delEventService.del(delNode, rootNode);
    }

}
