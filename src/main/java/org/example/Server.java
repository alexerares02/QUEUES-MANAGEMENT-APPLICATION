package org.example;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private volatile boolean stopRequest=true;
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private Task currTask;
    private int id;
    private static int aux=0;

    public int getId() {
        return id;
    }

    public Task getCurrTask() {
        return currTask;
    }

    public void setStopRequest(boolean stopRequest) {
        this.stopRequest = stopRequest;
    }

    public Server(){
        id=++aux;
        waitingPeriod=new AtomicInteger();
        tasks=new ArrayBlockingQueue<>(5000);
    }

    public void addTask(Task newTask) throws InterruptedException {
        this.waitingPeriod.addAndGet(newTask.getServiceTime().get());
        this.tasks.put(newTask);
    }

    public void setCurrTask(Task currTask) {
        this.currTask = currTask;
    }

    @Override
    public void run() {
        while(stopRequest)
        {
            if(!this.tasks.isEmpty() || currTask!=null) {

                try {
                    if(currTask==null || currTask.getServiceTime().get()==0)
                        currTask = this.tasks.take();
                            Thread.sleep(1000L);
                            this.waitingPeriod.decrementAndGet();


                } catch (InterruptedException e) {
                    stopRequest=false;
                }

            }

        }

    }


    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public BlockingQueue<Task> getTasks(){

        return tasks;
    }


}
