package com.epam.rd.autotasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadImpl implements ThreadUnion {

    private List<Thread> threadList1 = new ArrayList<>();

    private List<FinishedThreadResult> finishedThreadResults = Collections.synchronizedList(new ArrayList<>());

    private String name;

    private boolean isShutdown;

    public ThreadImpl(String str) {
        this.name = str;
        this.isShutdown = false;
    }

    @Override
    public int totalSize() {
        return threadList1.size();
    }

    @Override
    public int activeSize() {
        int count = 0;
        for (Thread thread : threadList1) {
            if (thread.getState().toString().equals("RUNNABLE") || thread.getState().toString().equals("WAITING")) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void shutdown() {
        isShutdown = !isShutdown;
        for (Thread thread : threadList1) {
            if (thread.isAlive()) {
                thread.interrupt();
            }
        }
    }

    @Override
    public boolean isShutdown() {
        return isShutdown;
    }

    @Override
    public void awaitTermination() {
        for (Thread thread : threadList1) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean isFinished() {
        if (isShutdown) {
            for (Thread thread : threadList1) {
                if (!thread.getState().toString().equals("TERMINATED")) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<FinishedThreadResult> results() {
        return finishedThreadResults;
    }

    @Override
    public synchronized Thread newThread(Runnable r) throws IllegalStateException{
        if (!isShutdown) {
            Thread thread = new Thread(new MyThread(finishedThreadResults, r), name + "-worker-" + threadList1.size());
            threadList1.add(thread);
            return thread;
        } else {
            throw new IllegalStateException();
        }

    }
}