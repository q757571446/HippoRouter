###写在前面
在Hybrid框架中，首要需要解决的问题是**原生与非原生界面之间的跳转问题**。传统的解决外链跳转问题无非两种：
>1. 基于Activity: AndroidManifest.xml中定义intent-filter，通过外链url携带的信息匹配intent-filter，隐式启动对应Activity。
>2. 基于Fragment: 启动指定Activity,通过外链携带Fragment的包名类名信息，通过反射实例化Fragment替换container。
    
方案1弊端在于，每需要跳转一个原生页面都需要去清单文件中声明，不够灵活且违背引入hybrid框架的动态化初衷。
方案2通过Fragment不需要再清单文件中声明，利用反射解决了方案1的弊端。但仍然存在的问题是风险过高，一旦包名信息出错，反射实例化就会出问题。同时Fragment一旦定义，就不能在轻易更改包名，不利于重构。



###解决方案

传统的startActivity本质上是一个编译时跳转，因为我们需要知道目标界面的Class文件。而外链跳转本质上是一个运行时跳转，我们可以通过一个url动态指定目标页面。所以问题的核心**在于如何解决编译时跳转和运行时跳转的冲突**。
仔细思考上面两种方案的核心在于解决**别名——》目标界面**的映射问提。
> 方案1： 通过intent-filter,解决url——》activity映射关系
> 方案2： 通过反射机制,解决ClassName——》Fragment映射关系

基于此我们可以提出第三种方案，我们可以在原生维护一张**Alias——>Class的路由表**，外链就可以通过别名取得目标界面的Class对象，从而完成跳转。这样就避免了方案1隐式启动需要在清单文件中预先定义的问题，方案2反射的不安全问题。

###存在问题
在实际界面跳转中，存在着很多参数携带情况发生。因此一个简单的别名虽然可以完成跳转，却无法解决这些复杂的需求。因此我们**需要对Alias做些制定一些Restful规范**。
####1. Activity
Alias | Class 
----|------
activity://wrap | WrapActivity  
activity://main/:s{name}/:i{password}?id=123456 | MainActivity  

    scheme: 必须要有
        activity标记这个uri应该被ActivityRouter打开
    host: 必须要有
        main作为MainActivity标识区别于其他uri
    path: 可有可无
        :s{name}标识打开MainActivity所需要一个key为name,String类型的参数, 不传递name打不开MainActivity
        :i{password}标识打开MainActivity所需要一个key为password,Integer类型的参数，不传递password打不开MainActivity
    parameter：可有可无
        id=123456作为打开MainActivity的附加参数

 
####2. Fragment
Alias | Class 
----|------
fragment://login/:s{name}/:i{password}?id=123456#wrap | LoginFragment  

    Scheme: 必须要有
        fragment标记这个uri应该被FragmentRouter打开
    Host: 必须要有
        login作为LoginFragment标识区别于其他uri
    Path: 可有可无
        :s{name}标识打开LoginFragment所需要一个key为name,String类型的参数, 不传递name打不开MainActivity
        :i{password}标识打开LoginFragment所需要一个key为password,Integer类型的参数，不传递password打不开MainActivity
    Parameter：可有可无
        id=123456作为打开LoginFragment的附加参数
    Fragment: 必须要有
        #wrap说明此fragment所需要attach的Activity，这里指的是WrapActivity。
**注意#wrap一定要有，他标记Fragment所需要attach的Activity，这里wrap对应WrapActivity别名的host**

###如何使用
####  1. 注册
* application中注册路由表
```
   
    RouterManager.getSingleton().registerActivityRouter(new ActivityRouterFactory() {
        @Override
        public Map<String, Class> getRouterTable() {
            Map<String, Class> map = new HashMap<>();
            map.put("activity://content/:s{username}", ContentActivity.class);
            return map;
        }
    });
```
* 也可以通过RouterMap注解
```
    @RouterMap("activity://content/:s{username}")
    public class ContentActivity extends Activity{
        ...
    }
```

####  2. 跳转
```
    //通过路由表中注册的别名完成跳转
    RouterRequest.from(this,"activity://content/kevin")
        .open();
```

####  3. 取值    
```
    //Activity中通过getIntent取值，Fragment中通过getArguments()取值
    getIntent().getStringExtra("username");
```

####详细使用请看Demo
