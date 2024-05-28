using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GUI_CSharp_WPF.Scheduler
{
    class ReadyQueueElement_Priority : ReadyQueueElement, IComparable<ReadyQueueElement_Priority> {

        public int priority;
        public int arrivalTime;
        public int remainingTime;

        public ReadyQueueElement_Priority(int processID, int burstTime, int priority, int arrivalTime) : base(processID, burstTime, 0)
        {
            this.priority = priority;
            this.arrivalTime = arrivalTime;
            this.remainingTime = burstTime;
        }

        public int CompareTo(ReadyQueueElement_Priority o)
        {
            int result1 = o.priority.CompareTo(this.priority);
            if (result1 == 0)
                return this.arrivalTime.CompareTo(o.arrivalTime);
            return result1;
        }
    }

    public class Priority : IScheduler
    {
        public List<DataType.Result> Run(List<DataType.Process> jobList, object parameter)
        {
            bool isPreemptive = false;
            List<DataType.Result> resultList = new List<DataType.Result>(jobList.Count);
            try
            {
                isPreemptive = (bool)parameter;
            }
            catch
            {
                return null;
            }

            PriorityQueue<ReadyQueueElement_Priority> readyQueue = new PriorityQueue<ReadyQueueElement_Priority>();
            IEnumerator<DataType.Process> jobItr = jobList.GetEnumerator();
            jobItr.MoveNext();
            DataType.Process nextJob = jobItr.Current;

            int runTime = 0;
            int startTime = 0;
            int inRunningTime = 0;
            ReadyQueueElement_Priority currentProcess = null;
            while (true)
            {
                while (nextJob != null && runTime == nextJob.arriveTime)
                {
                    readyQueue.Push(
                            new ReadyQueueElement_Priority(
                                    nextJob.processID,
                                    nextJob.burstTime,
                                    nextJob.priority,
                                    nextJob.arriveTime));
                    nextJob = (jobItr.MoveNext() ? jobItr.Current : null);
                }
                if (readyQueue.IsEmpty() && currentProcess == null)
                {
                    if (nextJob == null)
                        break;
                    else
                        runTime = nextJob.arriveTime;
                }
                else
                {
                    if (currentProcess == null
                            || currentProcess.remainingTime == 0
                            || (isPreemptive && !readyQueue.IsEmpty() && currentProcess.priority < readyQueue.Peek().priority))
                    {
                        if (currentProcess != null)
                        {
                            resultList.Add(
                                    new DataType.Result(
                                            currentProcess.processID,
                                            startTime,
                                            inRunningTime,
                                            currentProcess.waitingTime));
                            if (currentProcess.remainingTime != 0)
                            {
                                currentProcess.waitingTime = 0;
                                readyQueue.Push(currentProcess);
                            }
                        }
                        inRunningTime = 0;
                        startTime = runTime;
                        currentProcess = readyQueue.Poll();
                    }
                    else
                    {
                        int processingTime =
                                (nextJob != null
                                ? Math.Min(currentProcess.remainingTime, nextJob.arriveTime - runTime)
                                : currentProcess.remainingTime);
                        foreach (ReadyQueueElement_Priority waitingProcess in readyQueue.GetItems())
                            waitingProcess.waitingTime += processingTime;
                        currentProcess.remainingTime -= processingTime;
                        runTime += processingTime;
                        inRunningTime += processingTime;
                    }
                }
            }

            return resultList;
        }
    }
}
