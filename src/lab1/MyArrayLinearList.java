package lab1;

import java.util.NoSuchElementException;
import  dataStructures.ArrayLinearList;
import java.util.Scanner;
public class MyArrayLinearList extends ArrayLinearList{
	public int max() {
		if (this.size() == 0) {
			throw new NoSuchElementException("cannof find max from empty element");
		}
		int max = (int)this.element[0];
		for(int i = 0;i<this.size();i++) {
			if(max < (int)this.element[i]) {
				max = (int)this.element[i];
			}
		}
		return max;
	}
	public int min() {
		if (this.size() == 0) {
			throw new NoSuchElementException("cannof find min from empty element");
		}
		int min = (int)this.element[0];
		for(int i = 0;i<this.size();i++) {
			if(min > (int)this.element[i]) {
				min = (int)this.element[i];
			}
		}
		return min;
	}
	public int sum() {
		int sum = 0;
		for(int i = 0;i<this.size();i++) {
			sum = sum + (int)this.element[i];
		}
		return sum;
	}
	public float average() {
		float sum = (float)this.sum();
		float size = (float)this.size();
		return sum/size;
	}
	public void removeOdd() {
		for(int i = 0;i<this.size();i++) {
			if((int)this.element[i] % 2 ==1) {
				this.remove(i);
				i--;
			}
		}
	}
	public void quickSort(int low, int high) {
		if(low < high) {
			int pivot = this.partition(low,high, high);
			this.quickSort(low, pivot -1);
			this.quickSort(pivot +1, high);
		}
	}
	// 4,3,2,5
	public int partition(int low, int high, int pivot_i) {
		int j = -1;
		int pivot = (int)this.get(pivot_i);
		for(int i = low; i <= high;i++ ) {
			if((int)this.get(i) < pivot) {
				j++;
				this.swap(this.element, j, i);
			}
		}
		this.swap(this.element, j+1, pivot_i);
		return j+1;
	}
	public void swap(Object array[], int i,int j) {
		Object temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	public static void main(String[] args) {
		MyArrayLinearList l = new MyArrayLinearList();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
        	System.out.println("\n=== MyArrayLinearList Interactive Menu ===");
            System.out.println("1. Add an element");
            System.out.println("2. View list elements");
            System.out.println("3. Get Maximum value");
            System.out.println("4. Get Minimum value");
            System.out.println("5. Get Sum");
            System.out.println("6. Get Average");
            System.out.println("7. Remove all odd numbers");
            System.out.println("8. Sort list");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
        int choice = -1;
        try {
        	choice = Integer.parseInt(scanner.nextLine());
        }
        catch(NumberFormatException e){
        	System.out.println("Enter a valid number");
        	continue;
        }
        try {
        	switch (choice) {
        	case 1:
        		int value = Integer.parseInt(scanner.nextLine());
        		l.add(l.size(), value);
        		System.out.println(l.toString());
        		break;
        	case 2:
        		System.out.println(l.toString());
        		break;
        	case 3:
        		int max = l.max();
        		System.out.println("max:"+max);
        		break;
        	case 4:
        		int min = l.min();
        		System.out.println("min:"+min);
        		break;
        	case 5:
        		int sum = l.sum();
        		System.out.println("sum:"+sum);
        		break;
        	case 6:
        		float average = l.average();
        		System.out.println("average:"+average);
        		break;
        	case 7:
        		l.removeOdd();
        		System.out.println("odd element removed list:"+l.toString());
        		break;
        	case 8:
        		l.quickSort(0,l.size()-1);
        		System.out.println("Sorted list:"+l.toString());
        		break;
        	case 0:
        		running = false;
        		break;
        	}
        }
        catch(Exception e) {
        	System.out.println("Error occured. Try again");
        }
	}
        scanner.close();
	}
}
