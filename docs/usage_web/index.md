# 快速使用

> 部分截图可能与当前版本有细微差别，不影响教程。

## 开始之前

如果你还没有安装，请前往[安装](../setup/index.md)

## 开始

依据你的端口配置，打开网站，如默认端口则打开 http://localhost:8080

你将看到如下页面

![web](https://cdn.jsdelivr.net/gh/mikusugar/PictureBed@master/uPic/2021/08/web.png)

## 使用

我们将在这个例子里快速随机生成一批json。

这个例子的json里面有两个字段，分别是姓名(name)、工作过的城市列表(citys)。

+ 设置`name`字段

  ![name](https://cdn.jsdelivr.net/gh/mikusugar/PictureBed@master/uPic/2021/08/name.gif)

  如上图设置name字段后点下一个

+ 设置`citys`字段

  ![cityss](https://cdn.jsdelivr.net/gh/mikusugar/PictureBed@master/uPic/2021/08/cityss.gif)

  如上图，我们设置该字段为数组，在随机参数依据提示设置了数组长度为1到5

  点击下一个

+ 设置`citys`里的内容

  ![city](https://cdn.jsdelivr.net/gh/mikusugar/PictureBed@master/uPic/2021/08/city.gif)

  如上图，设置`citys`里的内容，这里的字段名只是一个标记，字段父亲的选择通过在结构预览界面点击`citys`，**一定要在下一个按钮点击前通过结构预览界面选择父节点哦。**

  点击下一个

+ 节点删除

  ![del](https://cdn.jsdelivr.net/gh/mikusugar/PictureBed@master/uPic/2021/08/del.gif)

  如上图,在**结构预览**界面选择需要删除的节点，点击删除按钮即可。

+ 结构预览

  ![VGOsSC](https://cdn.jsdelivr.net/gh/mikusugar/PictureBed@master/uPic/2021/06/VGOsSC.png)

  在结构预览里可以自由点击查看之前的配置。通过点击选择当前节点，当前节点可作为新加节点的父亲或者点击删除时选中的节点。

+ 生成

  在生成条数设置条数,然后点击开始。

  ![ou0OvC](https://cdn.jsdelivr.net/gh/mikusugar/PictureBed@master/uPic/2021/06/ou0OvC.png)

  ![HgjaSn](https://cdn.jsdelivr.net/gh/mikusugar/PictureBed@master/uPic/2021/06/HgjaSn.png)

  看，数据已经生成了，造数变得如此简单了，无需Code～