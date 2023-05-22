package org.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable {

    public  int timeLimit;//maximum processing time - read from UI
    public  Integer queues;
    public  Integer tasks;
    public  Integer minArrival;
    public  Integer maxArrival;
    public  Integer minService;
    public  Integer maxService;
    private int currentTime;
    private FileWriter log;
    private int peakHour=0;
    private int timePeakHour=0;
    private static double averageWaitingTime=0.0;
    private static double averageServiceTime=0.0;
    private static int count=0;
    private boolean ok=true;



    //entity responsible with queue management and client distribution
    private Scheduler scheduler;
    //frame for displaying simulation
    private SimulationFrame frame;
    //pool of tasks (clients shopping in the store)
    private List<Task> generatedTasks=null;

    public SimulationManager(int timeLimit, int queues, int tasks, int minArrival, int maxService, int minService, int maxArrival) {
        try {
            log=new FileWriter("src/main/java/org/example/log.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scheduler = new Scheduler(queues, tasks);
        this.tasks=tasks;
        this.timeLimit=timeLimit;
        this.queues=queues;
        this.minArrival=minArrival;
        this.maxArrival=maxArrival;
        this.minService=minService;
        this.maxService=maxService;
        generateNRandomTasks(maxArrival, minArrival, maxService, minService, tasks);
    }

    public static void addAverageWaitingTime(int number){

        averageWaitingTime+=number;
    }

    public static void addAverageServiceTime(int number){
        averageServiceTime+=number;
    }

    public void peakHour(List<Server> servers, int time){
        int noTasks = 0;
        for(Server s:servers){
            noTasks+=s.getTasks().size();
        }
        if(noTasks>peakHour)
        {
            peakHour=noTasks;
            timePeakHour=time;
        }

    }


    private void generateNRandomTasks(int maxArrival, int minArrival, int maxService, int minService, int tasks) {
        generatedTasks = new ArrayList<>();

        Task.setAux(1);

        Random random = new Random();
        for (int i = 0; i < tasks; i++) {
            Task aux = new Task((random.nextInt(maxArrival - minArrival + 1) + minArrival), (random.nextInt(maxService - minService + 1) + minService));
            generatedTasks.add(aux);
        }

    }

    @Override
    public void run() {
        currentTime = 0;
        while (currentTime <= timeLimit && ok) {
            ok=false;
            for(int i=0;i<generatedTasks.size();i++){
                if(currentTime==0) {
                    addAverageServiceTime(generatedTasks.get(i).getServiceTime().get());
                }
                if(generatedTasks.get(i).getArrivalTime()>currentTime)
                    ok=true;
                if(generatedTasks.get(i).getArrivalTime()==currentTime){
                    try {
                        scheduler.dispatchTask(generatedTasks.get(i));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            System.out.println(this.toString());
            try {
                log.write(this.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            peakHour(scheduler.getServers(),currentTime);

            for(Server s: scheduler.getServers()){
                if(s.getWaitingPeriod().get()!=0){
                    addAverageWaitingTime(s.getWaitingPeriod().get());
                    count++;
                }

                if(!s.getTasks().isEmpty())
                    ok=true;

                if(s.getCurrTask()!=null) {
                    ok=true;

                    s.getCurrTask().decrementServiceTime();
                    if(s.getCurrTask().getServiceTime().get()==0)
                    {
                        s.setCurrTask(null);
                    }
                }

            }

            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
        try {
            log.write("Peak hour:"+String.valueOf(timePeakHour)+"\n");
            log.write("Average service time:"+String.valueOf(averageServiceTime/(tasks*1.0))+"\n");
            log.write("Average waiting time:"+String.valueOf(averageWaitingTime/(count*1.0))+"\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        for(Server s:scheduler.getServers()){
            s.setStopRequest(false);
        }


        try {
            log.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString(){
        String s="Time "+String.valueOf(currentTime)+"\nWaiting clients:";
        for(Task t:generatedTasks){
            if(currentTime<t.getArrivalTime())
            s+="("+String.valueOf(t.getId())+", "+ String.valueOf(t.getArrivalTime())+", "+String.valueOf(t.getServiceTime())+"), ";
        }
        s+="\n";

        for(Server a: scheduler.getServers()){
            s+="Queue "+String.valueOf(a.getId())+": ";
            if(a.getCurrTask()!=null){
                s+="("+String.valueOf(a.getCurrTask().getId())+", "+ String.valueOf(a.getCurrTask().getArrivalTime())+", "+String.valueOf(a.getCurrTask().getServiceTime())+"), ";
            }
            for(Task t:a.getTasks()){
                s+="("+String.valueOf(t.getId())+", "+ String.valueOf(t.getArrivalTime())+", "+String.valueOf(t.getServiceTime())+"), ";
            }

            s+="\n";
        }
        return s;
    }

    public static void main(String[] args) {
        SimulationFrame frame=new SimulationFrame();
    }



}
