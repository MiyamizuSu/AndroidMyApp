**项目清单**

-   技术架构
>   - **AndroidJetCompose** 
>>  一种用组合式编程的方式代替xml的开发方式
>   -   编程语言为Kotlin  API版本为27   

-   项目目标
>   制作一个**备忘录**
>   功能点如下:
>>  1.  显示当前日期，当地天气
>>  2.  显示之前记录的发生的事情，距离如今有多远，发生的次数
>>  3.  显示之后要做的事情，距离如今有多远，要做的事情有什么主要事项
>>  4.  实现图片的存储

-   项目进度
>   1.  事已至此,先学习开发Android的技术吧  2024.11.8
>   2.  主页面已经搭建完毕了 2024.11.14


-   技术点
>   -  每一个组件的样式定义在modifier属性当中,可以达到类似于css的效果,并且modifier的函数调用是流式调用,有前后顺序
>   -  padding在backgroud之后就是内边距,在background前就是外边距,如果没有background 直接使用padding的效果就是margin(都是流式调用的惹得锅)
>   -  remember { mutableStateOf(T t) } 相当于vueJs中的v-model,声明了一个数据绑定
>   -  页面的填充机制，使得父容器的大小实际上会不断变小(也就是被消耗),导致fillmaxsize时,可能第一个是50%,但是第二个就变成了50%*50%了(注意!)
>   -  根据官方文档，room框架的数据库操作应当使用协程，避免因为io而导致主线程ui渲染被阻塞，启动协程的api为
>   ```CoroutineScope(Dispatchers.io).launch {doSomething()}```
>   -  在kotlin中异步方法为使用协程，LaunchedEffect(status)可以启动协程
