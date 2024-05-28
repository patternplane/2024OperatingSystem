using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GUI_CSharp_WPF.Scheduler
{
    interface IScheduler
    {
        List<DataType.Result> Run(List<DataType.Process> jobList, object parameter);
    }
}
