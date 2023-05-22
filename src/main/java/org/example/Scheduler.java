package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Scheduler {

    private List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;


    public Scheduler(int maxNoServers, int maxTasksPerServer){
        this.maxNoServers=maxNoServers;
        this.maxTasksPerServer=maxTasksPerServer;
        this.servers=new ArrayList<Server>();
        for(int i=0;i<maxNoServers;i++){
            Server s=new Server();
            this.servers.add(s);
        }

        for(Server s:servers){
            Thread serverSideThread=new Thread(s);
            serverSideThread.start();
        }
    }

    public void addTask(List<Server> servers, Task t) throws InterruptedException {
        // Auto-generated method sub
        int min=servers.get(0).getWaitingPeriod().get();
        Server aux = servers.get(0);
        for(int i=1;i<servers.size();i++){
            if(min>servers.get(i).getWaitingPeriod().get())
            {
                aux=servers.get(i);
                min=servers.get(i).getWaitingPeriod().get();
            }
        }
            aux.addTask(t);


    }



    public void dispatchTask(Task t) throws InterruptedException {
        addTask(servers,t);
    }

    public List<Server> getServers(){
        return servers;
    }



}
