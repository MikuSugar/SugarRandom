package me.mikusugar.sugar.random.cli.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.val;
import me.mikusugar.random.core.bean.ConfigSave;
import me.mikusugar.random.core.bean.SugarJsonNode;
import me.mikusugar.random.core.constant.ServiceName;
import me.mikusugar.random.core.service.AbstractRandomService;
import me.mikusugar.random.core.service.ConfigSavaRepository;
import me.mikusugar.random.core.service.RandomCoreService;
import me.mikusugar.random.core.utils.GenerateCodeUtil;
import me.mikusugar.random.core.utils.SugarJsonNodeSerialization;
import me.mikusugar.random.core.utils.SugarJsonUtils;
import me.mikusugar.sugar.random.cli.utils.CliUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
@SuppressWarnings("ALL")
@ShellComponent
public class CliService {

    ///////////////////////////////////////////////////////////////////////////
    // init
    ///////////////////////////////////////////////////////////////////////////
    @Autowired
    private Map<String, AbstractRandomService> randomServiceMap;

    @Autowired
    private ConfigSavaRepository configSavaRepository;

    @Autowired
    ApplicationContext applicationContext;

    private SugarJsonNode rootNode;

    private Map<String, SugarJsonNode> map;

    private Set<String> dtype;

    private SugarJsonNode curNode;

    public CliService() {
        this.rootNode =
                SugarJsonNode.builder()
                        .name("root")
                        .type(SugarJsonNode.TYPE.OBJECT)
                        .desc("默认根节点")
                        .randomServiceName(ServiceName.RANDOM_OBJ)
                        .build();
        this.map = new HashMap<>();
        this.map.put("root", rootNode);
        dtype = new HashSet<>();
        for (SugarJsonNode.TYPE type : SugarJsonNode.TYPE.values()) {
            dtype.add(type.toString());
        }
        this.curNode = rootNode;
        this.aliasMap = new HashMap<>();
    }


    ///////////////////////////////////////////////////////////////////////////
    // group show
    ///////////////////////////////////////////////////////////////////////////
    @ShellMethod(value = "展示所有随机类型", group = "show")
    public String showAllR() {
        StringBuilder res = new StringBuilder();
        randomServiceMap.forEach(
                (k, v) ->
                        res.append(k)
                                .append("\t 描述:")
                                .append(v.helpText().replace(System.lineSeparator(), " "))
                                .append(System.lineSeparator())
        );
        return res.toString();
    }

    @ShellMethod(value = "展示所有数据类型", group = "show")
    public String showDType() {
        StringBuilder res = new StringBuilder();
        final SugarJsonNode.TYPE[] values = SugarJsonNode.TYPE.values();
        for (SugarJsonNode.TYPE type : values) {
            res.append(type.toString()).append(System.lineSeparator());
        }
        return res.toString();
    }

    @ShellMethod(value = "预览随机结果", group = "show")
    public String showJson(@ShellOption(defaultValue = "1") int num) {
        StringBuilder ans = new StringBuilder();
        while (num-- > 0) {
            StringBuilder res = new StringBuilder();
            SugarJsonUtils.toJsonStr(null, rootNode, res);
            ans.append(SugarJsonUtils.json2PrettyFormat(res.toString()))
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
        final AbstractRandomService randomService = randomServiceMap.get(randomType);
        if (randomService == null) {
            return "找不到类型【" + randomType + "】!" + " 所有类型:" + System.lineSeparator() + showAllR();
        } else {
            return randomService.helpText();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // group random
    ///////////////////////////////////////////////////////////////////////////

    @ShellMethod(value = "添加字段", group = "random")
    public String add(String field, String father, String dtype, String rtype,
                      @ShellOption(defaultValue = "") String input) {
        //check
        if (map.containsKey(field)) {
            return "[" + field + "]该字段已存在，不能重复添加！";
        }
        if (!randomServiceMap.containsKey(rtype)) {
            return "[" + rtype + "]该随机类型不存在,请重新输入！";
        }
        if (!dtype.contains(dtype)) {
            return "[" + dtype + "]该数据类型不存在,请重新输入！";
        }
        if (!map.containsKey(father)) {
            return "[" + father + "]该节点不存在,请重新输入！";
        }

        final AbstractRandomService randomService = randomServiceMap.get(rtype);
        if (!randomService.check(dtype, input)) {
            return dtype + " " + input + " 数据检查未通过，请重新输入！";
        }

        SugarJsonNode node =
                SugarJsonNode.builder()
                        .name(field)
                        .type(SugarJsonNode.TYPE.valueOf(dtype))
                        .randomServiceName(rtype)
                        .randomService(randomService.createRandomCoreService(input))
                        .desc(randomService.helpText())
                        .father(map.get(father))
                        .build();
        map.get(father).getNexts().add(node);
        map.put(node.getName(), node);
        return "字段配置添加成功～";
    }

    @ShellMethod(value = "删除字段，会同时删除该字段的所有子字段,无法删除root字段", group = "random")
    public String delNode(String field) {
        if (!map.containsKey(field)) return CliUtils.showField(field) + "不存在";
        final SugarJsonNode node = map.get(field);
        final SugarJsonNode father = node.getFather();
        dfsDelNode(node);
        father.getNexts().remove(node);
        return CliUtils.showField(field) + "已删除";
    }

    private void dfsDelNode(SugarJsonNode node) {
        if (node.getNexts() != null) {
            node.getNexts().forEach(this::dfsDelNode);
        }
        map.remove(node.getName());
    }


    @ShellMethod(value = "移除所有配置", group = "random")
    public String removeAll() {
        this.map.clear();
        this.map.put("root", rootNode);
        return "配置清楚成功。";
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

    @ShellMethod(value = "生成Java代码",group = "random")
    public String code() throws JsonProcessingException {
        return GenerateCodeUtil.getCode(rootNode);
    }

    ///////////////////////////////////////////////////////////////////////////
    // group config
    ///////////////////////////////////////////////////////////////////////////

    @ShellMethod(value = "配置读取", group = "config")
    public String read(String name) throws JsonProcessingException {
        final Optional<ConfigSave> savaRepositoryById =
                configSavaRepository.findById(name);
        if (savaRepositoryById.isPresent()) {
            this.rootNode = SugarJsonNodeSerialization.read(
                    savaRepositoryById.get().getJson(), randomServiceMap);
            map.clear();
            dfsAddMap(this.rootNode);
            this.curNode = this.rootNode;
            return "配置读取成功";
        } else return "配置名不存在！";
    }

    private void dfsAddMap(SugarJsonNode node) {
        if (node == null) return;
        map.put(node.getName(), node);
        node.getNexts().forEach(this::dfsAddMap);
    }

    @ShellMethod(value = "配置存储", group = "config")
    public String save(String name) throws JsonProcessingException {
        if (name == null || name.trim().isEmpty()) {
            return "配置名为空，无法存储！";
        }
        final String json = SugarJsonNodeSerialization.write(rootNode);
        ConfigSave configSave = new ConfigSave();
        configSave.setId(name);
        configSave.setJson(json);
        configSavaRepository.save(configSave);
        return "配置存储成功";
    }

    @ShellMethod(value = "配置删除", group = "config")
    public String delConfig(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "配置名为空，无法删除！";
        }
        configSavaRepository.deleteById(name);
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
        if (map.containsKey(aliasName)) throw new Exception("别名不能与随机类型名相同");
        aliasMap.put(aliasName, rType);
    }

    @ShellMethod(
            value = "添加一个数组，input 参考 [show-rtype " + ServiceName.RANDOM_ARRAY_LEN + "]",
            group = "unix-stayle")
    public void mkarr(String name, String input) throws Exception {

        if (SugarJsonNode.TYPE.ARRAY.equals(curNode.getType())) {
            if (curNode.getNexts().size() > 0) throw new Exception("Array 只能有一个节点");
        }

        if (map.containsKey(name)) throw new Exception("名称已经存在，请更换");

        final AbstractRandomService arrayService = randomServiceMap.get(ServiceName.RANDOM_ARRAY_LEN);
        if (!arrayService.check(SugarJsonNode.TYPE.ARRAY.toString(), input))
            throw new Exception("参数检查不通过");
        final RandomCoreService randomCoreService = arrayService.createRandomCoreService(input);

        final SugarJsonNode res = SugarJsonNode.builder()
                .father(curNode)
                .name(name)
                .nexts(new ArrayList<>())
                .randomService(randomCoreService)
                .randomServiceName(ServiceName.RANDOM_ARRAY_LEN)
                .desc(arrayService.helpText())
                .type(SugarJsonNode.TYPE.ARRAY)
                .build();
        curNode.getNexts().add(res);
        map.put(name, res);
    }

    @ShellMethod(
            value = "添加一个object",
            group = "unix-stayle"
    )
    public void mkobj(String name) throws Exception {
        if (map.containsKey(name)) throw new Exception("名称已经存在，请更换");

        if (SugarJsonNode.TYPE.ARRAY.equals(curNode.getType())) {
            if (curNode.getNexts().size() > 0) throw new Exception("Array 只能有一个节点");
        }

        val objRandService = randomServiceMap.get(ServiceName.RANDOM_OBJ);
        val coreService = objRandService.createRandomCoreService("");

        SugarJsonNode res = SugarJsonNode.builder()
                .father(curNode)
                .name(name)
                .nexts(new ArrayList<>())
                .randomServiceName(ServiceName.RANDOM_OBJ)
                .desc(objRandService.helpText())
                .type(SugarJsonNode.TYPE.OBJECT)
                .build();

        curNode.getNexts().add(res);
        map.put(name, res);

    }

    @ShellMethod(value = "添加字段", group = "unix-stayle")
    public void touch(String field, String dtype, String rtype,
                      @ShellOption(defaultValue = "") String input) throws Exception {
        if (map.containsKey(field)) {
            throw new Exception("[" + field + "]该字段已存在，不能重复添加！");
        }
        if (!dtype.contains(dtype)) {
            throw new Exception("[" + dtype + "]该数据类型不存在,请重新输入！");
        }

        AbstractRandomService randomService = null;
        if (aliasMap.containsKey(rtype)) randomService = randomServiceMap.get(aliasMap.get(rtype));
        if (randomService == null) randomService = randomServiceMap.get(rtype);
        if (randomService == null) throw new Exception("[" + rtype + "]该随机类型不存在,请重新输入！");

        if (!randomService.check(dtype, input)) {
            throw new Exception(dtype + " " + input + " 数据检查未通过，请重新输入！");
        }
        if (SugarJsonNode.TYPE.ARRAY.equals(curNode.getType())) {
            if (curNode.getNexts().size() > 0) throw new Exception("Array 只能有一个节点");
        }
        SugarJsonNode node =
                SugarJsonNode.builder()
                        .name(field)
                        .type(SugarJsonNode.TYPE.valueOf(dtype))
                        .randomServiceName(rtype)
                        .randomService(randomService.createRandomCoreService(input))
                        .desc(randomService.helpText())
                        .father(curNode)
                        .build();
        curNode.getNexts().add(node);
        map.put(node.getName(), node);
    }

    @ShellMethod(value = "删除", group = "unix-stayle")
    public void rm(String path) throws Exception {
        final SugarJsonNode delNode = CliUtils.getPathNode(curNode, path, rootNode);
        delNode(delNode.getName());
    }

}
