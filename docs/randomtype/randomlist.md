# 指定列表的随机
## 有以下类型实现
+ INT
+ LONG
+ STRING
## 输入格式
以逗号分割列表。
以冒号分割权重。
权重为1时可不写权重。
权重越大随机生成的概率越大。

例如： a:1,b:4,c:5,d

## 拓展：权重随机的实现方式
灵感来自于 [https://leetcode-cn.com/problems/random-pick-with-weight/](https://leetcode-cn.com/problems/random-pick-with-weight/)

```java
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
```
