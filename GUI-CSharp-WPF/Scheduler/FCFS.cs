using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GUI_CSharp_WPF.Scheduler
{ 
    public class FCFS : IScheduler
    {
        public List<DataType.Result> Run(List<DataType.Process> jobList, object parameter)
        {
            int currentProcess = 0;
            int cpuTime = 0;
            int cpuDone = 0;

            int runTime = 0;

            List<DataType.Result> resultList = new List<DataType.Result>();

            Queue<ReadyQueueElement> readyQueue = new Queue<ReadyQueueElement>();
            int unProcessedIdx = 0;
            do
            {
                for (; unProcessedIdx < jobList.Count; unProcessedIdx++)
                {
                    DataType.Process frontJob = jobList.ElementAt(unProcessedIdx);
                    if (frontJob.arriveTime == runTime)
                    {
                        readyQueue.Enqueue(new ReadyQueueElement(frontJob.processID, frontJob.burstTime, 0));
                    }
                    else
                    {
                        break;
                    }
                }

                if (currentProcess == 0)
                {
                    if (readyQueue.Count != 0)
                    {
                        ReadyQueueElement rq = readyQueue.Dequeue();
                        resultList.Add(new DataType.Result(rq.processID, runTime, rq.burstTime, rq.waitingTime));
                        cpuDone = rq.burstTime;
                        cpuTime = 0;
                        currentProcess = rq.processID;
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

                foreach (ReadyQueueElement rm in readyQueue)
                {
                    rm.waitingTime++;
                }

            } while (unProcessedIdx < jobList.Count || readyQueue.Count != 0 || currentProcess != 0);

            return resultList;
        }
    }
}
