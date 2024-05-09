

public class ReadyQueueElement
{
    public int processID;
    public int burstTime;
    public int waitingTime;

    public ReadyQueueElement(int processID, int burstTime, int waitingTime)
    {
        this.processID = processID;
        this.burstTime = burstTime;
        this.waitingTime = waitingTime;
    }
}
