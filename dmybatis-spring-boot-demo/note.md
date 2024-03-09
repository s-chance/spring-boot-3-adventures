## SpringBoot 的自动配置原理

- 在主启动类上添加了 SpringBootApplication 注解，这个注解组合了 EnableAutoConfiguration 注解
- EnableAutoConfiguration 注解又组合了 Import 注解，导入了 AutoConfigurationImportSelector 类
- 这个类实现了 selectImports 方法，这个方法经过层层调用，最终会读取 META-INF 目录下后缀名为 imports 的文件（在 boot 2.7 以前的版本，读取的是 spring.factories 文件）
- 读取到 imports 中的全类名之后，会解析注册条件，也就是 @Conditional 及其衍生注解，把满足注册条件的 Bean 对象自动注入到 IOC 容器中。

## 自定义 starter

需求：自定义 mybatis 的 starter

步骤

- 创建 dmybatis-spring-boot-autoconfigure 模块，提供自动配置功能，并自定义配置文件 META-INF/spring/xxx.imports
- 创建 dmybatis-spring-boot-starter 模块，在 starter 中引入自动配置模块

dmybatis-spring-boot-autoconfigure 模块的操作：修改 pom.xml -> 编写 MybatisAutoConfig 自动配置类 -> 编写 imports 指向自动配置类

dmybatis-spring-boot-starter 模块的操作：修改 pom.xml，内容和 autoconfigure 模块相比只是多了对自定义 autoconfigure 的引入，此外该模块不需要任何其它操作，仅仅是作为依赖管理的模块