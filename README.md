# LowEventBus
低配版EventBus，学习用

# 用法如下

``android
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 注册
        LowEventBus.register(this);
        // 发送事件
        LowEventBus.post(new Event(10));

    }


    @Subscribe
    public void onAccept(Event event) {
        Log.d("MyTAG", "接到事件, id="+event.getId());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 取消注册
        LowEventBus.unRegister(this);
    }
}
``
