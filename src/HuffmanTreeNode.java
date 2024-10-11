import java.util.Comparator;

// TODO: Auto-generated Javadoc
/**
 * The Class HuffmanTreeNode. 
 */
public class HuffmanTreeNode {
	
	/** The weight. */
	private int weight;
	
	/** The ord value. */
	private int ordValue;
	
	/** The char value. */
	private char charValue;
	
	/** STATIC version of the id used to initialize the instance id to a unique value for every node. */
	private static int ID=0;
	
	/** The unique id for each instance - required to ensure consistent sorting in compareOrdWeights. */
	private int id;
	
	/** The left. */
	private HuffmanTreeNode left;
	
	/** The right. */
	private HuffmanTreeNode right;
	
	/**
	 * Instantiates a new huffman tree node. This constructor is used to generate unconnected leaf 
	 * nodes.
	 *
	 * @param ordValue the ord value (the integer value of the character)
	 * @param weight the weight (the frequency of occurrence of the character)
	 */
	public HuffmanTreeNode(int ordValue, int weight) {
		this.weight = weight;
		this.ordValue = ordValue;
		this.charValue = (char) ordValue;
		left = null;
		right = null;
		id = ID++;
	}
	
	/**
	 * Instantiates a new huffman tree node. This constructor is used to connect two nodes
	 * together, which may or may not be leaf nodes. This node will have two nodes - left and right.
	 * The weight of this node is the sum of the weights of the left and right nodes. Since this node
	 * is not a leaf node, the ordValue is -1 and the charValue is 0;
	 *
	 * @param weight the weight
	 * @param left the left
	 * @param right the right
	 */
	public HuffmanTreeNode(int weight, HuffmanTreeNode left, HuffmanTreeNode right) {
		this.weight = weight;
		this.ordValue = -1;
		this.charValue = 0;
		this.left = left;
		this.right = right;
		id = ID++;
	}
	
	/**
	 * Reset ID. If you are going to rebuild the tree - you should reset the ID....
	 */
	public void resetID() {
		ID = 0;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * Sets the weight.
	 *
	 * @param weight the new weight
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	/**
	 * Gets the ord value.
	 *
	 * @return the ord value
	 */
	public int getOrdValue() {
		return ordValue;
	}

	/**
	 * Sets the ord value.
	 *
	 * @param ordValue the new ord value
	 */
	public void setOrdValue(int ordValue) {
		this.ordValue = ordValue;
	}

	/**
	 * Gets the char value.
	 *
	 * @return the char value
	 */
	public char getCharValue() {
		return charValue;
	}

	/**
	 * Sets the char value.
	 *
	 * @param charValue the new char value
	 */
	public void setCharValue(char charValue) {
		this.charValue = charValue;
	}

	/**
	 * Gets the left.
	 *
	 * @return the left
	 */
	public HuffmanTreeNode getLeft() {
		return left;
	}

	/**
	 * Sets the left.
	 *
	 * @param left the new left
	 */
	public void setLeft(HuffmanTreeNode left) {
		this.left = left;
	}

	/**
	 * Gets the right.
	 *
	 * @return the right
	 */
	public HuffmanTreeNode getRight() {
		return right;
	}

	/**
	 * Sets the right.
	 *
	 * @param right the new right
	 */
	public void setRight(HuffmanTreeNode right) {
		this.right = right;
	}
	
	/**
	 * Checks if is leaf.
	 *
	 * @return true, if is leaf
	 */
	public boolean isLeaf() {
		return(ordValue != -1);
	}

	/** The comparator used to sort nodes by increasing weight then by 
	 *  increasing ordinal value. 
	 * 
	 *  This will be used by the Priority Queue to determine the ordering of 
	 *  nodes in the queue.
	 */  
	public static Comparator<HuffmanTreeNode> compareWeightOrd = new Comparator<HuffmanTreeNode>() {
		@Override
		public int compare(HuffmanTreeNode ht1, HuffmanTreeNode ht2) {
			// TODO: write this method
			if (ht1.getWeight() == ht2.getWeight()) {
				if (ht1.getOrdValue() == ht2.getOrdValue()) {
					if (ht1.getId() == ht2.getId()) {
						return 0;
					}
					return ht1.getId() - ht2.getId();
				}
				return ht1.getOrdValue() - ht2.getOrdValue();
			}
			return ht1.getWeight() - ht2.getWeight();//ht1.getWeight() > ht2.getWeight()? 1:-1;
			
		}
	};
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		return "ord = " + this.ordValue + "   weight = " + this.weight;
	}
	
}
