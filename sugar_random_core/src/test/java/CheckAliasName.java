
import lombok.extern.slf4j.Slf4j;
import me.mikusugar.random.core.service.AbstractRandomService;
import me.mikusugar.random.core.utils.GetAllService;
import org.junit.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author mikusugar
 */
@Slf4j
public class CheckAliasName {

    /**
     * 检查默认别名是否相同
     */
    @Test
    public void checkAliasName() throws InstantiationException, IllegalAccessException {
        Set<String> set = new HashSet<>();
        final Map<String, AbstractRandomService> allService = GetAllService.getAllService();
        allService.values().forEach(s->{
            if(s.getAliasName()!=null){
                assert !set.contains(s.getAliasName());
                assert !allService.containsKey(s.getAliasName());
                set.add(s.getAliasName());
            }
        });
        log.info("别名检查通过✅");
    }

}
