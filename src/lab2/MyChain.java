package lab2;

import dataStructures.Chain;
import lab1.MyArrayLinearList;

import java.util.Iterator;
import java.util.Scanner;
public class MyChain extends Chain {
public Object[] toArray() {
	Object [] m = new Object[this.size];
	Iterator iterator = this.iterator();
	int index = 0;
	
	while(iterator.hasNext()) {
		m[index] = iterator.next();
		index++;
	}
	return m;
}
public void addRange(Object [] elements) {
	for(Object e : elements) {
		this.add(size, e);
	}
}
public MyChain union(MyChain chain) {
	MyChain uc = new MyChain();
try {
	Object[] originalElements = this.toArray();
	for(Object e: originalElements) {
		uc.add(uc.size(), e);
	}
	Object[] additionalElements = chain.toArray();
	for(Object e: additionalElements) {
		if(uc.indexOf(e) != -1) {
			uc.add(uc.size(), e);
		}
	}
	return uc;
}catch(Exception e) {
	System.out.println("Error occured in union:" + e);
	return uc;
}
}
public MyChain intersection(MyChain chain) {
	MyChain in = new MyChain();
	Object[] originalElements = this.toArray();
	for(Object e: originalElements) {
		if(chain.indexOf(e) != -1) {
			in.add(in.size(), e);
		}
	}
	return in;
}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyChain c = new MyChain();
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
        	System.out.println("\n=== MyChain Interactive Menu ===");
            System.out.println("1. isEmpty");
            System.out.println("2. size");
            System.out.println("3. get");
            System.out.println("4. indexOf");
            System.out.println("5. remove");
            System.out.println("6. add");
            System.out.println("7. toString");
            System.out.println("8. toArray");
            System.out.println("9. addRange");
            System.out.println("10. union");
            System.out.println("11. intersection");
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
        		boolean empty = c.isEmpty();
        		System.out.println(empty);
        		break;
        	case 2:
        		System.out.println(c.size());
        		break;
        	case 3:
        		int getVal = Integer.parseInt(scanner.nextLine());
        		System.out.println(c.get(getVal));
        		break;
        	case 4:
        		int indexofVal = Integer.parseInt(scanner.nextLine());
        		System.out.println(c.indexOf(indexofVal));
        		break;
        	case 5:
        		int removeVal =  Integer.parseInt(scanner.nextLine());
        		c.remove(removeVal);
        		break;
        	case 6:
        		int addVal = Integer.parseInt(scanner.nextLine());
        		c.add(c.size, addVal);
        		break;
        	case 7:
        		System.out.println(c.toString());
        		break;
        	case 8:
        	 	Object [] massive = c.toArray();
        	 	String result = "[";
        	 	for(Object m: massive) {
        	 		result += m;
        	 		result += ',';
        	 	}
        	 	result += "]";
        	 	System.out.println(result);
        		break;
        	case 9:
        		String line = scanner.nextLine();
        		String [] sArray = line.split(",");
        		for(String s: sArray) {
        			c.add(c.size(), Integer.parseInt(s));
        		}
        		System.out.println(c.toString());
        		break;
        	case 10:
        		System.out.println("Элементүүдийг таслалаар тусгаарлан оруулна уу ?");
        		String unionLine = scanner.nextLine();
        		String [] uArray = unionLine.split(",");
        		MyChain unionC = new MyChain();
        		for(String s: uArray) {
        			unionC.add(unionC.size(), Integer.parseInt(s));
        		}
        		MyChain unionChain = c.union(unionC);
        		System.out.println(unionChain.toString());
        		break;
        	case 11:
        		System.out.println("Элементүүдийг таслалаар тусгаарлан оруулна уу ?");
        		String iLine = scanner.nextLine();
        		String [] iArray = iLine.split(",");
        		MyChain intC = new MyChain();
        		for(String s: iArray) {
        			intC.add(intC.size(), Integer.parseInt(s));
        		}
        		MyChain interChain = c.intersection(intC);
        		System.out.println(interChain.toString());
        		break;
        	case 0:
        		running = false;
        		break;
        	}
        }
        catch(Exception e) {
        	System.out.println(e.getLocalizedMessage());
        	System.out.println("Error occured. Try again");
        }
	}
        scanner.close();
	}

}
