package com.shengxuan.speed.socket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class ThreadTask<T>  {


    /**
     * 线程池，创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
     */
    private static ExecutorService executorService = Executors.newFixedThreadPool( 1) ;

    private MainHandler mainHandler = new MainHandler();

    public ThreadTask(){

    }


    /**
     * 任务开始之前调用，运行在主线程
     */
    public void onStart(){ }

    /**
     * 子线程中调用，运行在子线程
     * @return
     */
    public abstract T onDoInBackground() ;

    /**
     * 子线程返回的结果，运行在主线程
     * @param t
     */
    public void onResult( T t ){ }


    /**
     * 开始执行
     */
    public void execute(){
        onStart();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                T result = onDoInBackground();
                mainHandler.sendMessage(result);
            }
        });
    }

    private  class MainHandler{
        private void sendMessage(T result){
            onResult(result);
        }
    }



}