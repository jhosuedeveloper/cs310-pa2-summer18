import java.util.Comparator;

// There are 2 Tasks in this file

public class RangeTree2D<T> extends RangeTree1D<T>
{
  // Task #4: this is Build2DRangeTree(P)
  // Build 2D Range Tree (15 pts)
  public Node<T> build( T [] data_array)
  {
    if(data_array.length==1)
    {
      Node<T> v = new Node<T>();
      
    }
    RangeTree1D<T> x = new RangeTree1D<T>();

    return null;
  }

  // Task #5: this is RangeSearch_2D(x,x', y, y') (25 pts)
  public void Search2D(Range query, IterableLinkedList<T> l)
  {
    //Your code here
  }

  //!!! Do NOT CHANGE BELOW !!!
  public IterableLinkedList<T> Search2D(T min, T max)
  {
    IterableLinkedList<T> l = new IterableLinkedList<T>();
    Search2D(new Range(min, max), l);
    return l;
  }

  Comparator<T> comp_next_level;

  RangeTree2D(Comparator<T> _comp_x, Comparator<T> _comp_y)
  {
    super(_comp_x); //comparator for X tree
    this.comp_next_level=_comp_y; //comparator for Y tree
  }

}
