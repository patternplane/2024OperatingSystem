using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GUI_CSharp_WPF
{
    public class FileReader
    {
        const int dataTokenLen = 5;
        const string dataHead = "process";

        public List<DataType.Process> readFile(string filePath)
        {
            List<DataType.Process> result = new List<DataType.Process>();

            FileStream f = null;
            StreamReader s = null;
            try
            {
                f = new FileStream(filePath, FileMode.Open);
                s = new StreamReader(f);

                while (!s.EndOfStream)
                {
                    string[] data = s.ReadLine().Split(' ');

                    try
                    {
                        if (data.Length == dataTokenLen && data[0].CompareTo(dataHead) == 0)
                            result.Add(
                                new DataType.Process(
                                    int.Parse(data[1]),
                                    int.Parse(data[2]),
                                    int.Parse(data[3]),
                                    int.Parse(data[4])));
                    } catch {}
                }
            } catch {}

            s.Close();
            f.Close();
            return result;
        }
    }
}
