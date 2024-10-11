import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

import myfileio.MyFileIO;

// TODO: Auto-generated Javadoc
/**
 * The Class EncodeDecode. This is the controller of the Huffman project...
 */
public class EncodeDecode {
	
	/** The encode map. */
	private String[] encodeMap;
	
	/** The huff util. */
	private HuffmanCompressionUtilities huffUtil;
	
	/** The gui. */
	private EncodeDecodeGUI gui;
	
	/** The gw.  This is used to generate the frequency weights if no weights file is specified */
	private GenWeights gw;
	
	/** The bin util. This will be added in part 3 */
	//private BinaryIO binUtil;
	
	/** The input stream for reading a binary file. */
	private BufferedInputStream input;
	
	/** The output stream for writing a binary file. */
	private BufferedOutputStream output;
	
	/**  The array for storing the frequency weights. */
	private int[] weights;
	
	/**  Provides facilities to robustly handle external file IO. */
	private MyFileIO fio;
	
	/** The bin util. */
	private BinaryIO binUtil;
	
	/**
	 * Instantiates a new encode decode.
	 *
	 * @param gui the gui
	 */
	public EncodeDecode (EncodeDecodeGUI gui) {
		this.gui = gui;
		fio = new MyFileIO();
		huffUtil = new HuffmanCompressionUtilities();
		binUtil = new BinaryIO();
		gw = new GenWeights();
	}
	
	/**
	 * Encode. This function will do the following actions:
	 *         1) Error check the inputs
	 * 	       - Perform error checking on the file to encode, using MyFileIO fio.
	 *         - Generate the array of frequency weights - either read from a file in the output/ directory
	 *           or regenerate from the file to encode in the data/ directory
	 *         - Error check the output file...
	 *         Any errors will abort the conversion...
	 *         
	 *         2) set the weights in huffUtils
	 *         3) build the Huffman tree using huffUtils;
	 *         4) create the Huffman codes by traversing the trees.
	 *         
	 *         In part 3, you will call executeEncode to perform the conversion.
	 *
	 * @param fName 	the name of the input file to be encoded
	 * @param bfName 	the name of the binary (compressed) file to be created
	 * @param freqWts 	the name of the file to read for the frequency weights. If blank, or other error,
	 *                  generate the frequency weights from fName.
	 * @param optimize 	if true, ONLY add leaf nodes with non-zero weights to the priority queue
	 */
	void encode(String fName,String bfName, String freqWts, boolean optimize) {
		freqWts = "output/"+freqWts;
		bfName = "output/"+bfName;
		
		File fh = fio.getFileHandle("data/"+fName);
		if (readErrorCheck(fh,true) == false) {
			return;
		}
		File freqFile = fio.getFileHandle(freqWts);
		weights = null;
		if (readErrorCheck(freqFile,false) == false) {
			raiseInputAlert("Weights file is invalid, regenerating it...");
			weights = (gw.readInputFileAndReturnWeights(fName));
		}
		
		File writeFile = fio.getFileHandle(bfName);
		if (writeErrorCheck(writeFile,true) == false) {
			return;
		}
		
		if (weights == null) {
			huffUtil.readFreqWeights(freqFile);
		} else {
			huffUtil.setWeights(weights);
		}
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes(huffUtil.getTreeRoot(), "", 0);
		
		executeEncode(fh,writeFile);
		raiseDoneAlert("Encode complete");
	}
	
	/**
	 * error checking for reading a file
	 * @param fh
	 * @param alerts whether or not to raise alerts
	 * @return
	 */
	private boolean writeErrorCheck(File writeFile, boolean alerts) {
		int status = fio.getFileStatus(writeFile, false);
		
		if (status == MyFileIO.EMPTY_NAME) {
			if (alerts)
				raiseOutputAlert("Empty name");
			return false;
		}
		if (status == MyFileIO.WRITE_EXISTS) {
			if (status == MyFileIO.NO_WRITE_ACCESS) {
				if (alerts)
					raiseOutputAlert("File is not writeable");
				return false;
			}
			if (raiseConfirmAlert("Confirm overwrite?") == false) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * error checking for reading a file
	 * @param fh
	 * @param alerts whether or not to raise alerts
	 * @return
	 */
	private boolean readErrorCheck(File fh, boolean alerts) {
		int status = fio.getFileStatus(fh, true);

		if (status == MyFileIO.EMPTY_NAME) {
			if (alerts)
				raiseInputAlert("Empty name");
			return false;
		}
		if (status == MyFileIO.READ_EXIST_NOT || status == MyFileIO.READ_ZERO_LENGTH) {
			if (alerts)
				raiseInputAlert("File doesn't exist or has no data!");
			return false;
		}
		if (status == MyFileIO.NO_READ_ACCESS) {
			if (alerts)
				raiseInputAlert("Not readable!");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Execute encode. This function will write compressed binary file as part of part 3
	 * 
	 * This functions should:
	 * 1) get the encodeMap from HuffUtils 
	 * 2) for each character in the file, use the encodeMap to find the binary string that will
	 *    represent that character. The bits will accumulate and then be written to the compressed file
	 *    at byte granularity as long as the length is > 0)... 
	 * 3) when the input file is exhausted, write the EOF character, padding with 0's if needed 
	 *    (this should cause the file to be flushed and closed). 
	 *
	 * @param inFile the File object that represents the file to be compressed
	 * @param binFile the File object that represents the compressed output file
	 */
	void executeEncode(File inFile, File binFile) {
		input = binUtil.openInputFile(inFile);
		output = binUtil.openOutputFile(binFile);
		
		encodeMap = huffUtil.getEncodeMap();
		int c;
		try {
			while ((c = input.read()) != -1) {
				String charEncoding = encodeMap[(int)c];
				binUtil.convStrToBin(charEncoding);
			}
			binUtil.writeEOF(encodeMap[0]);
			
			fio.closeStream(input);
		} catch (IOException e) {
			System.out.println(e);
		}
		
		
		
	}
	
	// DO NOT CODE THIS METHOD UNTIL EXPLICITLY INSTRUCTED TO DO SO!!!
	/**
	 * Decode. This function will only be addressed in part 4. It will 
	 *         1) Error check the inputs
	 * 	       - Perform error checking on the file to decode
	 *         - Generate the array of frequency weights - this MUST be provided as a file
	 *         - Error check the output file...
	 *         Any errors will abort the conversion...
	 *         
	 *         2) set the weights in huffUtils
	 *         3) build the Huffman tree using huffUtils;
	 *         4) create the Huffman codes by traversing the trees.
	 *         5) executeDecode
	 *
	 * @param bfName 	the name of the binary file to read
	 * @param ofName 	the name of the text file to write...
	 * @param freqWts the freq wts
	 * @param optimize the optimize
	 */
	void decode(String bfName, String ofName, String freqWts,boolean optimize) {
		File fh = fio.getFileHandle("output/"+bfName);
		
		if (readErrorCheck(fh,true) == false) {
			return;
		}
		File freqFile = fio.getFileHandle("output/"+freqWts);
		if (readErrorCheck(freqFile,false) == false) {
			raiseInputAlert("Weights file is invalid, ABORT");
			return;
		}
		huffUtil.readFreqWeights(freqFile);
		
		File writeFile = fio.getFileHandle("output/"+ofName);
		if (writeErrorCheck(writeFile,true) == false) {
			return;
		}
		huffUtil.buildHuffmanTree(optimize);
		huffUtil.createHuffmanCodes(huffUtil.getTreeRoot(), "", 0);
		encodeMap = huffUtil.getEncodeMap();
		
		try {
			executeDecode(fh,writeFile);
		} catch (IOException e) {
			System.out.println(e);
			return;
		}
		raiseDoneAlert("Decode complete");
		
	}
	
	// DO NOT CODE THIS METHOD UNTIL EXPLICITLY INSTRUCTED TO DO SO!!!
	/**
	 * Execute decode.  - This is part of PART4...
	 * This function performs the decode of the binary(compressed) file.
	 * It will read each byte from the file and convert it to a string of 1's and 0's
	 * This will be appended to any leftover bits from prior conversions.
	 * Starting from the head of the string, decode occurs by traversing the Huffman Tree from the root
	 * until a Leaf node is reached. If a leaf node is reached, the character is written to the output
	 * file, and the corresponding # of bits is removed from the string. If the end of the bit string is reached
	 * with reaching a leaf node, the next byte is processed, and so on...
	 * After completely decoding the file, the output file is flushed and closed
	 *
	 * @param binFile the file object for the binary input file
	 * @param outFile the file object for the binary output file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	void executeDecode(File binFile, File outFile) throws IOException {
		String binStr = "";
		input = binUtil.openInputFile(binFile);
		output = binUtil.openOutputFile(outFile);
		
		int c;
		while ((c = input.read()) != -1) {
			binStr+=binUtil.convBinToStr((byte)c);
			byte decodeResult;
			while ((decodeResult = huffUtil.decodeString(binStr)) != -1) {
				if (decodeResult == 0) {
					fio.closeStream(input);
					fio.closeStream(output);
					return;
				}
				output.write(decodeResult);
				
				binStr = binStr.substring(encodeMap[(int)decodeResult].length());
			}
		}
		//input.close();
		
		fio.closeStream(input);
		fio.closeStream(output);
	}

	/**
	 * Raise input alert - wrapper to call the gui if defined, but
	 *                     send message to Console if not
	 *
	 * @param msg the msg
	 */
	private void raiseInputAlert(String msg) {
		if (gui != null) {
			gui.inputAlert(msg);
		}
		else {
			System.out.println("Input Alert: "+msg);
		}
	}

	/**
	 * Raise output alert - wrapper to call the gui if defined, but
	 *                      send message to Console if not
	 *
	 * @param msg the msg
	 */
	private void raiseOutputAlert(String msg) {
		if (gui != null) {
			gui.outputAlert(msg);
		}
		else {
			System.out.println("Output Alert: "+msg);
		}
	}

	/**
	 * Raise confirm alert - wrapper to call the gui if defined, but
	 *                       send message to Console if not
	 *
	 * @param msg the msg
	 * @return true, if successful
	 */
	private boolean raiseConfirmAlert(String msg) {
		if (gui != null) {
			return gui.confirmAlert(msg);
		}
		else {
			System.out.println("Confirm Alert: "+msg);
			return true;
		}
	}
	
	/**
	 * Raise done alert - wrapper to call the gui if defined, but
	 *                    send message to Console if not
	 *
	 * @param msg the msg
	 */
	private void raiseDoneAlert(String msg) {
		if (gui != null) {
			gui.doneAlert(msg);
		}
		else {
			System.out.println("Done Alert: "+msg);
		}
	}
	
}
