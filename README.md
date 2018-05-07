# doc-mapping
文件（excel，txt，csv）和java bean list映射功能，可方便的用于项目中文件的导入导出功能；支持多种配置方式：spring xml，spring namespace，annotation等方式配置。

1. spring xml配置方式：
详见test代码中spring-import.xml，spring-export.xml，对应main入口SpringParseMain.java，SpringBuildMain.java

2. spring namespace配置方式：
详见test代码中namespace-import.xml，namespace-export.xml，对应main入口NamespaceParseMain.java，NamespaceBuildMain.java

3. annotation配置方式：
详见test代码中的java包org.doc.mapping.test.config，对应main入口AnnotationParseMain.java，AnnotationBuildMain.java

