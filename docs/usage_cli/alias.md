# alias 命令的使用

> 这个命令的初衷是随机类型名太长了，这个项目最初是为WEB考虑的，WEB可以通过下拉框选择随机类型，CLI 只能手打命令，理论上可以做到自动补全，但是我翻了一遍spring shell的文档没有找到。。。不知道怎么实现。所以弄了alias系列命令。
>
> 2021/09/22 自定义补全找到方法了。（中文半天搜不出来，用英文搜出来的）[https://stackoverflow.com/questions/35624665/is-it-possible-to-tab-complete-values-in-spring-shell](https://stackoverflow.com/questions/35624665/is-it-possible-to-tab-complete-values-in-spring-shell)

## alias

**alias** [**--r-type**] string [**--alias-name**] string  

第一个参数是随机类型名，第二个参数是别名

别名不能与任何一个随机类型名相同，同名的别名会被新的别名覆盖。

## save-alias

**save-alias** [**--name**] string  

参数是存储别名的名字

> 存储别名配置相当于存储环境变量，总不能每次用都要重新设置


## read-alias

**read-alias** [**--name**] string 

参数是存储的名字

配置读取会将已设置的同名别名覆盖，会跳过与随机类型名一样的别名（这种情况应该比较少见）

## del-alias-conf

**del-alias-conf** [**--name**] string  

参数是存储的名字

## source

**source** [**--name**] string

参数是名字，功能与read-alias一样,这个命令更符合使用习惯。

## echo

**echo** [[**--name**] string]  

展示别名对应的真实随机类型

参数是别名,为空时会打印出所有别名信息。
