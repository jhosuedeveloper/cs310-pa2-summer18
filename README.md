# Range Search

- CS 310 Programming Assignment 2 Due: **July 8th** 11:59pm, 2018

## Assignment Objective
Given a set of 2D points, the problem of range search takes a 2D box and reports all points inside the box. In this assignment, you will implement a data structure called [range tree](https://en.wikipedia.org/wiki/Range_tree). Wikipedia provides a pretty good introduction on range tree, which in nutshell is a k-dimensional binary tree. In this assignment k=2. Range tree allows efficient orthogonal range search.

This picture shows an example of 2D range search.

<img src="https://cs.gmu.edu/~jmlien/teaching/cs310/uploads/Site/rangesearch.png" width="600">

## Table of Contents
1. [Introduction](#introduction-and-definitions)
2. [Input/Output](#input-output)
3. [Examples](#examples)
4. [Tasks](#tasks)
5. [Rules](#rules)
6. [Submission Instructions](#submission-instructions)
7. [Grading Rubric](#grading-rubric)
8. [Useful Links](#external-links)

## Introduction and Definitions

### 1D Range search
1D range tree is basically a balanced binary search tree.

- The leaves of the tree store the points.
- The internal nodes of T store splitting values to guide the search, i.e., the largest value in the left sub-tree

<img src="https://cs.gmu.edu/~jmlien/teaching/cs310/uploads/Site/bst.png" width="600">

Searching in a range tree requires you to determine the split node where the paths to x and x' splits. Here x and x' are the lower and upper bound of the range. The example below shows the range search of [6, 16]. The split node is node 14.

<img src="https://cs.gmu.edu/~jmlien/teaching/cs310/uploads/Site/1Dsearch.png" width="600">

### Pseudo code for 1D Range search
You will implement 1D range tree first, and then generalized it to 2D.


```java
Build 1D Range Tree (20%)

Input: A set P of 1-D points on a line.
Output: The root of 1-D range tree.

Node Build1DRangeTree(P)
{
	if( P contains only 1 point )
	{
		Create a node v storing this point 
	}
	else
	{
		Split P into 3 subsets: 
		    x_mid: the median;
		    P_left containing points with value <= x_mid; 
		    P_right containing points with value > x_mid;
		   
	         vleft = Build1DRangeTree(Pleft);
	         vright = Build1DRangeTree(Pright);
	
		 Create a node v storing xmid;
		 make vleft left child of v; 
		 make vright right child of v;
	}
	
	return v
}

```

 ```java
//Goal: Finding the split node for a given range for a given tree (10%)

//Input: Two values x and x' with x <= x'
//Output: The node v where the paths to x and x' splits, or the leaf where both paths end.
FindSplitNode(x, x')
{
	 v = root;
	 while( v is not a leaf and (x' <= xv or x > xv) )
	 {
	     if( x' <= xv )
	     	v = lc(v); // left child of the node v 
	     else 
	     	v = rc(v); // right child of the node v 
	 }
	 return v;
}
```

```java
//1D Range Search (25%)
//Input: A range tree T and a range [x:x']
//Output: All points that lie in the range.

RangeSearch_1D(x,x')
{
	vsplit = FindSplitNode(x, x')
	if( vsplit is a leaf )
	{
		Check if the point stored at vsplit must be reported
	}
	else 
	{
		// Follow the path to x and report the points in subtrees right of the path
		v = leftchild(vsplit);
		while( v is not a leaf )
		{
			if ( x <= v )
			{
				ReportSubTree(rc(v));
				v = lc(v);
			}
			else v = rc(v);
		}
		Check if the point stored at leaf v must be reported
		
		// Follow the path to x' and report the points in subtrees left of the path
		v = rightchild(vsplit);
		while( v is not a leaf )
		{
			if ( x' >= v )
			{
				ReportSubTree(lc(v));
				v = rc(v);
			}
			else v = lc(v);
		}
		Check if the point stored at leaf v must be reported
	}
}
```


### 2D Range search

An example of a 2D range tree is shown below. 2D Range tree has two levels

- First level is a 1D BST on x-axis (x-BST)
- For each node of x-BST, we build a 1D BST on y-axis for values in the sub-tree

<img src="https://cs.gmu.edu/~jmlien/teaching/cs310/uploads/Site/2Dtree.png" width="600">

We can perform the 2D range query similarly by only visiting the associated binary search tree on y-coordinate of the sub-tree of v, whose x-coordinate lies in the x-interval of the query rectangle

<img src="https://cs.gmu.edu/~jmlien/teaching/cs310/uploads/Site/2Dsearch.png" width="600">


### Pseudo code for 2D Range search

```java
Build 2D Range Tree (20%)

Input: A set P of 2-D points on the plane.
Output: The root of 2-D range tree.

Node Build2DRangeTree(P)
{
	if( P contains only 1 point )
	{
		Create a node v storing this point 
		v.next_level = Build1DRangeTree(y-coordinates of the points in P);  
	}
	else
	{
		Split P into 3 subsets: 
		    x_mid: the median x-coordinate;
		    P_left containing points with x-coordinate <= x_mid; 
		    P_right containing points with x-coordinate > x_mid;
		   
	         vleft = Build2DRangeTree(Pleft);
	         vright = Build2DRangeTree(Pright);
	
		 Create a node v storing xmid; 
		 make vleft left child of v;
		 make vright right child of v;
		 v.next_level = Build1DRangeTree(y-coordinates of the points in P);  
	}
	
	return v
}

```

```java
2D Range Search (25%)

Input: A range R=[x:x', y:y']
Output: All points that lie in the range.

RangeSearch_2D(x,x', y, y')
{

	vsplit = FindSplitNode(x,x')
	
	if( vsplit is a leaf )
	{
		Check if the point stored at vsplit must be reported
	}
	else { 
		// Follow the path to x and report the points in subtrees right of the path
		v = vsplit.left
		while(v is not a leaf) do {
			if( x <= v.x )
			{ 
			  if( v.right.next_level!=null )
				v.right.next_level.RangeSearch_1D(y,y');
			  else
			  {
				  if(v.right is not a leaf) { 
					ReportSubTree(v.right);
				  }
				  else{
					Check if the point stored at leaf v.right must be reported 
				  }
			  }
			  v = v.left 
			}
			else{ v = v.right} 
		} //end while
		Check if the point stored at leaf v must be reported
		
		// Follow the path to x' and report the points in subtrees left of the path
		v = vsplit.right
		while(v is not a leaf) do {
			if( x' >= v.x )
			{ 
			  if( v.left.next_level!=null )
				v.left.next_level.RangeSearch_1D(y,y');
			  else
			  {
				  if(v.left is not a leaf) { 
					ReportSubTree(v.left);
				  }
				  else{
					Check if the point stored at leaf v.left must be reported 
				  }
			  }
			  v = v.right 
			}
			else{ v = v.left} 
		} //end while
		Check if the point stored at leaf v must be reported
	}
}
```

## Input Output

The inputs are basically just a list of points. This will be stored in a file.

### Input Format

- The first line is a positive integer n>=1, indicate the number of points in this file
- The next *n* lines, contains n 3D points (x, y, z), numbers will be given. No need to check if they are actually number.
- Queries, the input points will be followed by lines of queries until a line containing only "exit" is reahed.
	- Queries are in the format of min_x max_x min_y max_y
	- When a keyword ***exist*** is given, the code prints out "bye" and terminates.

An example input with 10 points and 3 queries is shown below.

 ```
10
0.366	0.136	
0.082	0.791	
0.138	0.968	
0.414	0.785	
0.546	0.044	
0.513	0.612	
0.415	0.640	
0.199	0.999	
0.896	0.835	
0.091	0.719	
0.25 0.75 0.25 0.75 
0.75 0.25 0.8 0.1 
0.95 1.75 0.1 0.01 
exit
 ```

### Output format

Print the points in sorted order. That is, sort the points by x coordinates in ascending order. If two points have the same x coordinate, the point with smaller y should be printed.


## Examples

```
> java rtree input_file.txt
Read in 10 points
Query: 0.25 0.75 0.25 0.75 
Found 1 point
0.513	0.612	

Query: 0.75 0.25 0.8 0.1
Found 2 points
0.414	0.785	
0.513	0.612	

Query:  0.95 1.75 0.1 0.01
Found 0 point

Query:  exit
bye
```

## Tasks


## Rules

### You must

1. Have a style (indentation, good variable names, etc.)
2. Comment your code well in JavaDoc style (no need to overdo it, just do it well)
3. Have code that compiles with the command: javac *.java in your user directory
4. **Print regions to terminal by size, large to small**

### You may 

1. Import the following libraries
```java
import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.io.FileWriter;
import java.util.Scanner;
import java.io.FileNotFoundException;
```

### You cannot 
1. Make your program part of a package.
2. Import any additional libraries/packages
3. Copy code from your text book _Data Structures and Problem Solving Using Java_, 4th Edition by _Mark A. Weiss_

## Submission Instructions
- Use the cloud or some other server to backup your code!
- Remove all test files, jar files, class files, etc.
- You should just submit your java files and your readme.txt
- Zip your user folder (not just the files) and name the zip “username-p1.zip” (no other type of archive) where “username” is your username.
- Submit to blackboard.

## Grading Rubric
[back to top](#table-of-contents)

### No Credit
- Non submitted assignments
- Late assignments after 48 hours (**late tokens will be automatically applied**)
- Non compiling assignments
- Non-independent work
- "Hard coded" solutions
- Code that would win an obfuscated code competition with the rest of CS310 students.

### How will my assignment be graded?
- Grading will be divided into two portions:
  1. Manual/Automatic Testing (100%): To assess the correctness of programs.
  2. Manual Inspection (10% off the top points): [A checklist](#manual-code-inspection-rubric-10-off-the-top-points) of features your programs should exhibit. These comprise things that cannot be easily checked via unit tests such as good variable name selection, proper decomposition of a problem into multiple functions or cooperating objects, overall design elegance, and proper asymptotic complexity. These features will be checked by graders and assigned credit based on level of compliance. See the remainder of this document for more information.
- You CANNOT get points (even style/manual-inspection points) for code that doesn't compile or for submitting just the files given to you with the assignment. You CAN get manual inspection points for code that (a) compiles and (b) is an "honest attempt" at the assignment, but does not pass any unit tests.

#### Manual/Automatic Testing (100%)
- Your output images will be compared with our output via the following command
```
diff your-output.svg my-ouput.svg
```

#### Manual Code Inspection Rubric (10% "off the top" points)
These are all "off the top" points (i.e. items that will lose you points rather than earn you points):

Inspection Point | Points | High (all points) | Med (1/2 points) | Low (no points)
:---: | :---: | :--- | :--- | :--- 
Submission Format (Folder Structure) |  2 |  Code is in a folder which in turn is in a zip file. Folder is correctly named. | Code is not directly in user folder, but in a sub-folder. Folder name is correct or close to correct. | Code is directly in the zip file (no folder) and/or folder name is incorrect.
Code Formatting | 2 | Code has a set indentation and formatting style which is kept consistent throughout and code looks "well laid out".| Code has a mostly consistent indentation and formatting style, but one or more parts do not match.|Code indentation and formatting style changes throughout the code and/or the code looks "messy".
JavaDocs | 3 | The entire code base is well documented with meaningful comments in JavaDoc format. Each class, method, and field has a comment describing its purpose. Occasional in-method comments used for clarity. | The code base has some comments, but is lacking comments on some classes/methods/fields or the comments given are mostly "translating" the code. | The only documentation is what was in the template and/or documentation is missing from the code (e.g. taken out).
Coding conventions | 3 | Code has good, meaningful variable, method, and class names. All (or almost all) added fields and methods are properly encapsulated. For variables, only class constants are public. | Names are mostly meaningful, but a few are unclear or ambiguous (to a human reader) [and/or] Not all fields and methods are properly encapsulated. |  Names often have single letter identifiers and/or incorrect/meaningless identifiers. [Note: i/j/k acceptable for indexes.] [and/or] Many or all fields and methods are public or package default.

### External Links
- N/A

