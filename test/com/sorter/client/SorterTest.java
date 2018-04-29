package com.sorter.client;

import com.google.gwt.junit.client.GWTTestCase;

public class SorterTest extends GWTTestCase{
	
	/**
	   * Must refer to a valid module that sources this class.
	   */
	  public String getModuleName() {                                         // (2)
	    return "com.sorter.Sorter";
	  }

	  /**
	   * Add as many tests as you like.
	   */
	  public void testSimple() {                                              // (3)
	    assertTrue(true);
	  }
	  
	  public void testLine() {
		  String[] words = {"test", "word2"};
		  int n = 2;

		  Line l = new Line(n,words);
		  assertNotNull(l);
		  assertEquals(words[0], l.getWord(0));
		  assertEquals(words[1], l.getWord(1));
		  assertEquals(n, l.getN());
		}
	  
	  public void testReadData(){
		  String content = "Test word 2" + "\n" + "Testing intensifies";
		  Line[] l = new Line[2];
		  int n = 0;
		  
		  n = Sorter.ReadData(content, l, n);
		  
		  assertEquals(n,2);
		  
		  assertEquals(3,l[0].getN());
		  assertEquals("Test", l[0].getWord(0));
		  assertEquals("word", l[0].getWord(1));
		  assertEquals("2", l[0].getWord(2));
		  
		  assertEquals(2,l[1].getN());
		  assertEquals("Testing", l[1].getWord(0));
		  assertEquals("intensifies", l[1].getWord(1));
		  
	  }

	  public void testSortData() {
		  Line[] l = new Line[3];
		  l[0] = new Line(0,new String[] {"\n"});
		  l[1] = new Line(3,new String[] { "One", "Two", "Three" });
		  l[2] = new Line(3,new String[] { "1", "2", "3" });
		  
		  
		  Sorter.Sort(l, 3);
		  
		  assertEquals(l[0].getWord(0),"1");
		  assertEquals(l[0].getWord(1),"2");
		  assertEquals(l[0].getWord(2),"3");
		  assertEquals(l[0].getN(),3);
		  
		  assertEquals(l[1].getN(),0);
		  
		  assertEquals(l[2].getWord(0),"One");
		  assertEquals(l[2].getWord(1),"Two");
		  assertEquals(l[2].getWord(2),"Three");
		  assertEquals(l[2].getN(),3);
		  
	  }
}
