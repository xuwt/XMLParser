XMLParser
=========

 Android 官方的pull解析XmlDemo
------
 **1. 选择解析器 Choose a Parser**


官方推荐XmlPullParser，其在android中对XML的解析是高效且可维护的。android已经拥有该接口的两个实现：

KXmlParser，通过XmlPullParserFactory.newPullParser()创建。
ExpatPullParser，通过Xml.newPullParser()创建。
任意的选择都是可以的，这节中的例子用的是Xml.newPullParser()创建的ExpatPullParser。

**2. 分析数据源 Analyze the Feed**

解析数据源的第一步是决定哪些属性字段是你需要的， 解析器将提取你需要的属性字段而忽略其他的。以下显示的片段，是来自例子程序中正在被解析的数据源。每一个指向StackOverflow.com的请求都做为一个entry标签显示在数据源中，并且每一个entry标签都含有几个内嵌标签：

解析的url地址是下面的：

   http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest