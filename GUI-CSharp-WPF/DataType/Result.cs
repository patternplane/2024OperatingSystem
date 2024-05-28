using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GUI_CSharp_WPF.DataType
{
    public class Result
    {
        public int processID { get; set; }
        public int startP { get; set; }
        public int burstTime { get; set; }
        public int waitingTime { get; set; }

        public Result(int processID, int startP, int burstTime, int waitingTime)
        {
            this.processID = processID;
            this.startP = startP;
            this.burstTime = burstTime;
            this.waitingTime = waitingTime;
        }
    }
}
