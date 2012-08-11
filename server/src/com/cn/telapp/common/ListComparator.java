package com.cn.telapp.common;
import java.util.Comparator;


public class ListComparator {
	
	public static class MemberListComparator implements Comparator{

		public int compare(Object o1, Object o2) {
			String obj1 = (String)o1;
			String obj2 = (String)o2;			
			return Integer.valueOf(obj1.split(";")[8]).compareTo(Integer.valueOf(obj2.split(";")[8]));
		}
	}
}