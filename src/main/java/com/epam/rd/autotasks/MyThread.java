package com.epam.rd.autotasks;


import java.util.List;

public class MyThread implements Runnable {

    private String threadName;
    private Throwable throwable = null;
    private List<FinishedThreadResult> list;
    private Runnable runnable;

    public MyThread(List<FinishedThreadResult> list, Runnable runnable) {
        this.list = list;
        this.runnable = runnable;
    }

    public void run() {
        threadName = Thread.currentThread().getName();
        try {
            runnable.run();
        }catch (Throwable throwable) {
            this.throwable = throwable;
        } finally {
            list.add(new FinishedThreadResult(threadName,throwable));
        }
    }


}