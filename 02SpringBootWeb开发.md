SpringBoot Web 开发
jar -- > webapp
SpringBoot最大特点:自动装配
·SpringBoot应用步骤
1.创建应用
2.选择模块,在pom.xml中导入对应的启动器依赖
3.XxxAutoConfiguration根据[ConditionalOnXxxx]装配组件(从XxxxProperties类中读取默认值)
  从全局配置文件application.yaml中读取XxxxProperties类绑定的值 覆盖默认值

· 思考
1.SpringBoot自动配置了哪些东西
2.是否可以进行修改
3.如何修改
4.是否可以扩展
org.springframework.boot:spring-boot-Autoconfiguration下
META-INFO里的spring.factories中有许多1XxxAutoConfiguration

· 要解决的问题
1.导入静态资源 js...
2.首页
3.jsp? 模板引擎:Thymeleaf
4.装配扩展SpringMVC
5.增删改查
6.拦截器
7.国际化

· 静态资源问题
1.打开[WebMVCAutoConfiguration]进行查看
找到静态类WebMvcAutoConfigurationAdapter
1.1他的构造方法里就传入了一个ResourceProperties对象可以由Properties配置
里面定义了四个默认的路径
# "classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/"
即在目录resources下还可以有以下三个这三个文件夹
resources[访问优先级1-一般存放上传的文件] 
static[访问优先级2-一般存放图片] 
public[访问优先级3-一般存放公共访问的js等]。
[访问优先级:即在文件加下有同名文件时,优先访问优先级高的文件夹下的资源]
1.2他的addResourceHandlers 方法 
public void addResourceHandlers(ResourceHandlerRegistry registry) { 
    [若静态资源的属性已经被自定义了,默认的资源处理器就失效
    如何自定义,找到类WebMvcAutoConfigurationAdapter上方的WebMvcProperties配置文件
    里关于静态资源的配置:staticPathPattern="/**"]
    if (!this.resourceProperties.isAddMappings()) {
        logger.debug("Default resource handling disabled");
    } else {
        [什么是webjars:以maven在pom.xml导入依赖的方式导入js(如jQuery.js)
        <groupId>org.webjars</groupId>
         <artifactId>jquery</artifactId>
        即使用webjars(在左侧dependencies中可以找到)统一管理静态资源]
        Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
        CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
        if (!registry.hasMappingForPattern("/webjars/**")) {
        [路径映射:classpath:/META-INF/resources/webjars/映射成/webjars/**在地址栏输入相应信息即可访问静态资源]
            this.customizeResourceHandlerRegistration(registry.addResourceHandler(new String[]{"/webjars/**"})
            .addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"}).setCachePeriod(this.getSeconds(cachePeriod)).setCacheControl(cacheControl));
        }
        [这里staticPathPattern是在WebMvcAutoConfigurationAdapter上的标签里的WebMvcProperties文件中设置:/**]
        String staticPathPattern = this.mvcProperties.getStaticPathPattern();
        if (!registry.hasMappingForPattern(staticPathPattern)) {                             这个来自WebMVCProperties                                                                                         
            this.customizeResourceHandlerRegistration(registry.addResourceHandler(new String[]{[staticPathPattern]}).[addResourceLocations]
                                                            这个来自ResourceProperties
            (WebMvcAutoConfiguration.getResourceLocations(this.[resourceProperties].getStaticLocations())).setCachePeriod(this.getSeconds(cachePeriod)).setCacheControl(cacheControl));
        }

    }
}
1.3以上就是关于静态资源路径的一些源码
总结:
1.3.1 在SpringBoot中,可以使用一下方法处理静态资源
· webjars localhost:8080/webjars/...
· resources/ static/ public/ 和 /**的[拼接] 映射到 localhost:8080/**
· 在与java目录同级的resources的静态资源不会被访问!
/**就可以访问到上述三个文件夹中所有的静态资源(包括子文件夹下的)
1.3.2 自定义静态资源路径[(由源码得出)= spring.resources.static-locations + spring.mvc.static-path-pattern]
yaml文件
spring.resources.static-locations: [classpath:/resources/, classpath:/static/, classpath:/public/]
spring.mvc.static-path-pattern: [/*.txt, /*.js]
properties文件
spring.resources.static-locations=classpath:/resources/, classpath:/static/, classpath:/public/
spring.mvc.static-path-patterns=/*.js, /*.txt
或者
spring.mvc.static-path-pattern[0]=/*.js
spring.mvc.static-path-pattern[1]=/*.txt

· 首页定制问题 还是在[WebMvcAutoConfiguration]里寻找相关配置
然后找到对应的XxxProperties在全局配置文件中设置
1.找到一个bean WelcomePageHandlerMapping(里面的资源路径就与上面的静态资源配置有关)
2.getWelcomePage->getIndexHtml->return this.resourceLoader.getResource(location + "index.html")
即在resources/ static/ public/三个路径下的index界面都可以起作用(按优先级呈现)
# template文件夹就跟原来的WEB-INF是一样的 只有由controller跳转到这个目录下的页面(需要引入模板引擎)
[3.在某个文件夹下没有自己想要的文件类型,直接edit File Template加入对应类型的模板即可]
4.乱码问题:在yaml配置文件中加入
spring:
  http:
    encoding:
      force: true
5.遗留:网站的图标favicon.ico问题
