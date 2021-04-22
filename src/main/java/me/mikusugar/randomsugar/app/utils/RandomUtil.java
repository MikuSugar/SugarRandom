package me.mikusugar.randomsugar.app.utils;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * author: fangjie email: syfangjie@live.cn date: 2021/3/29 11:03 上午
 */
public class RandomUtil {

  private static final Random random = new Random();

  public static <T> RandomUtilInterface<T> getRandomData(List<T> list) {
    assert list != null && list.size() > 0;
    return () -> list.get(random.nextInt(list.size()));
  }

  public static <T> RandomUtilInterface<T> getRandomWeightData(Map<T, Integer> map) {
    assert map != null && map.size() > 0;

    final TreeMap<Integer, T> treeMap = new TreeMap<>();

    int size = 0;
    for (Map.Entry<T, Integer> e : map.entrySet()) {
      size += e.getValue();
      treeMap.put(size - 1, e.getKey());
    }
    final int len = size;
    return () -> treeMap.ceilingEntry(random.nextInt(len)).getValue();
  }

  public static RandomUtilInterface<Long> getRandomLong(long start, long end) {
    assert start <= end;
    return () -> start + Math.abs(random.nextLong() % (end - start)) % (end - start);
  }

  public static RandomUtilInterface<Integer> getRandomInt(int start, int end) {
    assert start <= end;
    return () -> start + random.nextInt(end - start);
  }

  public static RandomUtilInterface<Long> getRandomGaussianLong(long start, long end) {
    assert start <= end;
    return () -> (long) random.nextGaussian() * (end - start) + (end - start) / 2 + start;
  }

  public static RandomUtilInterface<Integer> getRandomGaussianInt(int start, int end) {
    assert start <= end;
    return () -> (int) (random.nextGaussian() * (end - start)) + (end - start) / 2 + start;
  }

}
