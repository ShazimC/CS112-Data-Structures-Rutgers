package mcs;

import java.io.IOException;
import java.util.Scanner;
import java.util.Random;

/**
 * This class implements a mini cipher system.
 * 
 * @author RU NB CS112
 */
public class MiniCipherSys {
	
	/**
	 * Circular linked list that is the sequence of numbers for encryption
	 */
	SeqNode seqRear;
	
	/**
	 * Makes a randomized sequence of numbers for encryption. The sequence is 
	 * stored in a circular linked list, whose last node is pointed to by the field seqRear
	 */
	public void makeSeq() {
		// start with an array of 1..28 for easy randomizing
		int[] seqValues = new int[28];
		// assign values from 1 to 28
		for (int i=0; i < seqValues.length; i++) {
			seqValues[i] = i+1;
			
		}
		
		// randomize the numbers
		Random randgen = new Random();
 	        for (int i = 0; i < seqValues.length; i++) {
	            int other = randgen.nextInt(28);
	            int temp = seqValues[i];
	            seqValues[i] = seqValues[other];
	            seqValues[other] = temp;
	        }
	     
	    // create a circular linked list from this sequence and make seqRear point to its last node
	    SeqNode sn = new SeqNode();
	    sn.seqValue = seqValues[0];
	    sn.next = sn;
	    seqRear = sn;
	    for (int i=1; i < seqValues.length; i++) {
	    	sn = new SeqNode();
	    	sn.seqValue = seqValues[i];
	    	sn.next = seqRear.next;
	    	seqRear.next = sn;
	    	seqRear = sn;
	    }
	}
	
	/**
	 * Makes a circular linked list out of values read from scanner.
	 */
	public void makeSeq(Scanner scanner) 
	throws IOException {
		SeqNode sn = null;
		if (scanner.hasNextInt()) {
			sn = new SeqNode();
		    sn.seqValue = scanner.nextInt();
		    sn.next = sn;
		    seqRear = sn;
		}
		while (scanner.hasNextInt()) {
			sn = new SeqNode();
	    	sn.seqValue = scanner.nextInt();
	    	sn.next = seqRear.next;
	    	seqRear.next = sn;
	    	seqRear = sn;
		}
	}
	
	
	
	/**
	 * Implements Step 1 - Flag A - on the sequence.
	 */
	void flagA() {
	    // COMPLETE THIS METHOD
		
		SeqNode pointer = seqRear;
		
		do {
			if(pointer.seqValue == 27) {//if the node at which the pointer is pointing has 27
				//switch its value with the one in front of it.
				pointer.seqValue = pointer.next.seqValue;
				pointer.next.seqValue = 27;
				break;
			}else {
				pointer = pointer.next;//otherwise go to the next node.
			}
		}while(pointer != seqRear);
	
	}
	
	/**
	 * Implements Step 2 - Flag B - on the sequence.
	 */
	void flagB() {
	    // COMPLETE THIS METHOD
		
		//an arrow starting at the end of the circular LL 
		SeqNode pointer = seqRear;
		
		do {
			if(pointer.seqValue == 28) { //if the node at which the pointer is pointing has 28 
				
				//switch its value with the one in front of it
				pointer.seqValue = pointer.next.seqValue;
				pointer.next.seqValue = pointer.next.next.seqValue;
				pointer.next.next.seqValue = 28;
				break;
			}else {
				pointer = pointer.next;//otherwise go to the next node.
			}
		}while(pointer != seqRear);
	
	}
	
	/**
	 * Implements Step 3 - Triple Shift - on the sequence.
	 */
	void tripleShift() {
	    // COMPLETE THIS METHOD
		
		//if the rear and head are flags, there is nothing in between them, therefore do nothing.
		if( isFlag(seqRear) && isFlag(seqRear.next) )
			return;
		
		else if( isFlag(seqRear.next) ) {
			//if only the head is a flag
			
			//make a pointer starting at the first value after the head of the cll.
			SeqNode pointer = seqRear.next.next;
			
			do {
				//if the pointer is a flag
				if(pointer.seqValue == 27 || pointer.seqValue == 28) {
					//set it to the rear
					seqRear = pointer;
					break;//you're done
				}else pointer = pointer.next;//otherwise go to the next node.
					
			}while(pointer != seqRear);
			
			/* By setting the second sequential flag of the cll as the rear, you have visually shifted all of the nodes starting from the end forward
			 * until the second flag is the only one left. This successfully swaps all of the "0" nodes before the head flag with the nodes after the 
			 * second flag.
			 */
		}
		
		else if( isFlag(seqRear) ) {
			//if only the rear is a flag.
			SeqNode pointer = seqRear.next;
			//pointer starting at head.
			do {
				if(pointer.next.seqValue == 27 || pointer.next.seqValue == 28) {
					//go through the list and find the node right before the first sequential flag.
					seqRear = pointer;
					//set it to the rear and you're done.
					break;
				}else pointer = pointer.next;
					
			}while(pointer != seqRear);
			/*
			 * By setting this node right before the first sequential flag as rear, you visually shifted all of the nodes up until the first sequential flag
			 * became head, meaning it visually has "0" nodes before it. VISUALLY. This successfully swaps the "0" nodes after the rear flag with the nodes
			 * before the first sequential flag.
			 */
		}
		
		else {
		
			SeqNode pointer = seqRear.next;
			SeqNode origHead = seqRear.next;
			
			do {
				if(pointer.next.seqValue == 27 || pointer.next.seqValue == 28) {
					seqRear.next = pointer.next;
					break;
				}else pointer = pointer.next;
				
			}while(pointer != seqRear);
			
			SeqNode pointer2 = pointer.next.next;
			
			do {
				if(pointer2.seqValue == 27 || pointer2.seqValue == 28) {
					pointer.next = pointer2.next;
					pointer2.next = origHead;
					seqRear = pointer;
					break;
				}else pointer2 = pointer2.next;
			}while(pointer2 != pointer);
		}
	
	}
	
	/**
	 * Implements Step 4 - Count Shift - on the sequence.
	 */
	void countShift() {		
	    // COMPLETE THIS METHOD
		
		SeqNode pointer = seqRear;
		
		do {
			if(pointer.next == seqRear) {//find the node right before rear
				pointer.next = seqRear.next;//point it to the head
				
				int lastValue = seqRear.seqValue;
				if(lastValue == 28)
					lastValue = 27;
				
				for(int i = 0; i < lastValue; i++) {//shift pointer rearValue amount of times forward from the recently assigned head.
					pointer = pointer.next;
				}
				seqRear.next = pointer.next;//point rear to the new head, which is the node after the rearValueth node in the sequence.
				pointer.next = seqRear;//point the rearValueth node in the sequence to the rear.
				break;//break off when done
			}else pointer = pointer.next;
		}while(pointer != seqRear);

	}
	
	/**
	 * Gets a key. Calls the four steps - Flag A, Flag B, Triple Shift, Count Shift, then
	 * counts down based on the value of the first number and extracts the next number 
	 * as key. But if that value is 27 or 28, repeats the whole process (Flag A through Count Shift)
	 * on the latest (current) sequence, until a value less than or equal to 26 is found, 
	 * which is then returned.
	 * 
	 * @return Key between 1 and 26
	 */
	
	int getKey() {
	    // COMPLETE THIS METHOD
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE;
		int firstValue = 0;
		SeqNode pointer;
		do {
			flagA();		
			flagB();
			tripleShift();
			countShift();
			
			firstValue = seqRear.next.seqValue;
			if(firstValue == 28)
				firstValue = 27;
			
			pointer = seqRear.next;
			
			for(int i=firstValue; i>0; i--) {
				pointer = pointer.next;
			}
		}while(isFlag(pointer));
		
		return pointer.seqValue;
	}
	
	/**
	 * Utility method that prints a circular linked list, given its rear pointer
	 * 
	 * @param rear Rear pointer
	 */
	private static void printList(SeqNode rear) {
		if (rear == null) { 
			return;
		}
		System.out.print(rear.next.seqValue);
		SeqNode ptr = rear.next;
		do {
			ptr = ptr.next;
			System.out.print("," + ptr.seqValue);
		} while (ptr != rear);
		System.out.println("\n");
	}
	
	/**
	 * Encrypts a message, ignores all characters except upper case letters
	 * 
	 * @param message Message to be encrypted
	 * @return Encrypted message, a sequence of upper case letters only
	 */
	public String encrypt(String message) {	
	    // COMPLETE THIS METHOD
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		String output = "";
		
	    for(int i = 0; i < message.length(); i++) {
	    	int totalCharValue = 0;
	    	
	    	if(message.charAt(i) >= 'A' && message.charAt(i) <= 'Z') {
	    		totalCharValue = (int)message.charAt(i) + getKey();
	    		
	    		if(totalCharValue > (int)'Z') 
	    			totalCharValue -= 26;
	    		
	    		output += (char)totalCharValue;
	    	}
	    }
	    return output;
	}
	
	/**
	 * Decrypts a message, which consists of upper case letters only
	 * 
	 * @param message Message to be decrypted
	 * @return Decrypted message, a sequence of upper case letters only
	 */
	public String decrypt(String message) {	
	    // COMPLETE THIS METHOD
	    // THE FOLLOWING LINE HAS BEEN ADDED TO MAKE THE METHOD COMPILE
		String output = "";
		
	    for(int i = 0; i < message.length(); i++) {
	    	int totalCharValue = 0;
	    	
	    	if(message.charAt(i) >= 'A' && message.charAt(i) <= 'Z') {
	    		
	    		totalCharValue = (int)message.charAt(i) - getKey();
	    		
	    		if(totalCharValue < (int)'A')
	    			totalCharValue += 26;    		
	  
	    		output += (char)totalCharValue;
	    	}
	    }
	    return output;
		
	}
	
	private boolean isFlag(SeqNode node) {
		if(node.seqValue == 27 || node.seqValue == 28)
			return true;
		else return false;
	}
	
	/* Personal method just for key organization.
	 * private int[] generateKeyStream(String message) {
		
		int keyStreamLength = 0;
		for(int i=0; i<message.length(); i++) {
			if(message.charAt(i) >= 'A' && message.charAt(i) <= 'Z') {
				keyStreamLength++;
			}
		}
		int[] keyStream = new int[keyStreamLength];
		for(int j=0; j<keyStream.length; j++) {
			keyStream[j] = getKey();
		}
		return keyStream;
	}*/
}