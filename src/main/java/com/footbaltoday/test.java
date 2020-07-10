package com.footbaltoday;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		list.add("A");
		list.add("B");
		list.add("C");
		list.add("D");
		list.add("E");
		list.add("F");
		List<String> itemsSelected = new ArrayList<String>();
		Random rand = new Random();
		itemsSelected = list;
         /*
         while (itemsSelected.size() < 3) {
        	 String t =  list.get(rand.nextInt(list.size()));	
        	 if(!itemsSelected.contains(t)) {
        		 itemsSelected.add(t);
        	 }
		}*/
         
         
         for (String string : itemsSelected) {
			System.out.println(string);
		}
	}

}
