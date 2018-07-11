import java.util.Comparator;

// There are 3 Tasks in this file

public class RangeTree1D<T>
{
  //Task 1: this is Build1DRangeTree(P) (20 pts)
  public Node<T> build( T [] data_array)
  {

    //declarations
    T []P_left;
    T []P_right;
    Node<T> x_mid = new Node<T>();

    Node<T> v = new Node<T>();

    if(data_array.length>=2)
    {

      boolean switched;

      do {
        switched = false;
        for(int i=0; i<data_array.length-1;i++)
        {
          if(comp.compare(data_array[i],data_array[i+1])>=1)
          {
            T temp = data_array[i];
            data_array[i] = data_array[i+1];
            data_array[i+1] = temp;
            switched = true;
          }
        }
      }while(switched==true);

    }//end of sorting if the array has at least 2 elements.

// for(int i =0 ; i< data_array.length; i++)
// {
//   System.out.println(data_array[i]);
// }
// System.out.println("----------");

    if(data_array.length==1)
    {
        v.data = data_array[0];
    }
    else
    {
      if(data_array.length%2==0)
      {
        x_mid.data = data_array[data_array.length/2-1];
        //@SuppressWarnings("unchecked")
        P_left = (T[]) new Object[data_array.length/2];
        P_right = (T[]) new Object[data_array.length - data_array.length/2];

        for(int i =0;i<P_left.length;i++)
        {
          P_left[i] = data_array[i];
        }
        for(int i =0;i<P_right.length;i++)
        {
          P_right[i] = data_array[data_array.length/2+i];
        }
      }
      else
      {
        x_mid.data = data_array[data_array.length/2];
        P_left = (T[]) new Object[data_array.length/2+1];
        P_right = (T[]) new Object[data_array.length - (data_array.length/2+1)];

        for(int i =0;i<P_left.length;i++)
        {
          P_left[i] = data_array[i];
        }
        for(int i =0;i<P_right.length;i++)
        {
          P_right[i] = data_array[data_array.length/2+i+1];
        }

      }
      Node<T> vleft = new Node<T>();
      Node<T> vright = new Node<T>();


      vleft = build( P_left);
      vright = build( P_right);
      v = x_mid;
      v.left = vleft;
      v.right = vright;
    }

    return v;

  }//end of build method



  //Task #2: FindSplitNode(x, x')
  //Method to find the split node.
  protected Node<T> FindSplitNode( Range range)
  {
    Node<T> v = new Node<T>();
    v= this.root;

    /*
    if(v.data==null)
    {

      System.out.println("hello root is null");
    }
    System.out.println("---" + (Double)v.data);

    System.out.println("---" + (Double)v.left.data);
    System.out.println("---" + (Double)v.right.data);

    System.out.println("---" + (Double)v.left.left.data);
    System.out.println("---" + (Double)v.left.right.data);
    System.out.println("---" + (Double)v.right.left.data);
    System.out.println("---" + (Double)v.right.right.data);
    System.out.println("-----------------------------" );

    System.out.println("---" + (Double)v.left.left.left.data);
    System.out.println("---" + (Double)v.left.left.right.data);

    System.out.println("---" + (Double)v.left.right.left.data);
    System.out.println("---" + (Double)v.left.right.right.data);

    System.out.println("---" + (Double)v.right.left.left.data);
    System.out.println("---" + (Double)v.right.left.right.data);
    System.out.println("---" + (Double)v.right.right.left.data);
    System.out.println("---" + (Double)v.right.right.right.data);
*/




    while( (v.is_leaf() == false)  && ((Double)range.max <= (Double)v.data || (Double)range.min > (Double)v.data))
    {
      if((Double)range.max <= (Double)v.data)
      {
        v = v.left; // left child of the node v
      }
      else
      {
        v = v.right; // right child of the node v
      }
    }




    return v;
  }//end fo FindSplitNode Method

  //Method to make range and call search.
  //Do NOT CHANGE THIS FUNCTION
  public IterableLinkedList<T> Search1D(T min, T max)
  {
    IterableLinkedList<T> l = new IterableLinkedList<T>();
    Search1D(new Range(min, max), l);
    return l;
  }

  //
  // Task #3: RangeSearch_1D(x,x')  (25 pts)
  // Note 1: Store results in "l"
  // Note 2: Use reportSubTree method below
  //
  public void Search1D(Range query, IterableLinkedList<T> l)
  {
    Node<T> vsplit = new Node<T>();

    vsplit = FindSplitNode(query);

    if(vsplit.is_leaf())
    {
      if(((Double)vsplit.data<=(Double)query.max) && ((Double)vsplit.data>=(Double)query.min))
      {
        reportSubTree(vsplit, l);

      }
        System.out.println("leafyyyy" );
    }
    else
    {
      //follow the path to x and report the points in subtrees riht of the path
      Node<T> v = new Node<T>();
      v = vsplit.left;
      while(v.is_leaf() ==false)
      {
        if((Double)query.min <= (Double)v.data )
        {
          reportSubTree(v.right,l);
          v = v.left;
          System.out.println("xxx" );
        }
        else
        {
          v = v.right;
          System.out.println("yy" );
        }
      }
      //check if the point stored at leasf v must be reported
      if(((Double)v.data<=(Double)query.max) && ((Double)v.data>=(Double)query.min))
      {
        reportSubTree(v, l);
        System.out.println("zz" + v.data);
      }





      //follow the path to x' and report the points in subtrees left of the path
      v = vsplit.right;
      while(v.is_leaf()==false)
      {
        if((Double)query.max >= (Double)v.data)
        {
          reportSubTree(v.left, l);
          v = v.right;
        }
        else
        {
          v = v.left;
        }
        System.out.println("sdf");
      }
      if(((Double)v.data<=(Double)query.max) && ((Double)v.data>=(Double)query.min))
      {
        reportSubTree(v, l);
      }





    }//end of big else

  }// end of RangeSearch_1D function

  //change this function to test your 1D range search locally
  //make sure you have this class working before moving to RangeTree2D
  public static void main(String [] args)
  {
    //your test code here



    class NC implements Comparator<Double> {
      public int compare(Double o1, Double o2){
        Double r=o1-o2;
        if(r>0) return 1;
        else if(r<0) return -1;
        else return 0;
      }
    }
    //
    RangeTree1D<Double> RT1D=new RangeTree1D<>(new NC());
    Double [] data={2.0,7.0,3.0,4.0,5.0,6.0,1.0,8.0,9.0,10.0};
    RT1D.root = RT1D.build(data);
    Double min=3.5, max=7.99;
    IterableLinkedList<Double> result=RT1D.Search1D(min, max);
    result.sort(new NC());
    System.out.println("Query: "+min+" "+max+"\nResults=");
    for(Double v : result)
    {
      System.out.println(v);
    }
  } // end of test main

  //
  // !!! DO NOT CHANGE ANYTHING BELOW !!!
  //

  protected Comparator<T> comp;

  public RangeTree1D(Comparator<T> _comp)
  {
    this.comp=_comp;
  }

  public class Range
  {
    public T min, max;

    public Range(T _min, T _max)
    {
      this.min=_min; this.max=_max;
    }

    public boolean is_inside(T p)
    {
        return (comp.compare(p,min) >= 0 && comp.compare(max,p) >= 0);
    }
  }

  public class Node<T>
  {
    public Node<T> left, right;
    RangeTree1D<T> next_level;
    public T data;

    public boolean is_leaf(){ return left == null && right == null; }
    public boolean has_next_level(){ return next_level!=null; }
  }

  protected Node<T> root;


  //Method to report subtree
  protected void reportSubTree(Node<T> n,IterableLinkedList<T> l)
  {
    if(n.right == null && n.left == null){
      l.add(n.data);
    }
    else{
      reportSubTree(n.left,l);
      reportSubTree(n.right,l);
    }
  }

}
