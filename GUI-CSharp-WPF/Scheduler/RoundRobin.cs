using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GUI_CSharp_WPF.Scheduler
{
    public class RoundRobin : IScheduler
    {
        public List<DataType.Result> Run(List<DataType.Process> jobList, object parameter)
        {
            int timeSlice = 0;
            List<DataType.Result> resultList = new List<DataType.Result>();
            try
            {
                timeSlice = (int)parameter;
            } 
            catch
            {
                return null;
            }

            int processingIdx = 0;
            int currentProcess = 0;
            int cpuTime = 0;
            int cpuDone = 0;

            int runTime = 0;
            ReadyQueueElement rq = null;
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
                        rq = readyQueue.ElementAt(0);
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
                        resultList.Add(new DataType.Result(rq.processID, runTime - cpuDone, cpuDone, rq.waitingTime));
                        continue;
                    }
                    if (cpuTime == timeSlice)
                    {
                        resultList.Add(new DataType.Result(rq.processID, runTime - cpuTime, cpuTime, rq.waitingTime));
                        readyQueue.Add(new ReadyQueueElement(rq.processID, cpuDone - cpuTime, rq.waitingTime));
                        rq = readyQueue.ElementAt(0);
                        cpuDone = rq.burstTime;
                        cpuTime = 0;
                        currentProcess = rq.processID;
                        readyQueue.RemoveAt(0);
                    }
                }
                cpuTime++;
                runTime++;

                foreach (ReadyQueueElement readyQueueElement in readyQueue)
                {
                    readyQueueElement.waitingTime++;
                }

            } while (jobList.Count != processingIdx || readyQueue.Count != 0 || currentProcess != 0);

            return resultList;
        }
    }
}
