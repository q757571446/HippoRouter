# HippoRouter
Router for android
## Example
### Activity
#### Use
```java
 ActivityRequest.from(this, "activity://example/kevin/123455")
                .open();
```
#### Register
```java
@Route("activity://example/:s{username}/:i{password}")
public class ExampleActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        initDataFromIntent();
    }

    public void initDataFromIntent() {
        //get params in intent
        String username = getIntent().getStringExtra("username");
        int password = getIntent().getIntExtra("password", -1);
    }
}
```
### Fragment
#### Use
```java
FragmentRequest.from(getFragmentManager(), "fragment://example")
        .withParams("username","kevin")
        .withParams("password",123456)
        .attach(R.id.container);
```
#### Register
```java
@Route("fragment://example")
public class ExampleFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_example,null);
    }
    
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDataFromArguments();
    }

    public void initDataFromArguments() {
        //get params in arguments
        String username = getArguments().getString("username");
        int password = getArguments().getInt("password");
    }
}
```

## Intercept


## Installation

1. in root `build.gradle`
```
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        //add the plugin of apt
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
}

allprojects {
    repositories {
        jcenter()
        //add the maven repository
        maven{
            url 'https://codingboy.bintray.com/maven'
        }
    }
}
```
2. in module `build.gradle`
```
apply plugin: 'com.neenbedankt.android-apt'
dependencies {
    compile 'com.hippo.router:hipporouter:0.0.1'
    apt 'com.hippo.router:hipporouter-compile:0.0.1'
}
```
