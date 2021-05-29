SpringMVC的配置和拓展(重点难点)

·视图解析器
1.建个config文件夹
2.创建一个配置类 #MyMvcConfig.java# (/*全面接管并*/扩展SpringMVC的配置)
3.类上使用注解@Configuration
4.实现接口 implements WebMvcConfigurer
[5.使用@EnableWebMvc将全面接管SpringMVC的配置 就不保留默认的设置了 这里注意不能加这个注解
@EnableWebMvc这个类就是导入Import一个DelegatingWebMvcConfiguration类,
而DelegatingWebMvcConfiguration类继承WebMvcConfigurationSupport
回过头看WebMvcAutoConfiguration上方有个@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
因此,到使用@EnableWebMvc注解时,WebMvcAutoConfiguration全部失效]
6.接管视图解析器(之前是手动在spring的配置文件.xml中配置)
    [默认的ContentNegotiatingViewResolver类]实现了[ViewResolver接口]
    实现了视图解析器接口ViewResolver的类 就能成为视图解析器,现在手动实现接管
    查看ViewResolver接口发现有一个叫[resolveViewName]的方法,看ContentNegotiatingViewResolver中
    是如何重写的
    1使用了一个getCandidateViews方法,其中
    2遍历了所有的视图解析器 ViewResolver viewResolver: this.viewResolvers(从容器中得到)
    3添加到候选的视图
    4返回所有的候选视图
    双击shift找到DispatcherServlet.class类中找到doDispatch,这里打断点,debug,
    在浏览器地址栏输入localhost:8080/可以看到this,点开this可以看到viewResolver的列表
    里面有Thymeleaf的视图解析器,还有自己定义的视图解析器
7.结论:只要实现WebMvcConfigurer接口,内部定义一个实现ViewResolver接口的类,
创建这个类并@Bean注入到Spring容器中让SpringBoot接管即可.
但是一般不是用自定义的视图解析器
8.在MyMvcConfig中重写一个视图跳转方法
@Override
public void addViewControllers(ViewControllerRegistry registry){
    // 在这里进行url和视图的映射
    registry.addViewController("/addViewControllers").setViewName("thymeleafTest.html");
}
数据部分为空,因为没有传数据


·[消息格式化转换器]
在WebMvcConfiguration中找到FormattingConversionService mvcConversionService()方法
里面有个this.mvcProperties.getDateFormat说明可以在配置文件中进行设置,点进去找,
源码(要下载)注解上有 默认的日期格式 "dd/MM/yyyy"
在全局配置文件application.yaml中进行配置

===========>
总结:
[SpringBoot中,有非常多的xxxConfiguration(注意:不是xxxAutoConfiguration)
这些配置类有的被XXXAutoConfiguration所使用,有的帮助进行扩展配置,当看到这样的配置类,要注意它们
扩展了什么功能]