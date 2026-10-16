package lab3;

import dataStructures.ArrayStack;
import java.util.Random;
import java.util.Scanner;

public class MyStack extends ArrayStack {

	public MyStack rand() {
		Object[] arr = this.toArray();
		Random rnd = new Random();
		
		for (int i = arr.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			Object temp = arr[index];
			arr[index] = arr[i];
			arr[i] = temp;
		}
		
		MyStack randomStack = new MyStack();
		for (int i = arr.length - 1; i >= 0; i--) {
			randomStack.push(arr[i]);
		}
		return randomStack;
	}

	public MyStack unique() {
		Object[] arr = this.toArray();
		MyStack uniqueStack = new MyStack();

		for (int i = 0 ;i<arr.length;i++) {
			Object current = arr[i];
			if (!uniqueStack.exists(current)) {
				uniqueStack.push(current);
			}
		}
		return uniqueStack;
	}

	public MyStack addRange(Object[] elements) {
		for (Object e : elements) {
			this.push(e);
		}
		return this;
	}

	public Object[] toArray() {
		MyStack temp = new MyStack();
		int count = 0;
		
		while (!this.empty()) {
			temp.push(this.pop());
			count++;
		}
		
		Object[] result = new Object[count];

		for (int i = 0 ;i< count;i++) {
			Object o = temp.pop();
			result[i] = o;
			this.push(o); 
		}
		
		return result;
	}

	public boolean exists(Object element) {
		Object[] arr = this.toArray();
		for (Object o : arr) {
			if (o.equals(element)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		MyStack stack = new MyStack();
		Scanner scanner = new Scanner(System.in);
		boolean running = true;

		while (running) {
			System.out.println("\n=== MyStack Интерактив Цэс ===");
			System.out.println("1. empty (Стек хоосон эсэх)");
			System.out.println("2. peek (Оройн элементийг харах)");
			System.out.println("3. push (Стек рүү элемент нэмэх)");
			System.out.println("4. pop (Оройн элементийг гаргах)");
			System.out.println("5. toArray (Массив байдлаар харах)");
			System.out.println("6. addRange (Олон элемент зэрэг нэмэх)");
			System.out.println("7. exists (Элемент байгаа эсэхийг шалгах)");
			System.out.println("8. unique (Давхардлыг арилгах)");
			System.out.println("9. rand (Санамсаргүй холих)");
			System.out.println("0. Гарах");
			System.out.print("Сонголтоо оруулна уу: ");

			int choice = -1;
			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Зөвхөн тоо оруулна уу!");
				continue;
			}

			try {
				switch (choice) {
					case 1:
						System.out.println("Стек хоосон эсэх: " + stack.empty());
						break;
					case 2:
						System.out.println("Оройн элемент: " + stack.peek());
						break;
					case 3:
						System.out.print("Нэмэх элементээ оруулна уу: ");
						String pushVal = scanner.nextLine();
						stack.push(pushVal);
						System.out.println(pushVal + " элементийг нэмлээ.");
						break;
					case 4:
						System.out.println("Гаргасан элемент: " + stack.pop());
						break;
					case 5:
						Object[] arr = stack.toArray();
						System.out.print("Стекийн элементүүд (Оройгоос ёроол руу): [ ");
						for (Object o : arr) {
							System.out.print(o + " ");
						}
						System.out.println("]");
						break;
					case 6:
						System.out.print("Элементүүдийг таслалаар тусгаарлан оруулна уу (жишээ нь: a,b,c): ");
						String line = scanner.nextLine();
						String[] sArray = line.split(",");
						stack.addRange(sArray);
						System.out.println("Элементүүдийг нэмлээ.");
						break;
					case 7:
						System.out.print("Шалгах элементээ оруулна уу: ");
						String searchVal = scanner.nextLine();
						System.out.println(searchVal + " элемент байна уу? " + stack.exists(searchVal));
						break;
					case 8:
						MyStack uStack = stack.unique();
						System.out.print("Давхардалгүй стек: [ ");
						for (Object o : uStack.toArray()) {
							System.out.print(o + " ");
						}
						System.out.println("]");
						break;
					case 9:
						MyStack rStack = stack.rand();
						System.out.print("Хольсон стек: [ ");
						for (Object o : rStack.toArray()) {
							System.out.print(o + " ");
						}
						System.out.println("]");
						break;
					case 0:
						running = false;
						System.out.println("Програмаас гарлаа.");
						break;
					default:
						System.out.println("Буруу сонголт байна. Дахин оролдоно уу.");
				}
			} catch (java.util.EmptyStackException e) {
				System.out.println("Алдаа: Стек хоосон байна!");
			} catch (Exception e) {
				System.out.println("Алдаа гарлаа: " + e.getLocalizedMessage());
			}
		}
		scanner.close();
	}
}