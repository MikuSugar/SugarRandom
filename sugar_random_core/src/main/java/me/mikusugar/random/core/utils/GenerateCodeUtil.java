package me.mikusugar.random.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import me.mikusugar.random.core.bean.SugarJsonNode;

/**
 * @author mikusugar
 */
public class GenerateCodeUtil {

    public static String getCode(SugarJsonNode sugarJsonNode) throws JsonProcessingException {
        final String json = SugarJsonNodeSerialization.write(sugarJsonNode);

        return "import com.fasterxml.jackson.core.JsonProcessingException;\n" +
                "import me.mikusugar.random.core.service.GenerateCodeService;\n" +
                "import java.util.List;\n" +
                "import java.util.stream.Collectors;\n" +
                "import java.util.stream.Stream;\n" +
                "//生成代码的示例\n" +
                "\n" +
                "/**\n" +
                " * 请添加以下依赖\n" +
                " * <dependency>\n" +
                " * <groupId>me.mikusugar.random</groupId>\n" +
                " * <artifactId>sugar_random_core</artifactId>\n" +
                " * <version>xxx</version>\n" +
                " * </dependency>\n" +
                " *\n" +
                " * @author mikusugar\n" +
                " */\n" +
                "public class SugarRandom {\n" +
                "\n" +
                "    public static void main(String[] args) throws JsonProcessingException, InstantiationException, IllegalAccessException {\n" +
                "\n" +
                "        final SugarRandom sugarRandom = new SugarRandom();\n" +
                "        //生成单个\n" +
                "        System.out.println(sugarRandom.next());\n" +
                "\n" +
                "        int num = 1000000;\n" +
                "        //生成多个\n" +
                "        final long s = System.nanoTime();\n" +
                "        final List<String> res = sugarRandom.getParallelStream(num).collect(Collectors.toList());\n" +
                "    }\n" +
                "\n" +
                "    private final GenerateCodeService generateCodeService;\n" +
                "\n" +
                "    public SugarRandom() throws JsonProcessingException, InstantiationException, IllegalAccessException {\n" +
                "        String json = \""+helpJson(json)+"\";\n" +
                "        this.generateCodeService = new GenerateCodeService(json);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 生成一个随机json\n" +
                "     * @return json字符串\n" +
                "     */\n" +
                "    public String next() {\n" +
                "        return this.generateCodeService.getJson();\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     *  生成指定数量的json\n" +
                "     * @param num 数量\n" +
                "     * @return 返回的是并行流\n" +
                "     */\n" +
                "    public Stream<String> getParallelStream(int num) {\n" +
                "        return Stream.generate(this::next).parallel().limit(num);\n" +
                "    }\n" +
                "\n" +
                "}\n";
    }

    /**
     * 添加转义
     * @param json json
     * @return 转义后
     */
    private static String helpJson(String json) {
        final StringBuilder res = new StringBuilder();
        for (char c:json.toCharArray()){
            if(c=='"'){
                res.append("\\");
            }
            res.append(c);
        }
        return res.toString();
    }
}
