using Microsoft.Win32;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace GUI_CSharp_WPF
{
    /// <summary>
    /// MainWindow.xaml에 대한 상호 작용 논리
    /// </summary>
    public partial class MainWindow : Window, INotifyPropertyChanged
    {
        Drawer dr;
        FileReader fileReader;
        OpenFileDialog fd;
        Scheduler.IScheduler[] schedulers;

        /* ==========================
         *       Binding Data 
         * ========================== */

        AlgorithmType currentType = AlgorithmType.FCFS;
        public string SubInfoText { get { return SubInfoSelector(currentType); } }
        public string ParameterText { get; set; }
        public string AverageResultText { get; set; }
        public ObservableCollection<DataType.Process> InputJobs { get; } = new ObservableCollection<DataType.Process>();
        public ObservableCollection<DataType.Result> ResultTable { get; } = new ObservableCollection<DataType.Result>();
        public ObservableCollection<DataType.Result> ProcessResultTable { get; } = new ObservableCollection<DataType.Result>(); 

        private string SubInfoSelector(AlgorithmType type)
        {
            switch (type)
            {
                case AlgorithmType.Priority:
                    return "스케쥴링 파라미터 (is Preemptive? <true / false>)";
                case AlgorithmType.New:
                case AlgorithmType.RoundRobin:
                    return "스케쥴링 파라미터 (Time Quantum <number>)";
                case AlgorithmType.FCFS:
                case AlgorithmType.SJF:
                    return "스케쥴링 파라미터 (불필요)";
                default:
                    return "스케쥴링 파라미터";
            }
        }

        private void EH_AlgorithmSelectionChanged(object sender, RoutedEventArgs e)
        {
            int algorithmNumber = int.Parse(((RadioButton)sender).Tag.ToString());
            currentType = (AlgorithmType)(algorithmNumber - 1);
            NotifyPropertyChanged("SubInfoText");
        }

        // UI Updator
        public event PropertyChangedEventHandler PropertyChanged;
        private void NotifyPropertyChanged([CallerMemberName] String propertyName = "")
        {
            PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(propertyName));
        }

        /* ==========================
         *    Constructor & Initial
         * ========================== */

        public MainWindow()
        {
            InitializeComponent();
            this.DataContext = this;

            // Gantt Chart Drawer
            dr = new Drawer();
            DrawingCanvas.Children.Add(dr);

            fileReader = new FileReader();

            // File Open Dialog
            fd = new OpenFileDialog();
            fd.Filter = "Process List (*.txt)|*.txt";

            // Assign Schedulers
            schedulers = new Scheduler.IScheduler[5];
            for (int i = 0; i < 5; i++)
                schedulers[i] = GetScheduler((AlgorithmType)i);
        }

        private Scheduler.IScheduler GetScheduler(AlgorithmType type)
        {
            switch (type)
            {
                case AlgorithmType.FCFS:
                    return new Scheduler.FCFS();
                case AlgorithmType.New:
                    return new Scheduler.New();
                case AlgorithmType.Priority:
                    return new Scheduler.Priority();
                case AlgorithmType.RoundRobin:
                    return new Scheduler.RoundRobin();
                case AlgorithmType.SJF:
                    return new Scheduler.SJF();
                default:
                    return null;
            }
        } 

        /* ==========================
         *         Open File 
         * ========================== */

        private void EH_AddButtonClick(object sender, RoutedEventArgs e)
        {
            if (true == fd.ShowDialog())
            {
                InputJobs.Clear();
                ResultTable.Clear();
                ProcessResultTable.Clear();

                foreach (DataType.Process p in fileReader.readFile(fd.FileName))
                    InputJobs.Add(p);

                NotifyPropertyChanged("InputJobs");
            }
        }

        /* ==========================
         *        Do Simulate
         * ========================== */

        private void EH_StartButtonClick(object sender, RoutedEventArgs e)
        {
            CheckAndSimulate();
        }

        private void CheckAndSimulate()
        {
            if (InputJobs.Count == 0)
                return;
            object parameter;
            if (!ParameterConverter(out parameter))
            {
                ParameterText = "=== 잘못된 파라미터가 입력됨 ===";
                NotifyPropertyChanged("ParameterText");
                return;
            }

            long TotalWaiting = 0;
            long WorkEndTime = 0;
            long TotalProcessing = 0;

            // Run Scheduling
            List<DataType.Process> jobs = new List<DataType.Process>(InputJobs);
            jobs.Sort((a, b) => a.arriveTime.CompareTo(b.arriveTime));
            List<DataType.Result> results = schedulers[(int)currentType].Run(jobs,parameter);

            // Set Results
            ResultTable.Clear();
            foreach (DataType.Result r in results)
            {
                ResultTable.Add(r);
                TotalWaiting += r.waitingTime;
                WorkEndTime = r.startP + r.burstTime;
                TotalProcessing += r.burstTime;
            }

            // Draw Gantt Chart 
            DrawingCanvas.Width = dr.DrawGanttChart(results);

            // Make Process's Result
            results.Sort((a, b)=>a.processID.CompareTo(b.processID));
            List<DataType.Result> processResults = new List<DataType.Result>(jobs.Count);
            int pid = -500;
            foreach (DataType.Result r in results)
            {
                if (pid != r.processID)
                {
                    processResults.Add(new DataType.Result(r.processID, -1, r.burstTime, r.waitingTime));
                    pid = r.processID;
                }
                else
                {
                    processResults.Last().burstTime += r.burstTime;
                    processResults.Last().waitingTime += r.waitingTime;
                }
            }

            // Set Process's Result
            ProcessResultTable.Clear();
            foreach (DataType.Result r in processResults)
                ProcessResultTable.Add(r);

            // Set Average Times
            AverageResultText =
                "총 작업시간 : " + WorkEndTime
                + "     CPU 사용시간 : " + TotalProcessing
                + "     평균 대기시간 : " + (TotalWaiting / (double)jobs.Count)
                + "     평균 처리시간 : " + ((TotalWaiting + TotalProcessing) / (double)jobs.Count);
            NotifyPropertyChanged("AverageResultText");
        }

        private bool ParameterConverter(out object parameter)
        {
            if (currentType == AlgorithmType.RoundRobin
                || currentType == AlgorithmType.New)
            {
                try
                {
                    int value = int.Parse(ParameterText);
                    if (value > 0)
                    {
                        parameter = value;
                        return true;
                    }
                }
                catch { }
            }
            else if (currentType == AlgorithmType.Priority)
            {
                try
                {
                    parameter = bool.Parse(ParameterText);
                    return true;
                } catch {}
            }
            else
            {
                parameter = null;
                return true;
            }
            parameter = null;
            return false;
        }
    }
}
