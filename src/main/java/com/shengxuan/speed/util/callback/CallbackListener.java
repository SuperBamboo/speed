package com.shengxuan.speed.util.callback;

public interface CallbackListener {
    /**
     * 请求成功
     * @param response
     * code:0成功  1失败
     */
    void onFinish(int code,String response);

    /**
     * 请求异常
     * @param e
     */
    void onError(Exception e);
}
