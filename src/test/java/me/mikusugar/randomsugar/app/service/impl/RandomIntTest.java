package me.mikusugar.randomsugar.app.service.impl;

import me.mikusugar.randomsugar.app.bean.SugarJsonNode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RandomIntTest {

  @Autowired private RandomInt randomInt;

  private final String intType = SugarJsonNode.TYPE.INT.toString();

  @Test()
  public void testParse() {
    assert (randomInt.check(intType, "1,2,1"));
    assert (randomInt.check(intType, "1,2,0"));
    assert (!randomInt.check("", "1,2,0"));
    assert (!randomInt.check(intType, "2,1,a"));
    assert (!randomInt.check(intType, "2,1,1"));
    assert (!randomInt.check(intType, "22,25,3"));
    assert (randomInt.check(intType, "  4,5,1   "));
  }
}
