package org.example;

import java.util.*;
public class Solution {
    public List< Integer > superiorElements(int []a) {
        // Write your code here.
        List<Integer> result=new ArrayList<>();
        
        for(int i=0;i<a.length;i++){
            boolean isLeader=true;
            
            for(int j=i+1;j<a.length;j++){
                 if(a[j]>=a[i]){
                     isLeader=false;
                     break;
                 }
            }
            
            if(isLeader){
                result.add(a[i]);
            }
        }
        Collections.sort(result);
        return result;

    }
}