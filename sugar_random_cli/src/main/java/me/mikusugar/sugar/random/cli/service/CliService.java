package me.mikusugar.sugar.random.cli.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.event.*;
import me.mikusugar.random.core.utils.GenerateCodeUtil;
import me.mikusugar.random.core.utils.SugarJsonUtils;
import me.mikusugar.sugar.random.cli.utils.CliUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    private SugarJsonNode rootNode;


    private SugarJsonNode curNode;

    public CliService(ApplicationContext applicationContext,
                      PreEventService preEventService,
                      GetHelpEventService getHelpEventService,
                      DelEventService delEventService,
                      ConfigEventService configEventService,
                      NextEventService nextEventService,
                      ShowAllTypeEventService showAllTypeEventService,
                      AliasEventService aliasEventService) {

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
    public String outFile(int num, String path) throws IOException {
        final FileWriter out = new FileWriter(new File(path + ".json"));
        int size = num;
        while (size-- > 0) {
            val res = new StringBuilder();
            SugarJsonUtils.toJsonStr(null, rootNode, res);
            out.write(res.toString());
            if (size > 0) {
                out.write("," + System.lineSeparator());
            }
        }
        out.flush();
        out.close();
        return "文件已经生成";
    }

    @ShellMethod(value = "生成Java代码", group = "random")
    public String code() throws JsonProcessingException {
        return GenerateCodeUtil.getCode(rootNode);
    }

    ///////////////////////////////////////////////////////////////////////////
    // group config
    ///////////////////////////////////////////////////////////////////////////

    @ShellMethod(value = "配置读取", group = "config")
    public String read(String name) throws JsonProcessingException {
        final SugarJsonNode node = configEventService.getSugarJsonNode(name);
        if (node != null) {
            this.rootNode = node;
            this.curNode = this.rootNode;
            return "配置读取成功";
        } else return "配置名不存在！";
    }


    @ShellMethod(value = "配置存储", group = "config")
    public String save(String name) throws JsonProcessingException {
        if (name == null || name.trim().isEmpty()) {
            return "配置名为空，无法存储！";
        }
        configEventService.saveConfig(name, rootNode);
        return "配置存储成功";
    }

    @ShellMethod(value = "配置删除", group = "config")
    public String delConfig(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "配置名为空，无法删除！";
        }
        configEventService.delConfig(name);
        return "删除成功";
    }

    ///////////////////////////////////////////////////////////////////////////
    // unix 文件系统风格命令
    ///////////////////////////////////////////////////////////////////////////

    private Map<String, String> aliasMap;

    @ShellMethod(value = "当前节点路径", group = "unix-stayle")
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

    @ShellMethod(value = "展示结构", group = "unix-stayle")
    public String ll(@ShellOption(defaultValue = "") String path) throws Exception {
        final SugarJsonNode node = CliUtils.getPathNode(curNode, path, rootNode);
        assert node != null;
        return CliUtils.JsonNodeToTreeStr(node);
    }

    @ShellMethod(value = "别名，仅针对随机类型名，会覆盖，优先级大于随机类型名", group = "unix-stayle")
    public void alias(String rType, String aliasName) throws Exception {
        if (!aliasEventService.checkAliasEventService(aliasName)) throw new Exception("别名不能与随机类型名相同");
        aliasMap.put(aliasName, rType);
    }

    @ShellMethod(
            value = "添加一个数组，input 参考 [show-rtype " + ServiceName.RANDOM_ARRAY_LEN + "]",
            group = "unix-stayle")
    public void mkarr(String name, String input) throws Exception {
        if (nextEventService.check(ServiceName.RANDOM_ARRAY_LEN, curNode, input, name)) {
            nextEventService.add(name, ServiceName.RANDOM_ARRAY_LEN, input, curNode);
        } else throw new Exception("配置不合法");
    }


    @ShellMethod(
            value = "添加一个object",
            group = "unix-stayle"
    )
    public void mkobj(String name) throws Exception {
        if (nextEventService.check(ServiceName.RANDOM_OBJ, curNode, "", name)) {
            nextEventService.add(name, ServiceName.RANDOM_OBJ, "", curNode);
        } else throw new Exception("配置不合法");

    }

    @ShellMethod(value = "添加字段", group = "unix-stayle")
    public void touch(String field, String rtype,
                      @ShellOption(defaultValue = "") String input) throws Exception {

        if (aliasMap.containsKey(rtype)) rtype = aliasMap.get(rtype);

        if (nextEventService.check(rtype, curNode, input, field)) {
            nextEventService.add(field, rtype, input, curNode);
        } else throw new Exception("配置不合法");
    }

    @ShellMethod(value = "删除", group = "unix-stayle")
    public void rm(String path) throws Exception {
        final SugarJsonNode delNode = CliUtils.getPathNode(curNode, path, rootNode);
        delEventService.del(delNode, rootNode);
    }

}
