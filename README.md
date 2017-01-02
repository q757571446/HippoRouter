# HippoRouter
A router to solve rpc communication
## Example
### Activity
#### Use
```java
 ActivityRequest.from(this, "activity://example/:kevin/:123455")
                .open();
```
#### Register
```java
@RouterMap("activity://example/:s{username}/:i{password}")
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
@RouterMap("fragment://example")
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

## Extend

## Installation
