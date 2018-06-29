//
// !!! Do NOT Change anything in this file
//
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Comparator;

public class cs310pa2
{
      static public class Point2D
      {
        Point2D(Double _x, Double _y){x=_x;y=_y;}
        public String toString()
        {
            return x+" "+y;
        }

        Double x, y;
      }

      @SuppressWarnings("unchecked")
      public static void main(String args[])
      {
        if(args.length==0)
        {
            System.err.println("Usage: cs310pa2 *.txt");
            return;
        }

        // Scanner scanData = new Scanner(System.in);
        // int numPoints = scanData.nextInt();
        // int count = 0;
        // double[] data = new double[numPoints*3];

        try
        {
          Scanner scanner = new Scanner(new File(args[0]));
          Point2D [] data=readPoints(scanner);

          //create the tree
          RangeTree2D<Point2D> rt = new RangeTree2D<Point2D>(
                                         new Comparator<Point2D>(){public int compare(Point2D a, Point2D b){ Double r=a.x-b.x; return (r>0)?1:((r<0)?-1:0);}},
                                         new Comparator<Point2D>(){public int compare(Point2D a, Point2D b){ Double r=a.y-b.y; return (r>0)?1:((r<0)?-1:0);}}
                                        );
          //read query
          while(scanner.hasNextDouble())
          {
            Double xMin = scanner.nextDouble();
            Double xMax = scanner.nextDouble();
            Double yMin = scanner.nextDouble();
            Double yMax = scanner.nextDouble();

            System.out.println("\nQuery: "+xMin+" "+xMax+" "+yMin+" "+yMax);

            //make a query
            IterableLinkedList<Point2D> result = rt.Search2D(new Point2D(xMin,yMin), new Point2D(xMax,yMax) );
            result.sort(new Comparator<Point2D>(){public int compare(Point2D a, Point2D b){
              Double r=a.x-b.x;
              if(r==0) r=a.y-b.y;
              return (r>0)?1:((r<0)?-1:0);
            }});

            //print results
            System.out.print("Found "+result.size()+" point");
            if(result.size()>1) System.out.print("s");
            System.out.print("\n");

            for(Point2D v : result)
            {
              System.out.println(v);
            }
          }//end while

          //done
          System.out.println("\nQuery:  exit\nbye");
        }
        catch (FileNotFoundException e) {
          e.printStackTrace();
        }
      }//end main

      //read a polygon from file
      private static Point2D [] readPoints(Scanner scan)
      {
        try {
            int size;
            if(scan.hasNextInt()) size=scan.nextInt(); else throw new IOException("size is not given");
            Point2D [] data=new Point2D[size];

            int id=0;
            while(scan.hasNextDouble())
            {
              double x, y;
              x=scan.nextDouble();
              if(scan.hasNextDouble()) y=scan.nextDouble();
              else throw new IOException("Y value is not given");
              data[id++]=new Point2D(x,y);
            }

            return data;
        }
        catch (IOException e){
          e.printStackTrace();
        }
        return null;
      }
}
