package com.engru.al.engru;

import android.util.Log;

import java.util.ArrayList;

public class MyArrayIntegerStringListToString extends ArrayList<String> {
    private static final String startArray="Start";
    private static final String endArrays="End";
    private static final String charsToSplit="\t";
    @Override public String toString() {
        String array=startArray;
        if(this.size()==0){
            return null;
        }
        for (int i=0;i!=this.size();++i){
            if(i==this.size()-1){
                array+=this.get(i)+endArrays;
                break;
            }
            array+=this.get(i)+charsToSplit;
        }
        return array;
    }
    public static ArrayList<String> backwardFromString(String x){
        if(x!=null && x.startsWith(startArray) && x.endsWith(endArrays)){
            x=x.replace(startArray,"");
            x=x.replace(endArrays,"");
        }else{
            return null;
        }
        String [] oldArray=x.split(charsToSplit);
        ArrayList<String> newArray=new ArrayList<String>(charsToSplit.length());
        for (String i:oldArray){
            newArray.add(i);
        }
        return newArray;
    }
}