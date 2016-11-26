package com.example.hipporouter.extend;

import com.example.library.router.request.impl.RouterRequest;
import com.example.library.router.router.Router;

/**
 * Created by Kevin on 2016/11/26.
 */

public class MyRouter extends Router{
    /**
     * @return MyRouter需要处理的scheme，例如myrouter://home
     */
    @Override
    public String getDefaultScheme() {
        return "myrouter";
    }

    /**
     * 处理跳转请求
     * @param request 封装了跳转url参数及上下文
     * @return 是否跳转成功
     */
    @Override
    public boolean open(RouterRequest request) {
        return false;
    }
}
