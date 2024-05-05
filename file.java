class Node {
    Process data;
    Node next;

    public Node(Process data) {
        this.data = data;
        this.next = null;
    }
}

class Process {
    int id;
    int arrivalTime;
    int burstTime;
    int priority;
    int startTime;
    int completionTime;

    public Process(int id, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.startTime = -1; // Initialize to -1 to indicate not started
        this.completionTime = -1; // Initialize to -1 to indicate not completed
    }
}

public class PriorityQueue {
    Node front;
    int currentTime;

    public PriorityQueue() {
        front = null;
        currentTime = 0;
    }

    public void enqueue(Process process) {
        Node newNode = new Node(process);

        // If queue is empty or new node has higher priority
        if (front == null || process.priority < front.data.priority) {
            newNode.next = front;
            front = newNode;
        } else {
            Node temp = front;
            while (temp.next != null && temp.next.data.priority <= process.priority) {
                temp = temp.next;
            }
            newNode.next = temp.next;
            temp.next = newNode;
        }
    }

    public Process dequeue() {
        if (front == null) {
            throw new IllegalStateException("Queue is empty");
        }

        Process process = front.data;
        front = front.next;
        return process;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public void schedule() {
        // Process the elements in the priority queue
        while (!isEmpty()) {
            Process process = dequeue();
            process.startTime = currentTime;
            currentTime += process.burstTime;
            process.completionTime = currentTime;
        }
    }

    public float calculateTurnaroundTime(Process process) {
        return process.completionTime - process.arrivalTime;
    }

    public float calculateWaitingTime(Process process) {
        return calculateTurnaroundTime(process) - process.burstTime;
    }

    public float calculateResponseTime(Process process) {
        return process.startTime - process.arrivalTime;
    }

    public float calculateAverageTurnaroundTime() {
        float totalTurnaroundTime = 0;
        int numProcesses = 0;

        Node temp = front;
        while (temp != null) {
            Process process = temp.data;
            totalTurnaroundTime += calculateTurnaroundTime(process);
            numProcesses++;
            temp = temp.next;
        }

        return totalTurnaroundTime / numProcesses;
    }

    public float calculateAverageWaitingTime() {
        float totalWaitingTime = 0;
        int numProcesses = 0;

        Node temp = front;
        while (temp != null) {
            Process process = temp.data;
            totalWaitingTime += calculateWaitingTime(process);
            numProcesses++;
            temp = temp.next;
        }

        return totalWaitingTime / numProcesses;
    }

    public float calculateAverageResponseTime() {
        float totalResponseTime = 0;
        int numProcesses = 0;

        Node temp = front;
        while (temp != null) {
            Process process = temp.data;
            totalResponseTime += calculateResponseTime(process);
            numProcesses++;
            temp = temp.next;
        }

        return totalResponseTime / numProcesses;
    }

    public static void main(String[] args) {
        PriorityQueue pq = new PriorityQueue();

        // Adding processes with arrival time, burst time, and priority
        pq.enqueue(new Process(1, 0, 5, 2));
        pq.enqueue(new Process(2, 1, 3, 1));
        pq.enqueue(new Process(3, 2, 6, 3));

        // Schedule the processes
        pq.schedule();

        // Calculate and print turnaround time, waiting time, and response time for each process
        System.out.println("Process\tTurnaround Time\tWaiting Time\tResponse Time");
        Node temp = pq.front;
        while (temp != null) {
            Process process = temp.data;
            System.out.println(process.id + "\t\t" + pq.calculateTurnaroundTime(process) + "\t\t\t" +
                    pq.calculateWaitingTime(process) + "\t\t\t" + pq.calculateResponseTime(process));
            temp = temp.next;
        }

        // Calculate and print average metrics
        float avgTurnaroundTime = pq.calculateAverageTurnaroundTime();
        float avgWaitingTime = pq.calculateAverageWaitingTime();
        float avgResponseTime = pq.calculateAverageResponseTime();

        System.out.println("\nAverage Turnaround Time: " + avgTurnaroundTime);
        System.out.println("Average Waiting Time: " + avgWaitingTime);
        System.out.println("Average Response Time: " + avgResponseTime);
    }
}
