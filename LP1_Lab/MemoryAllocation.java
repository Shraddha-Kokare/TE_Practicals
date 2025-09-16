public class MemoryAllocation {

    static int[] processes = {212, 417, 112, 426};
    static int[] partitions = {100, 500, 200, 300, 600};

    public static void main(String[] args) {
        System.out.println("First Fit Allocation:\n");
        firstFit();
        System.out.println("\n-------------------------\n");

        System.out.println("Next Fit Allocation:\n");
        nextFit();
        System.out.println("\n-------------------------\n");

        System.out.println("Best Fit Allocation:\n");
        bestFit();
        System.out.println("\n-------------------------\n");

        System.out.println("Worst Fit Allocation:\n");
        worstFit();
    }

    private static void printPartitions(int[] partitions, String[] allocation) {
        System.out.println("before allocation :\n");
        for (int i = 0; i < partitions.length; i++) {
            System.out.println("Partition " + i + ": " + partitions[i] + " kB");
        }
        System.out.println("\nafter allocation:\n");
        for (int i = 0; i < partitions.length; i++) {
            if (allocation[i] != null) {
                System.out.println("Partition " + i + ": " + partitions[i] + " kB: " + allocation[i]);
            } else {
                System.out.println("Partition " + i + ": " + partitions[i] + " kB");
            }
        }
    }

    // private static void firstFit() {
    //     int[] partitionSizes = partitions.clone();
    //     String[] allocation = new String[partitionSizes.length];

    //     for (int i = 0; i < processes.length; i++) {
    //         boolean allocated = false;
    //         for (int j = 0; j < partitionSizes.length; j++) {
    //             if (partitionSizes[j] >= processes[i]) {
    //                 allocation[j] = "Process " + i + " of size " + processes[i] + "KB allocated";
    //                 partitionSizes[j] -= processes[i];
    //                 allocated = true;
    //                 break;
    //             }
    //         }
    //         if (!allocated) {
    //             System.out.println("Process " + i + " of size " + processes[i] + "KB cannot be allocated.");
    //         }
    //     }
    //     printPartitions(partitions, allocation);
    // }

    private static void nextFit()
    {
        int partition=partitions.clone()
        
    }

    private static void firstFit() {
    int[] partitionSizes = partitions.clone();
    int[] allocation = new int[partitionSizes.length];
    for (int i = 0; i < allocation.length; i++) allocation[i] = -1;  // -1 means unallocated

    boolean[] processAllocated = new boolean[processes.length];

    for (int i = 0; i < processes.length; i++) {
        for (int j = 0; j < partitionSizes.length; j++) {
            if (partitionSizes[j] >= processes[i] && allocation[j] == -1) { // partition free and fits process
                allocation[j] = i;
                partitionSizes[j] -= processes[i];
                processAllocated[i] = true;
                break;
            }
        }
    }

    // Print before allocation
    System.out.println("before allocation :\n");
    for (int i = 0; i < partitions.length; i++) {
        System.out.println("Partition " + i + ": " + partitions[i] + " kB");
    }

    // Print after allocation
    System.out.println("\nafter allocation:\n");
    for (int i = 0; i < partitions.length; i++) {
        if (allocation[i] != -1) {
            int pIndex = allocation[i];
            System.out.println("Partition " + i + ": " + partitions[i] + " kB: Process " + pIndex + " of size " + processes[pIndex] + "KB allocated");
        } else {
            System.out.println("Partition " + i + ": " + partitions[i] + " kB");
        }
    }

    // Print unallocated processes
    for (int i = 0; i < processes.length; i++) {
        if (!processAllocated[i]) {
            System.out.println("\nProcess " + i + " of size " + processes[i] + "KB cannot be allocated.");
        }
    }
}


    private static void nextFit() {
        int[] partitionSizes = partitions.clone();
        String[] allocation = new String[partitionSizes.length];
        int lastAllocatedIndex = 0;

        for (int i = 0; i < processes.length; i++) {
            boolean allocated = false;
            int j = lastAllocatedIndex;
            int count = 0;
            while (count < partitionSizes.length) {
                if (partitionSizes[j] >= processes[i]) {
                    allocation[j] = "Process " + i + " of size " + processes[i] + "KB allocated";
                    partitionSizes[j] -= processes[i];
                    allocated = true;
                    lastAllocatedIndex = j;
                    break;
                }
                j = (j + 1) % partitionSizes.length;
                count++;
            }
            if (!allocated) {
                System.out.println("Process " + i + " of size " + processes[i] + "KB cannot be allocated.");
            }
        }
        printPartitions(partitions, allocation);
    }

    private static void bestFit() {
        int[] partitionSizes = partitions.clone();
        String[] allocation = new String[partitionSizes.length];

        for (int i = 0; i < processes.length; i++) {
            int bestIndex = -1;
            for (int j = 0; j < partitionSizes.length; j++) {
                if (partitionSizes[j] >= processes[i]) {
                    if (bestIndex == -1 || partitionSizes[j] < partitionSizes[bestIndex]) {
                        bestIndex = j;
                    }
                }
            }
            if (bestIndex != -1) {
                allocation[bestIndex] = "Process " + i + " of size " + processes[i] + "KB allocated";
                partitionSizes[bestIndex] -= processes[i];
            } else {
                System.out.println("Process " + i + " of size " + processes[i] + "KB cannot be allocated.");
            }
        }
        printPartitions(partitions, allocation);
    }

    private static void worstFit() {
        int[] partitionSizes = partitions.clone();
        String[] allocation = new String[partitionSizes.length];

        for (int i = 0; i < processes.length; i++) {
            int worstIndex = -1;
            for (int j = 0; j < partitionSizes.length; j++) {
                if (partitionSizes[j] >= processes[i]) {
                    if (worstIndex == -1 || partitionSizes[j] > partitionSizes[worstIndex]) {
                        worstIndex = j;
                    }
                }
            }
            if (worstIndex != -1) {
                allocation[worstIndex] = "Process " + i + " of size " + processes[i] + "KB allocated";
                partitionSizes[worstIndex] -= processes[i];
            } else {
                System.out.println("Process " + i + " of size " + processes[i] + "KB cannot be allocated.");
            }
        }
        printPartitions(partitions, allocation);
    }
}
