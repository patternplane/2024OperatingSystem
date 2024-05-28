using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GUI_CSharp_WPF.Scheduler
{
    class ReadyQueueElement_New : ReadyQueueElement, IComparable<ReadyQueueElement_New> {

        public int remainingTime;
        public int arrivalTime;

        public ReadyQueueElement_New(int processID, int burstTime, int arrivalTime, int waitingTime) : base(processID, burstTime, waitingTime)
        {
            this.remainingTime = burstTime;
            this.arrivalTime = arrivalTime;
        }

        public int CompareTo(ReadyQueueElement_New o)
        {
            int result1 = this.remainingTime.CompareTo(o.remainingTime);
            if (result1 == 0)
                return this.arrivalTime.CompareTo(o.arrivalTime);
            else
                return result1;
        }
    }

    public class New : IScheduler
    {
        public List<DataType.Result> Run(List<DataType.Process> jobList, object parameter)
        {
            int timeQuantum;
            try
            {
                timeQuantum = (int)parameter;
            }
            catch
            {
                return null;
            }

            if (timeQuantum < 1)
                return null;

            List<DataType.Result> resultList = new List<DataType.Result>(jobList.Count);
            
            PriorityQueue<ReadyQueueElement_New> readyQueue = new PriorityQueue<ReadyQueueElement_New>();
            IEnumerator<DataType.Process> jobItr = jobList.GetEnumerator();
            jobItr.MoveNext();
            DataType.Process nextJob = jobItr.Current;

            int runTime = 0;
            int startTime = 0;
            int inRunningTime = 0;
            ReadyQueueElement_New currentProcess = null;
            while (true)
            {
                while (nextJob != null && nextJob.arriveTime <= runTime)
                {
                    readyQueue.Push(
                            new ReadyQueueElement_New(
                                    nextJob.processID,
                                    nextJob.burstTime,
                                    nextJob.arriveTime,
                                    runTime - nextJob.arriveTime));
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
                            || (!readyQueue.IsEmpty() && readyQueue.Peek().CompareTo(currentProcess) < 0))
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
                        int processingTime = Math.Min(currentProcess.remainingTime, (currentProcess.remainingTime <= timeQuantum * 2 ? currentProcess.remainingTime : timeQuantum));
                        foreach (ReadyQueueElement_New waitingProcess in readyQueue.GetItems())
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
