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
                "\n" +
                "/**\n" +
                " * 请添加以下依赖\n" +
                " * <dependency>\n" +
                " *    <groupId>me.mikusugar.random</groupId>\n" +
                " *    <artifactId>sugar_random_core</artifactId>\n" +
                " *    <version>xxx</version>\n" +
                " * </dependency>\n" +
                " *\n" +
                " * @author mikusugar\n" +
                " */\n" +
                "public class SugarRandom {\n" +
                "\n" +
                "    public static void main(String[] args) throws JsonProcessingException, InstantiationException, IllegalAccessException {\n" +
                "        System.out.println(new SugarRandom().next());\n" +
                "    }\n" +
                "\n" +
                "    private final GenerateCodeService generateCodeService;\n" +
                "\n" +
                "    public SugarRandom() throws JsonProcessingException, InstantiationException, IllegalAccessException {\n" +
                "    String json=\""+helpJson(json)+"\";\n" +
                "        this.generateCodeService = new GenerateCodeService(json);\n" +
                "    }\n" +
                "\n" +
                "    public String next() {\n" +
                "        return this.generateCodeService.getJson();\n" +
                "    }\n" +
                "\n" +
                "}\n";
    }

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
