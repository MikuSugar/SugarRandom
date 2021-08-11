# 表达式随机

## 初衷

也许现有的随机类型并不能满足需求，但是有些简单的随机又不至于去新开发类（[开发参考](https://mikusugar.me/SugarRandom/develop/newtype/)）, 或者是当前随机的二次处理。

所以表达式诞生了。

表达式随机参数有两个，分别是数据类型和表达式，用逗号分割。



其中 表达式引擎用的是[aviatorscript](https://www.yuque.com/boyan-avfmj/aviatorscript/cpow90),具体的语法参考文档。

在官方文档的基础上添加了一个函数 **sugar(String name,String input)**,第一个参数为随机服务名，第二个参数为该随机服务接收的输入。



## 栗子

### 手机号加密

在目前随机生成手机号的基础上对中间4位加密

表达式为

`a=sugar('随机中国大陆手机号','');string.substring(a,0,3)+'****'+string.substring(a,7)`

![exprphone](https://cdn.jsdelivr.net/gh/mikusugar/PictureBed@master/uPic/2021/08/exprphone.gif)