import java.util.Scanner;

public class producer_consumer {

    public static int s = 1, full = 0, empty = 10;

    // Semaphore wait function
    public static void waitS() {
        s--;
    }

    public static void signalS() {
        s++;
    }

    public static void waitFull() {
        full--;
    }

    public static void signalFull() {
        full++;
    }

    public static void waitEmpty() {
        empty--;
    }

    public static void signalEmpty() {
        empty++;
    }

    public static void producer() {
        if (s == 1 && empty > 0) {
            waitS();
            waitEmpty();

            full++;
            System.out.println("Produced an item.");
            display();

            signalS();
        } else {
            System.out.println("Cannot produce: Buffer is full.");
        }
    }

    public static void consumer() {
        if (s == 1 && full > 0) {
            waitS();
            waitFull();

            empty++;
            System.out.println("Consumed an item.");
            display();

            signalS();
        } else {
            System.out.println("Cannot consume: Buffer is empty.");
        }
    }

    public static void display() {
        System.out.println("Status: s=" + s + " | empty=" + empty + " | full=" + full);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n========= Producer-Consumer Menu ========");
            System.out.println("1. Produce");
            System.out.println("2. Consume");
            System.out.println("3. Display Status");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    producer();
                    break;
                case 2:
                    consumer();
                    break;
                case 3:
                    display();
                    break;
                case 4:
                    System.out.println("Exiting simulation.");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (choice != 4);

        sc.close();
    }
}