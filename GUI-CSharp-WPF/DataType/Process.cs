using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GUI_CSharp_WPF.DataType
{
    public class Process
    {
        public int processID { get; set; }
        public int arriveTime { get; set; }
        public int burstTime { get; set; }
        public int priority { get; set; }

        public Process(int processID, int arriveTime, int burstTime, int priority)
        {
            this.processID = processID;
            this.arriveTime = arriveTime;
            this.burstTime = burstTime;
            this.priority = priority;
        }
    }
}
