package com.example.finalproject.system;

public class ArrayUnits {

	public ArrayUnits() {
		// TODO Auto-generated constructor stub
	}
	
	public static int arrayIndexOf(int array[],int value){
		int index = 0;
		for(index=0;index<array.length;++index){
			if(array[index]==value)
				break;
		}
		return index;
	}

}
