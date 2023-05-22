package org.example;


import java.util.concurrent.atomic.AtomicInteger;

public class Task {

    private int id;
    private static int aux=1;
    private int arrivalTime;
    private AtomicInteger serviceTime;

    public Task(int arrivalTime, int serviceTime){
        this.id=aux;
        aux++;
        this.arrivalTime= arrivalTime;
        this.serviceTime=new AtomicInteger(serviceTime) ;
    }

    @Override
    public String toString() {
        return this.id+" "+this.arrivalTime+" "+this.serviceTime;
    }

    public int getId() {
        return id;
    }

    public static void setAux(int aux) {
        Task.aux = aux;
    }

    public AtomicInteger getServiceTime() {
        return serviceTime;
    }

    public void decrementServiceTime(){
        this.serviceTime.decrementAndGet();
    }

    public int getArrivalTime() {
        return arrivalTime;
    }
}
