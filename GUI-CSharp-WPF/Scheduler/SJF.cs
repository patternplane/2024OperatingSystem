using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GUI_CSharp_WPF.Scheduler
{
    public class SJF : IScheduler
    {
        public List<DataType.Result> Run(List<DataType.Process> jobList, object parameter)
        {
            List<DataType.Result> resultList = new List<DataType.Result>();
            int processingIdx = 0;
            int currentProcess = 0;
            int cpuTime = 0;
            int cpuDone = 0;
            int runTime = 0;

            List<ReadyQueueElement> readyQueue = new List<ReadyQueueElement>();

            do
            {
            while (jobList.Count != processingIdx)
            {
                DataType.Process frontJob = jobList.ElementAt(processingIdx);
                if (frontJob.arriveTime == runTime)
                {
                    readyQueue.Add(new ReadyQueueElement(frontJob.processID, frontJob.burstTime, 0));
                    processingIdx++;
                    readyQueue.Sort(
                        (o1, o2) =>
                        {
                            if (o1.burstTime > o2.burstTime) return 1;
                            else if (o1.burstTime < o2.burstTime) return -1;
                            else return o1.processID.CompareTo(o2.processID);
                        });
                }
                else
                    break;
            }

                if (currentProcess == 0)
                {
                    if (!(readyQueue.Count == 0))
                    {
                        ReadyQueueElement rq = readyQueue.First();
                        resultList.Add(new DataType.Result(rq.processID, runTime, rq.burstTime, rq.waitingTime));
                        cpuDone = rq.burstTime;
                        cpuTime = 0;
                        currentProcess = rq.processID;
                        readyQueue.RemoveAt(0);
                    }
                }
                else
                {
                    if (cpuTime == cpuDone)
                    {
                        currentProcess = 0;
                        continue;
                    }
                }
                cpuTime++;
                runTime++;

                foreach (ReadyQueueElement readyQueueElement in readyQueue)
                    readyQueueElement.waitingTime++;

            } while (jobList.Count != processingIdx || readyQueue.Count != 0 || currentProcess != 0);

            return resultList;
        }
    }
}
