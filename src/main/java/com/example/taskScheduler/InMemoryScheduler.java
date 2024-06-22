package com.example.taskScheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class InMemoryScheduler {

    //going to store a list of tasks, sorted by time (The one that has time early will be executed first)
    //going to initialise a list of worker threads that will execute the tasks
    PriorityQueue<Task> taskQueue;

    List<WorkerThread> workerThreads;

    int maxThreads;

    private boolean isShutDown = false;

    //create a lock object fro all sync operations
    Object lock = new Object();

    public void shutDown(){
        synchronized (taskQueue) {
            this.isShutDown = true;
            for(WorkerThread workerThread : workerThreads){
                workerThread.interrupt();
            }
            taskQueue.notifyAll();
        }
    }

    public InMemoryScheduler(int maxThreads){
        this.maxThreads = maxThreads;
        workerThreads = new ArrayList<>(maxThreads);
        taskQueue = new PriorityQueue<>(new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return (int) (o1.getTime() - o2.getTime());
            }
        });
    }

    public void start(){

        for(int i=0; i<maxThreads; i++){
            WorkerThread workerThread = new WorkerThread();
            workerThreads.add(workerThread);
            workerThread.start();
        }

    }


    public void scheduleTask(Runnable runnable, long time){
        synchronized (taskQueue){
            if(isShutDown){
                throw new IllegalStateException("Scheduler has been shut down");
            }

            Task task = new Task(runnable, time);
            taskQueue.offer(task);
            //will notify all the workers to resume and pick the task to execute it
            taskQueue.notifyAll();
        }
    }


    //recursive method that will keep on adding tasks into the queue
    public void scheduleTaskAtFixedInterval(Runnable runnable, int interval){
        synchronized (taskQueue) {
            if (isShutDown) {
                throw new IllegalStateException("Scheduler has been shut down");
            }

            Task task = new Task(() -> {
                try {
                    runnable.run();
                } finally {
                    synchronized (taskQueue) {
                        if (!isShutDown) {
                            long time = System.currentTimeMillis() + interval * 1000;
                            Task newTask = new Task(runnable, time);
                            taskQueue.offer(newTask);
                            taskQueue.notifyAll();
                        }
                    }
                }
            }, System.currentTimeMillis() + interval * 1000);
            /******************************************/
            taskQueue.offer(task);
            taskQueue.notifyAll();
        }
    }




    public class WorkerThread extends Thread{

        //This needs to execute the task from the taskqueue, so I will put it in the same class
        public void run(){
            //fetch the tasks from the taskQueue and execute their run method
            //check if the main thread hasn't stopped
            //also add checks if the taskQueue is not empty
            //similar to producer consumer problem, this is a consumer
            while(true){
                Task task;
                synchronized (taskQueue){
                    while(!isShutDown && taskQueue.isEmpty()){
                        try{
                            taskQueue.wait();
                        }catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    if(isShutDown){
                        break;
                    }

                    //main logic
                    task = taskQueue.peek();
                    if(task.getTime() > System.currentTimeMillis()){
                        try{
                            long waitTime = (task.getTime() - System.currentTimeMillis());
                            taskQueue.wait(waitTime);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }else{

                        taskQueue.poll();

                    }

                    //finally execute
                    try{
                        System.out.println("Worker thread "+ Thread.currentThread().getName()+" executing task "+ task.getTime());
                        task.run();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }

                }
            }

        }
    }


}
