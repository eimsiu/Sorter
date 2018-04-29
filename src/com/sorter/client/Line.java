package com.sorter.client;


public class Line{
    int n;	//how many words in the line
    String[] words;	//all the words inside the line
    
    
    //
    /**
     * instantiate method
     * @param sk - amount of words inside the line
     * @param words - all the words inside the line
     */
    Line(int sk, String[] words){
        this.n = sk;
        this.words = words;
    }
    
    /**
     * @param i - which column to the line to get
     * @return the i-th element of words
     */
    public String getWord(int i){
        return words[i];
    }
    
    /**
     * @return the amount of words inside the line
     */
    public int getN(){
        return n;
    }
    
    public void clear() {
    	n = 0;
    	words = new String[0];
    }
}
