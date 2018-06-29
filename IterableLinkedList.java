//
// !!! Do NOT Change anything in this file
//

//This from in-class coding.

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Comparator;

public class IterableLinkedList<T> implements Iterable<T>
{

  public int size;
  Node<T> head, tail;

  //----------------------------------------

  private class MyIterator implements Iterator<T>
  {
    Node<T> cur;
    Node<T> last;
    int mysize;


    MyIterator()
    {
        this.cur=head;
        this.last=null;
        this.mysize=size;
    }

    public boolean hasNext()
    {
      if(cur==null) return false;
      return true;
    }

    public T next()
    {
      if(mysize!=size) throw new NoSuchElementException("list changed");
      if(hasNext()==false) throw new NoSuchElementException("no next");
      T v=this.cur.data;
      this.last=this.cur;
      this.cur=this.cur.next;
      return v;
    }

    public void remove()
    {
      if(this.last!=null)
      {
        IterableLinkedList.this.remove(this.last);
        this.last=null;
      }
      else throw new NoSuchElementException();

    }

  }

  //create iterator
  public Iterator<T> iterator()
  {
    return new MyIterator();
  }

  //----------------------------------------

  private class Node<X>
  {
    Node(X _data){ data=_data; }
    X data;
    Node<X> next;
    Node<X> pre;
  }

  // Constructor to create an empty list
  public IterableLinkedList( ){
    size=0;
  }


  // Add x at position i
  public void insert(int i, T x)
  {
    if(i<0 || i>=size){
      throw new IndexOutOfBoundsException("shit this is out of bound: "+i);
    }

    Node<T> n=this.head;
    for(int j=0;j<i;j++) n=n.next;

    //
    Node<T> m=new Node<T>(x);
    m.pre=n;
    m.next=n.next;
    n.next=m;
    if(m.next!=null) m.next.pre=m;

    //
    if(n==tail) tail=m;

    size++;
  }

  //Add x at the end
  public void add(T x)
  {
    Node<T> n=new Node<T>(x);

    if(tail!=null)
    {
      tail.next=n;
      n.pre=tail;
      tail=n;
    }
    else{
      head=tail=n;
    }

    size++;
  }

  //get the i-th element
  public T get(int i)
  {
    if(i<0 || i>=size) return null;
    Node<T> n=this.head;
    for(int j=0;j<i;j++) n=n.next;

    return n.data;
  }

  public void set(int i, T x){

    if(i<0 || i>=size){
      throw new IndexOutOfBoundsException("shit this is out of bound: "+i);
    }

    Node<T> n=this.head;
    for(int j=0;j<i;j++) n=n.next;

    n.data=x;
  }

  public void remove(Node<T> n)
  {
    //delete n
    if(n.pre!=null) n.pre.next=n.next;
    if(n.next!=null) n.next.pre=n.pre;

    if(n==tail) tail=n.pre;
    if(n==head) head=n.next;

    size--;
  }

  public void remove(int i)
  {
    if(i<0 || i>=size){
      throw new IndexOutOfBoundsException("shit this is out of bound: "+i);
    }

    Node<T> n=this.head;
    for(int j=0;j<i;j++) n=n.next;
    remove(n);
  }

  //get the i-th element
  public T find(T x)
  {
    Node<T> n=this.head;
    for(int i=0;i<size;i++)
    {
      T data=n.data;
      if(data.equals(x)) return data;
      n=n.next;
    }

    return null; //not found
  }

  public int size(){ return size; }

  public void sort( Comparator<T> comp )
  {
    boolean swapped=false;
    do {
      swapped=false;
      Node<T> ptr=head;
      while(ptr!=null)
      {
        Node<T> next=ptr.next;
        if(next!=null)
        {
          if(comp.compare(ptr.data,next.data)>0) //ptr is bigger than next
          {
            T tmp=ptr.data;
            ptr.data=next.data;
            next.data=tmp;
            swapped=true;
          }
        }
        ptr=next;
      }//end while(ptr)
    } while (swapped);
  }

  //Tests
  public static void main(String [] args)
  {
    IterableLinkedList<String> sLL=new IterableLinkedList<>();
    sLL.add("abcde");
    sLL.add("xyz");
    sLL.add("gmu");
    sLL.add("loves");
    sLL.add("$$$");
    sLL.add("xy");

    for(String ss : sLL)
    {
      System.out.println("unsorted values: "+ss);
    }

    sLL.sort(new Comparator<String>(){
      public int compare(String a, String b){return a.compareTo(b);}
    });

    for(String ss : sLL)
    {
      System.out.println("sorted values: "+ss);
    }

    //test 1
    Iterator it = sLL.iterator();

    while(it.hasNext()) {
       System.out.println("t1 values: "+(String)it.next());
     }

    //test2
    for(String ss : sLL)
    {
      System.out.println("t2 values: "+ss);
    }

    //test3
    sLL.forEach(value -> System.out.println("t3 values: "+value));

    // long count = sLL.stream()
    //               .filter(value -> value.contains("e"))
    //               .count();

  }
}
