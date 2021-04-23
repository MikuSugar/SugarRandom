package me.mikusugar.randomsugar.app.service.impl;

import me.mikusugar.randomsugar.app.bean.SugarJsonNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RandomLongTest {

  @Autowired private RandomLong randomLong;

  private final String longType = SugarJsonNode.TYPE.LONG.toString();

  @Test
  public void test() {
    System.out.println(Long.MAX_VALUE);
    assert (randomLong.check(longType, "1,9223372036854775807,1"));
    assert (randomLong.check(longType, "1,9223372036854775807,0"));
    assert (!randomLong.check("", "1,2,0"));
    assert (!randomLong.check(longType, "2,1,a"));
    assert (!randomLong.check(longType, "2,1,1"));
    assert (!randomLong.check(longType, "22,25,3"));
    assert (randomLong.check(longType, "  4,5,1   "));
  }
}
