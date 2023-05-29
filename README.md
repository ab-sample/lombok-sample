# lombok-sample

这段代码演示了在jetbrains idea中生成lombok编译后的源码文件的方式

## Run

```bash
mvn clean package -U -Dmaven.test.skip=true -P release
```

## Note

### lombok-maven-plugin

完整的配置如下所


```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.qwfys.sample</groupId>
    <artifactId>lombok-sample</artifactId>
    <version>1.0-SNAPSHOT</version>

    <name>lombok-sample</name>

    <properties>
        <java.version>17</java.version>

        <resource.delimiter>@</resource.delimiter>
        <maven.compiler.release>${java.version}</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

        <lombok-maven-plugin.version>1.18.20.0</lombok-maven-plugin.version>
        <lombok.version>1.18.26</lombok.version>

        <!-- 原始源码目录，我们后边要将源码目录切换到delombok.dir中 -->
        <origin.source.dir>src/main/java</origin.source.dir>
        <!-- 打包生成source时使用的目录 -->
        <source.generate.dir>src/main/java</source.generate.dir>
        <!-- lombok生成源码的目录 -->
        <delombok.dir>${project.build.directory}/delombok</delombok.dir>

        <maven-resources-plugin.version>3.3.1</maven-resources-plugin.version>
        <maven-compiler-plugin.version>3.11.0</maven-compiler-plugin.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>3.8.1</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <optional>true</optional>
        </dependency>
    </dependencies>

    <build>
        <!-- 使用delombok的源码目录作为项目的源码目录 -->
        <sourceDirectory>${source.generate.dir}</sourceDirectory>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
                <configuration>
                    <parameters>true</parameters>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <version>${maven-resources-plugin.version}</version>
                <configuration>
                    <propertiesEncoding>${project.build.sourceEncoding}</propertiesEncoding>
                    <delimiters>
                        <delimiter>${resource.delimiter}</delimiter>
                    </delimiters>
                    <useDefaultDelimiters>false</useDefaultDelimiters>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <profiles>
        <profile>
            <id>release</id>
            <properties>
                <!-- 源码目录切换到delombok目录 -->
                <source.generate.dir>${delombok.dir}</source.generate.dir>
            </properties>
            <build>
                <plugins>
                    <plugin>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok-maven-plugin</artifactId>
                        <version>${lombok-maven-plugin.version}</version>
                        <configuration>
                            <encoding>${project.build.sourceEncoding}</encoding>
                            <!-- 指定要解除lombok注释的代码在这个目录中 -->
                            <sourceDirectory>${origin.source.dir}</sourceDirectory>
                            <!--
                                delombok后的源码默认是输出到target/generated-sources/delombok目录中的，这里我们修改输出到我们指定的目录，因为
                                如果输出到target/generated-sources中会导致IDEA也将其识别为源码，最终就是导致IDEA报错，因为class重复
                            -->
                            <outputDirectory>${delombok.dir}</outputDirectory>
                        </configuration>
                        <executions>
                            <execution>
                                <phase>generate-sources</phase>
                                <goals>
                                    <goal>delombok</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-source-plugin</artifactId>
                        <version>3.2.1</version>
                        <executions>
                            <execution>
                                <phase>package</phase>
                                <goals>
                                    <goal>jar-no-fork</goal>
                                </goals>
                            </execution>
                        </executions>
                    </plugin>
                </plugins>
            </build>
        </profile>
    </profiles>
</project>
```
如果不是在jetbrains中运行，可以将上述代码中的`<outputDirectory>${delombok.dir}</outputDirectory>`所在行注释掉，也是可以的，这个时候，lombok生成的java文件将会存放在目录`target/generated-sources/delombok`中。

### maven-compiler-plugin

从JDK 9开始，java编译器启用了--release参数项，故而`maven-compiler-plugin`也从3.6开始拥抱了这一变化，上述代码中，

```xml
    <properties>
        <java.version>17</java.version>
        <maven.compiler.release>${java.version}</maven.compiler.release>
    </properties>
```
这部分也是正好应用了这一特性。


>参考文献
>- [Lombok Maven Plugin](http://anthonywhitford.com/lombok.maven/lombok-maven-plugin/usage.html)
>- [使用了lombok后如何生成正确源码包](https://blog.csdn.net/AS011x/article/details/126744011)
>- [Setting the --release of the Java Compiler](https://maven.apache.org/plugins/maven-compiler-plugin/examples/set-compiler-release.html)

