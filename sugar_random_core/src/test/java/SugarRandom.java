import com.fasterxml.jackson.core.JsonProcessingException;
import me.mikusugar.random.core.service.GenerateCodeService;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//生成代码的示例

/**
 * 请添加以下依赖
 * <dependency>
 * <groupId>me.mikusugar.random</groupId>
 * <artifactId>sugar_random_core</artifactId>
 * <version>xxx</version>
 * </dependency>
 *
 * @author mikusugar
 */
public class SugarRandom {

    public static void main(String[] args) throws JsonProcessingException, InstantiationException, IllegalAccessException {

        final SugarRandom sugarRandom = new SugarRandom();
        //生成单个
        System.out.println(sugarRandom.next());

        int num = 1000000;
        //生成多个
        final long s = System.nanoTime();
        final List<String> res = sugarRandom.getParallelStream(num).collect(Collectors.toList());
    }

    private final GenerateCodeService generateCodeService;

    public SugarRandom() throws JsonProcessingException, InstantiationException, IllegalAccessException {
        String json = "";
        this.generateCodeService = new GenerateCodeService(json);
    }

    /**
     * 生成一个随机json
     * @return json字符串
     */
    public String next() {
        return this.generateCodeService.getJson();
    }

    /**
     *  生成指定数量的json
     * @param num 数量
     * @return 返回的是并行流
     */
    public Stream<String> getParallelStream(int num) {
        return Stream.generate(this::next).parallel().limit(num);
    }

}
