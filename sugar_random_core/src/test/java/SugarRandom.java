import com.fasterxml.jackson.core.JsonProcessingException;
import me.mikusugar.random.core.service.GenerateCodeService;
//生成代码的示例
/**
 * 请添加以下依赖
 * <dependency>
 *    <groupId>me.mikusugar.random</groupId>
 *    <artifactId>sugar_random_core</artifactId>
 *    <version>xxx</version>
 * </dependency>
 *
 * @author mikusugar
 */
public class SugarRandom {

    public static void main(String[] args) throws JsonProcessingException, InstantiationException, IllegalAccessException {
        System.out.println(new SugarRandom().next());
    }

    private final GenerateCodeService generateCodeService;

    public SugarRandom() throws JsonProcessingException, InstantiationException, IllegalAccessException {
        String json = "{\"name\":\"root\",\"type\":\"OBJECT\",\"desc\":\"默认根节点\",\"randomServiceName\":\"随机对象\",\"input\":\"\",\"nexts\":[{\"name\":\"name\",\"type\":\"STRING\",\"desc\":\"输入0随机生成女性名字，输入1随机生成男性名字，不输入都生成\",\"randomServiceName\":\"随机中文姓名\",\"input\":\"\",\"nexts\":[]},{\"name\":\"uuid\",\"type\":\"STRING\",\"desc\":\"java 原生的UUID，无需输入\",\"randomServiceName\":\"UUID\",\"input\":\"\",\"nexts\":[]},{\"name\":\"citys\",\"type\":\"ARRAY\",\"desc\":\"设置数组长度,输入 2 代表数组长度为2 ,输入 2,3 代表数组长度为 2到3随机\",\"randomServiceName\":\"随机数组长度\",\"input\":\"1,5\",\"nexts\":[{\"name\":\"city\",\"type\":\"STRING\",\"desc\":\"随机生成国内城市，无需输入～ (北上广等概率大)\",\"randomServiceName\":\"默认国内城市\",\"input\":\"\",\"nexts\":[]}]},{\"name\":\"other\",\"type\":\"OBJECT\",\"desc\":\"什么都不需要输入\",\"randomServiceName\":\"随机对象\",\"input\":\"\",\"nexts\":[{\"name\":\"phone\",\"type\":\"STRING\",\"desc\":\"随机生成国内手机号，无需输入\",\"randomServiceName\":\"随机中国大陆手机号\",\"input\":\"\",\"nexts\":[]},{\"name\":\"mac\",\"type\":\"STRING\",\"desc\":\"随机生成MAC地址,无需输入\",\"randomServiceName\":\"MAC\",\"input\":\"\",\"nexts\":[]}]}]}";
        this.generateCodeService = new GenerateCodeService(json);
    }

    public String next() {
        return this.generateCodeService.getJson();
    }

}
