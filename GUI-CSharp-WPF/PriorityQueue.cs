using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GUI_CSharp_WPF
{
    public class PriorityQueue<T> where T : IComparable<T>
    {
        List<T> dataArray = new List<T>(100);
        int n = 0;

        public PriorityQueue()
        {
            dataArray.Add(default(T));
        }

        public void Push(T item)
        {
            dataArray.Add(item);
            n++;
            UpwardRestructure();
        }

        public bool IsEmpty()
        {
            return n == 0;
        }

        public T Peek()
        {
            if (n == 0)
                throw new Exception("PriorityQueue Empty");

            return dataArray[1];
        }

        public T Poll()
        {
            if (n == 0)
                return default(T);

            T result = dataArray[1];
            n--;

            if (n == 0)
            {
                dataArray.RemoveAt(1);
                return result;
            }
            else
            {
                dataArray[1] = dataArray.ElementAt(n + 1);
                dataArray.RemoveAt(n + 1);
                DownwardRestructure(1);
                return result;
            }
        }

        private void UpwardRestructure()
        {
            T temp;
            int currentIdx = n;
            int parentIdx;
            int selectedChild;

            while (currentIdx > 1)
            {
                parentIdx = currentIdx / 2;

                if (parentIdx * 2 + 1 > n
                    || dataArray[parentIdx * 2].CompareTo(dataArray[parentIdx * 2 + 1]) < 0)
                    selectedChild = parentIdx * 2;
                else
                    selectedChild = parentIdx * 2 + 1;

                if (dataArray[parentIdx].CompareTo(dataArray[selectedChild]) <= 0)
                    break;

                temp = dataArray[selectedChild];
                dataArray[selectedChild] = dataArray[parentIdx];
                dataArray[parentIdx] = temp;

                currentIdx = parentIdx;
            }
        }

        private void DownwardRestructure(int startItemIdx)
        {
            T temp;
            int prioriChildIdx = 0;

            while (true)
            {
                if (startItemIdx * 2 + 1 <= n)
                {
                    if (dataArray[startItemIdx * 2].CompareTo(dataArray[startItemIdx * 2 + 1]) < 0)
                        prioriChildIdx = startItemIdx * 2;
                    else
                        prioriChildIdx = startItemIdx * 2 + 1;
                }
                else if (startItemIdx * 2 == n)
                    prioriChildIdx = startItemIdx * 2;
                else
                    break;

                if (dataArray[startItemIdx].CompareTo(dataArray[prioriChildIdx]) <= 0)
                    break;

                temp = dataArray[startItemIdx];
                dataArray[startItemIdx] = dataArray[prioriChildIdx];
                dataArray[prioriChildIdx] = temp;

                startItemIdx = prioriChildIdx;
            }
        }

        public T[] GetItems()
        {
            T[] result = new T[n];
            dataArray.CopyTo(1,result,0,n);
            return result;
        }
    }
}
