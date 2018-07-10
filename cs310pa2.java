//
// !!! Do NOT Change anything in this file
//
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
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
        String filename=null;
        boolean savesvg=false;
        for(String arg: args)
        {
            if(arg.compareTo("-svg")==0) savesvg=true;
            filename=arg;
        }

        if(filename==null)
        {
            System.err.println("Usage: [-svg] cs310pa2 *.txt");
            return;
        }

        // Scanner scanData = new Scanner(System.in);
        // int numPoints = scanData.nextInt();
        // int count = 0;
        // double[] data = new double[numPoints*3];

        try
        {
          Scanner scanner = new Scanner(new File(filename));
          Point2D [] data=readPoints(scanner);
          System.out.println("Read in "+data.length+" points");

          //create the tree
          RangeTree2D<Point2D> rt = new RangeTree2D<Point2D>(
                                         new Comparator<Point2D>(){public int compare(Point2D a, Point2D b){ Double r=a.x-b.x; return (r>0)?1:((r<0)?-1:0);}},
                                         new Comparator<Point2D>(){public int compare(Point2D a, Point2D b){ Double r=a.y-b.y; return (r>0)?1:((r<0)?-1:0);}}
                                        );

          rt.build(data);

          //read query
          int query_count=0;
          while(scanner.hasNextDouble())
          {
            Double xMin = scanner.nextDouble();
            Double xMax = scanner.nextDouble();
            Double yMin = scanner.nextDouble();
            Double yMax = scanner.nextDouble();

            System.out.println("Query: "+xMin+" "+xMax+" "+yMin+" "+yMax);

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
            System.out.println("\n");

            for(Point2D v : result)
            {
              System.out.println(v);
            }

            //save to svg
            if(savesvg)
            {
              String svgfilename="result_"+query_count+".svg";
              saveSVG(svgfilename,new Point2D(xMin,yMin), new Point2D(xMax,yMax),data,result);
              query_count++;
            }

          }//end while

          //done
          System.out.println("Query:  exit\nbye");
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

            for(int id=0;id<size;id++)
            {
              double x, y;
              if(scan.hasNextDouble()) x=scan.nextDouble();
              else throw new IOException("X value is not given");

              if(scan.hasNextDouble()) y=scan.nextDouble();
              else throw new IOException("Y value is not given");
              data[id]=new Point2D(x,y);
            }

            return data;
        }
        catch (IOException e){
          e.printStackTrace();
        }
        return null;
      }


      static public void saveSVG(String filename, Point2D qmin, Point2D qmax, Point2D [] data, IterableLinkedList<Point2D> result)
      {
          try
          {
            FileWriter fileWriter = new FileWriter(new File(filename));
            double radius=1;
            //write svg header
            {
              double [] bbox={Double.MAX_VALUE,-Double.MAX_VALUE,Double.MAX_VALUE,-Double.MAX_VALUE};
              for(Point2D pt : data)
              {
                if(pt.x < bbox[0]) bbox[0]=pt.x;
                if(pt.x > bbox[1]) bbox[1]=pt.x;
                if(pt.y < bbox[2]) bbox[2]=pt.y;
                if(pt.y > bbox[3]) bbox[3]=pt.y;
              }
              //padding
              double width=(bbox[1]-bbox[0]);
              double height=(bbox[3]-bbox[2]);
              double pad=Math.min(width,height)/10;
              bbox[0]-=pad; bbox[1]+=pad; bbox[2]-=pad; bbox[3]+=pad;
              width=(bbox[1]-bbox[0]);
              height=(bbox[3]-bbox[2]);
              radius=Math.min(width,height)*1.0/200;
              //
              String header="<?xml version=\"1.0\" standalone=\"no\" ?>\n<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n<svg ";
              header+=(" width=\""+ 800 +"px\""); //let us fix the window size to 800x800
              header+=(" height=\""+ 800 +"px\"");
              header+=(" viewBox=\""+bbox[0]+" "+bbox[2]+" "+width+" "+height+" \"");
              header+=(" xmlns=\"http://www.w3.org/2000/svg\"");
              header+=(" version=\"1.1\""+">\n");
              fileWriter.write(header);
            }

            //write each point
            double R=200.0, G=50.0, B=120.0;

            for(Point2D pt : data)
            {
              fileWriter.write(svgCircle(pt,radius,R,G,B));
            }

            //write Range
            {
              R=250.0; G=250.0; B=120.0;
              fileWriter.write(svgBox(qmin,qmax,radius,R,G,B));
            }

            //write points in range
            radius*=1.1;
            R=50.0; G=250.0; B=120.0;
            for(Point2D pt : result)
            {
              fileWriter.write(svgCircle(pt,radius,R,G,B));
            }

            //write svg footer
            {
              String footer="</svg>\n";
              fileWriter.write(footer);
            }

            //done
            fileWriter.flush();
            fileWriter.close();
          }
          catch (IOException e) {
            e.printStackTrace();
          }

          System.out.println("- Saved results to "+filename);
      }

      public static String svgCircle(Point2D pt, double radius, double R, double G, double B)
      {
        double stroke_width=radius/10;
        String str="<circle cx=\""+pt.x+"\" cy=\""+pt.y+"\" r=\""+radius+"\"";
        str+=" fill=\"rgb("+R+","+G+","+B+")\" ";
        str+=" stroke-width=\""+stroke_width+"\" stroke=\"rgb("+R/3+","+G/3+","+B/3+")\" ";
        str+="/>\n";
        return str;
      }

      public static String svgBox(Point2D qmin, Point2D qmax, double radius, double R, double G, double B)
      {
        String str="<rect x=\""+qmin.x+"\" y=\""+qmin.y+"\" width=\""+(qmax.x-qmin.x)+"\" height=\""+(qmax.y-qmin.y)+"\"";
        str+=" fill=\"rgb("+R+","+G+","+B+")\" fill-opacity=\"0.4\" ";
        str+=" stroke-width=\""+radius/10+"\" stroke=\"rgb("+R/2+","+G/2+","+B/2+")\" ";
        str+="/>\n";
        return str;
      }

}
