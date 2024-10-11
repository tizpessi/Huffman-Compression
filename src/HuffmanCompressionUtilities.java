import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

import myfileio.MyFileIO;

// TODO: Auto-generated Javadoc
/**
 * The Class HuffmanCompressionUtilities.
 */
public class HuffmanCompressionUtilities {
	
	/** The queue. */
	private PriorityQueue<HuffmanTreeNode> queue;
	
	/** The root. */
	private HuffmanTreeNode root;
	
	/**  The encode map - this will map a character to the bit string that will replace it. */
	private String[] encodeMap;
	
	/** The str. This is used to print the tree structure for testing purposes */
	private String str;
	
	/** The frequency weights. */
	private int[] weights;

	/** The instance of MyFileIO - fio. */
	private MyFileIO fio;
	/**
	 * Instantiates a new huffman compression utilities.
	 */
	public HuffmanCompressionUtilities() {
		//TODO: 
		// Initialize Priority Queue with an appropriate comparator...
		// Initialize root to null, and all other private variables
		// including fio.
		
		queue = new PriorityQueue<HuffmanTreeNode>(HuffmanTreeNode.compareWeightOrd);
		root = null;
		encodeMap = new String[128];
		weights = new int[128];
		fio = new MyFileIO();
		
	}
	
	/**
	 * Gets the tree root.
	 *
	 * @return the tree root
	 */
	public HuffmanTreeNode getTreeRoot() {
		//TODO - write this method
		return root;
	}
	
	/**
	 * Gets the encode map.
	 *
	 * @return the encode map
	 */
	public String[] getEncodeMap() {
		//TODO - write this method
		return encodeMap; 
	}
	
	/**
	 * Read freq weights from a file in the output/ directory
	 * You can assume that this file has already been error checked.
	 * Use fio and a BufferedReader to read the line, split into
	 * fields, and initialize the weights array/
	 *
	 * @param inf the inf
	 * @return the int[] that represent the weights....
	 */
	public int[] readFreqWeights(File inf) {
		//TODO - write this method
		BufferedReader data = fio.openBufferedReader(inf);
		if (data==null) {
			System.err.println("Unable to open BufferedReader for file: "+inf.getName());				
		}
		String c;

		weights = new int[128];
		try {
			while ((c = data.readLine()) != null) {
				String[] split = c.split("\\,");
				weights[Integer.parseInt(split[0])] = Integer.parseInt(split[1]);
			}
			//weights[0] ++;
		} catch (IOException e) {
			System.err.println("Error in reading file: "+inf.getName());
			e.printStackTrace();
		}	
		fio.closeFile(data);
		return weights; // remove this when written
	}			

	/**
	 * Initialize huffman queue from the weights array.
	 *
	 * @param minimize - when true, only add:
	 *     indexes with non-zero weights to the queue
	 *     index 0 (the EOF character) to the queue
	 */
	void initializeHuffmanQueue(boolean minimize) {
		//TODO - write this method
		for (int i = 0; i < weights.length; i++) {
			if (minimize && (!(weights[i] > 0 || i == 0))) {
				continue;
			}
			queue.add(new HuffmanTreeNode(i,weights[i]));
			
		}
	}
	
	/**
	 * Sets the weights.
	 *
	 * @param weights the new weights
	 */
	public void setWeights(int[] weights) {
		//TODO - write this method
		this.weights = weights;
	}
	
	/**
	 * Builds the huffman tree. Make sure to:
	 * 1) initialize root to null (cleanup any prior conversions)
	 * 2) re-initialize the encodeMap
	 * 3) initialize the queue
	 * 4) build the tree:
	 *    while the queue is not empty:
	 *       pop the head of the queue into the left HuffmanTreeNode.
	 *       if the queue is empty - set root = left, and return;
	 *       pop the head of the queue into the right HuffmanTreeNode
	 *       create a new non-leaf HuffmanTreeNode whose children are left and right,
	 *       and whose weight is the sum of the weight of the left and right children
	 *       add the new node back to the queue.
	 * 
	 * It is assumed that the weights have been initialized prior
	 * to calling this method.
	 *
	 * @param minimize - This is just passed to the method to initialize the queue.
	 */
	public void buildHuffmanTree(boolean minimize) {
		HuffmanTreeNode left, right;
		//TODO: write this method
		root = null;
		encodeMap = new String[128];
		initializeHuffmanQueue(minimize);
		
		while ((!queue.isEmpty())) {
			left = queue.poll();
			if (queue.isEmpty()) {
				root = left;
				return;
			}
			right = queue.poll();
			int newWeight = left.getWeight() + right.getWeight();
			HuffmanTreeNode newNode = new HuffmanTreeNode(newWeight,left,right);
			queue.add(newNode);
		}
		
		
	}
	
	/**
	 * Prints the node info for debugging purposes.
	 *
	 * @param level the level
	 * @param ord the ord
	 * @param aChar the a char
	 * @param code the code
	 */
	private void printNodeInfo(int level, int ord, char aChar, String code) {
		if (ord <32 ) {
			System.out.println("Level: "+level+ "   Ord: "+ord+"[ ] = "+code);
		} else {
			System.out.println("Level: "+level+ "   Ord: "+ord+"("+aChar+") = "+code);
		}
		
	}
	
	/**
	 * Creates the huffman codes. Starting at the root node, recursively traverse the tree to create 
	 * the code. Moving to a left child adds "0" to the code, moving to the right child adds "1".
	 * If the node is a leaf, then set the appropriate entree in the encodeMap to the accumulated 
	 * code. You should never encounter a null pointer in this process... but good to check..
	 *
	 * @param node the node
	 * @param code the code
	 * @param level the level
	 */
	public void createHuffmanCodes(HuffmanTreeNode node, String code, int level) {
		//TODO: write this method
		if (node.isLeaf()) {
			encodeMap[node.getOrdValue()] = code;
			return;
		}
		if (node.getLeft() != null) {
			createHuffmanCodes(node.getLeft(),code+"0",level+1);
		}
		if (node.getRight() != null) {
			createHuffmanCodes(node.getRight(),code+"1",level+1);
		}
		
		
		
	}
	
	/**
	 * Prints the huffman tree for debugging and test purposes...
	 * Uses a preorder traversal
	 *
	 * @param root the root
	 * @param level the level
	 */
	public void printHuffmanTree(HuffmanTreeNode root, int level) {
		if (root == null) {
			return;
		} 
		
		if (level == 0) {
			str = "";
		}
		
		if (root.isLeaf()) {
			if (root.getOrdValue() < 32) {
				str += level+"l"+root.getOrdValue();  // lower case "l" means non-printing char
			} else {
				str += level +"L"+root.getCharValue();  // upper case "L" means printing char
			}
		} else {
			str += level+"N";      // Not a character
 
			if ((root.getLeft() == null) && (root.getRight() == null)) {
				return;
			}
		
			str += ('(');
		    printHuffmanTree(root.getLeft(),level+1);
		    str += ')';
		    
		    if (root.getRight() != null) {
		    	str += ('(');
		    	printHuffmanTree(root.getRight(),level+1);
		    	str += (')');
		    }
		
		}
		
	}
	
	// THIS IS PART OF DECODE....
	/**
	 * Traverse tree, based upon the passed in binary String (inStr).
	 * 
	 * The algorithm recursively traverses the tree based on the sequence of bits 
	 * until either a leaf node is encountered and the char is returned or the string of bits
	 * has been consumed without finding a character (returns -1);
	 * 
	 * This is a recursive routine - index is passed in to read the appropriate "bit" of
	 * inStr and determine whether to left or right, or return the character if this is a
	 * leaf node...
	 * 
	 * @param root the root
	 * @param inStr the in str
	 * @param index the index
	 * @return the byte
	 */
	private byte traverseTree(HuffmanTreeNode root, String inStr, int index) {
		//TODO - write this method
		
		if (root.isLeaf()) {
			return (byte)(root.getOrdValue());
		}
		if (index >= inStr.length())
			return -1;
		
		if (inStr.charAt(index) == '1') {
			if (root.getRight() != null) {
				return traverseTree(root.getRight(),inStr,index+1);
				
			} else {
				return -1;
			}
		} else {
			if (root.getLeft() != null) {
				return traverseTree(root.getLeft(),inStr,index+1);
				
			} else {
				return -1;
			}
		}
		
		
		//return -1; // remove when completed.
	}
	
	/**
	 * Decode string.
	 * Algorithm:
	 *  If the input string is empty, return -1
	 *  Save a copy of the binary string
	 *  Traverse the tree with the binary string
	 *  If no character found, restore the binary string from the copy
	 *  Return the decoded character if found, -1 if not
	 *
	 * @param inStr the string of Binary characters to be processed
	 * @return the byte
	 */
	public byte decodeString(String inStr) {
		//TODO - write this method
		if (inStr == "") 
			return -1;
		byte result = traverseTree(root, inStr, 0);
		return result; // remove when completed.
	}
		
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return str;
	}
} 
