import java.util.*;
import java.util.Comparator;

// There are 3 Tasks in this file

public class RangeTree1D<T>
{
  //Task 1: this is Build1DRangeTree(P) (20 pts)
  public Node<T> build( T [] data_array)
  {
    //your code here.

    return null;
  }

  //Task #2: FindSplitNode(x, x')
  //Method to find the split node.
  protected Node<T> FindSplitNode( Range range)
  {
    return null;
  }

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
    //your code here
  }

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
    RT1D.build(data);
    Double min=3.5, max=7.99;
    IterableLinkedList<Double> result=RT1D.Search1D(min, max);
    result.sort(new NC());
    System.out.println("Query: "+min+" "+max+"\nResults=");
    for(Double v : result)
    {
      System.out.println(v);
    }
  }

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
