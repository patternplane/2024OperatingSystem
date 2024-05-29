using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Media;

namespace GUI_CSharp_WPF
{
    class Drawer : FrameworkElement
    {
        private VisualCollection _children;

        public Drawer()
        {
            _children = new VisualCollection(this);
        }

        const double InboxTextOffset = 2.0;
        const double OutboxTextOffset = 5.0;
        const double TimeSize = 10.0;
        const double FrontMargin = 15.0;

        Pen recPen = new Pen(Brushes.Red, 1);

        private void SetText(DrawingContext dc, string str, double x, double y)
        {
            dc.DrawText(
               new FormattedText(str,
                  System.Globalization.CultureInfo.GetCultureInfo("en-us"),
                  FlowDirection.LeftToRight,
                  new Typeface("Verdana"),
                  15.0, Brushes.Black, 50),
               new Point(x, y));
        }

        private void SetRec(DrawingContext dc, double width, double x)
        {
            dc.DrawRectangle(
                null, 
                recPen,
                new Rect(
                    new Point(x, 20), 
                    new Size(width, 60)));
        }

        public void Clear()
        {
            _children.Clear();
        }

        public double DrawGanttChart(List<DataType.Result> results)
        {
            _children.Clear();

            DrawingVisual drawingVisual = new DrawingVisual();
            DrawingContext drawingContext = drawingVisual.RenderOpen();

            double totalLen = 0.0;
            foreach (DataType.Result r in results)
            {
                SetText(drawingContext, "P" + r.processID, FrontMargin + r.startP * TimeSize + InboxTextOffset, 40);
                SetText(drawingContext, r.startP.ToString() , FrontMargin + r.startP * TimeSize - OutboxTextOffset, 80);
                SetRec(drawingContext, r.burstTime * TimeSize, FrontMargin + r.startP * TimeSize);
                totalLen = r.burstTime * TimeSize + FrontMargin + r.startP * TimeSize;
            }

            drawingContext.Close();

            _children.Add(drawingVisual);

            return totalLen + FrontMargin;
        }

        protected override int VisualChildrenCount => _children.Count;

        protected override Visual GetVisualChild(int index)
        {
            if (index < 0 || index >= _children.Count)
            {
                throw new ArgumentOutOfRangeException();
            }

            return _children[index];
        }
    }
}
