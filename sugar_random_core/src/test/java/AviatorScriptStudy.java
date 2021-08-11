import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import lombok.SneakyThrows;

/**
 * @author mikusugar
 */
public class AviatorScriptStudy {

    @SneakyThrows
    public static void main(String[] args) {
        Expression exp = AviatorEvaluator.getInstance().compile("{1}", true);
        final Object execute = exp.execute();
        System.out.println();
    }

}
