// Java program to implement solution of producer-consumer problem.
import java.util.LinkedList;

public class Geeks {
    public static void main(String[] args)
        throws InterruptedException
    {
        // Object of a class that has both produce()
        // and consume() methods
        final PC pc = new PC();
        
        // Create producer thread
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    pc.produce();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        // Create consumer thread
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try {
                    pc.consume();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        
        // Start both threads
        t1.start();
        t2.start();
        
        // t1 finishes before t2
        t1.join();
        t2.join();
    }
    
    // This class has a list, producer (adds items to list)
    // and consumer (removes items).
    public static class PC {
        // Create a list shared by producer and consumer
        // Size of list is 2.
        LinkedList<Integer> list = new LinkedList<>();
        int capacity = 2;
        
        // Function called by producer thread
        public void produce() throws InterruptedException
        {
            int value = 0;
            while (true) {
                synchronized (this)
                {
                    // producer thread waits while list is full
                    if (list.size() == capacity) {
                        System.out.println("List is full, producer is waiting...");
                        // Signal any waiting consumer before waiting
                        notify();
                        wait();
                    }
                    
                    // to insert the jobs in the list
                    list.add(value);
                    System.out.println("Producer produced-" + value);
                    value++;
                    
                    // notifies the consumer thread that now it can start consuming
                    notify();
                    
                    // makes the working of program easier to understand
                    Thread.sleep(1000);
                }
            }
        }
        
        // Function called by consumer thread
        public void consume() throws InterruptedException
        {
            while (true) {
                synchronized (this)
                {
                    // consumer thread waits while list is empty
                    if (list.size() == 0) {
                        System.out.println("List is empty, consumer is waiting...");
                        // Signal any waiting producer before waiting
                        notify();
                        wait();
                    }
                    
                    // to retrieve the first job in the list
                    int val = list.removeFirst();
                    System.out.println("Consumer consumed-" + val);
                    
                    // Wake up producer thread
                    notify();
                    
                    // and sleep
                    Thread.sleep(1000);
                }
            }
        }
    }
}