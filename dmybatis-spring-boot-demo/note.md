## yaml 配置信息获取

- `@Value("${键名}")`

  该注解可以将 yaml 配置文件中对应的配置注入到成员变量上，同时该注解也需要配合 `@Component` 等注解才能读取到 yaml 的配置。

- `@ConfigurationProperties(prefix = "前缀")`

  针对拥有共同前缀的配置，例如`user.name`、`user.age`等，可以直接在对应的类上使用该注解指定共同的前缀，并可以省略 `@Value` 的编写。

## Bean 管理

### Bean 扫描

Bean 扫描的注解是 `@ComponentScan(basePackage = "com.xxx")` , 指定扫描 com.xxx 目录下的包。

但是 SpringBoot 主启动类中并没有 `@ComponentScan` 注解也可以扫描主启动类所在的目录，这是因为 `@SpringBootApplication` 注解内部就已经包含 `@ComponentScan` 的注解，会默认扫描主启动类所在的目录。

因此，如果出现不在主启动类同级目录下的包，则需要手动在主启动类上使用 `@ComponentScan` 指定目录。

### Bean 注册

|    注解     |         说明          |                   使用场景                    |
| :---------: | :-------------------: | :-------------------------------------------: |
| @Component  | 声明 Bean 的基础注解  |        不适用衍生注解时，则使用此注解         |
| @Controller | @Component 的衍生注解 |                 标注控制器类                  |
|  @Service   | @Component 的衍生注解 |                  标注业务类                   |
| @Repository | @Component 的衍生注解 | 标注数据访问类(使用 mybatis 时, 不需要此注解) |



如果要注册的 Bean 对象来自第三方，则无法通过以上注解声明为 Bean 对象，可以使用更底层的注解

- `@Bean`

  在方法上使用，将方法的返回值注入到 IOC 容器中，成为 Bean 对象。该注解通常会在配置类中集中使用。

  如果方法内部需要另外一个已存在的 Bean 对象，可以将该对象直接写到方法的参数列表中，Spring 会将其自动注入。

- `@Import`

  - 导入配置类

    如果配置类位于主启动类无法扫描的位置，可以在主启动类上使用此注解，直接指定配置类，例如 `@Import(xxxConfig.class)`

  - 导入 `ImportSelector` 接口实现类

    如果存在大量需要手动指定扫描的类，则使用最基础的`@Import`就比较繁琐，这时候就可以借助 `ImportSelector` 接口实现类。

    `ImportSelector` 接口实现类可以实现批量注册。将所有需要扫描注册的包写到一份文件里面(以 .imports 为后缀)，使用 `ImportSelector` 接口实现类读取配置

    ```java
    public class CommonImportSelector implements ImportSelector {
    
        // 批量注入无法被自动扫描的类
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            // 读取配置文件的内容
            ArrayList<String> imports = new ArrayList<>();
            InputStream is = CommonImportSelector.class.getClassLoader().getResourceAsStream("common.imports");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
    
            try {
                while ((line = br.readLine()) != null) {
                    imports.add(line);
                }
    
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return imports.toArray(new String[0]);
        }
    }
    ```

    然后将 `ImportSelector` 接口实现类导入主启动类

    ```java
    @SpringBootApplication
    // 使用 ImportSelector 批量注入
    @Import(CommonImportSelector.class)
    public class Application {
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
    }
    ```

  - `@EnableXxx` 注解，封装 `@Import` 注解

    为了使代码更加优雅，可以将 `@Import(CommonImportSelector.class)` 封装为新的注解

    注解类如下

    ```java
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Import(CommonImportSelector.class)
    public @interface EnableCommonConfig {
        // 自定义注解，包含 ImportSelector
    }
    ```

    然后在主启动类中就可以这样编写

    ```java
    @SpringBootApplication
    @EnableCommonConfig
    public class Application {
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
    }
    ```

### 注册条件

SpringBoot 提供了设置注册生效条件的注解 `@Conditional`

|           注解            |                 说明                  |
| :-----------------------: | :-----------------------------------: |
|  @ConditionalOnProperty   | 配置文件中存在对应的属性时，注册 Bean |
| @ConditionalOnMissingBean | 当不存在指定类型的 Bean 时，注册 Bean |
|    @ConditionalOnClass    |   当前环境存在指定的类时，注册 Bean   |

- `@ConditionalOnProperty` 通常在尝试获取 yaml 配置信息时使用，如果有配置信息则有效，如果无配置信息则无效。
- `@ConditionalOnMissingBean`  通常在判断另一个 Bean 是否存在时使用，如果不存在则有效，如果存在则无效。(这种注册条件可能用于可相互替代的 Bean 对象上)
- `@ConditionalOnClass` 通常在判断一个指定的类是否存在时使用，如果存在则有效，如果不存在则无效。(这种注册条件可能用于具有先后依赖关系的 Bean 对象上)



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