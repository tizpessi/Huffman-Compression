import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import myfileio.MyFileIO;

// TODO: Auto-generated Javadoc
/**
 * The Class BinaryIO.
 */
public class BinaryIO {
	
	/** The binary String of 1's and 0's to be converted to a Byte. Must be a field to be persistent */
	private String binStr;
	
	/** The output stream for writing the binary (compressed) file . */
	private BufferedOutputStream binOutput;
	
	/** The input stream for reading a binary file. */
	private BufferedInputStream binInput;

	/** fio provides access to File handling methods. */
	private MyFileIO fio;
	
	/**
	 * Instantiates a new binary IO.
	 */
	public BinaryIO() {
		binStr = "";
		fio = new MyFileIO();
	}
	
	// Getters for supporting testing and execution
	/**
	 * Gets the binary String.
	 *
	 * @return the binStr
	 */
	public String getBinStr() {
		return binStr;
	}

	/**
	 * Gets the binary output Stream - for testing purposes ONLY
	 *
	 * @return the binOutput
	 */
	BufferedOutputStream getBinOutput() {
		return binOutput;
	}

	/**
	 * Gets the binary input stream - for testing purposes ONLY
	 *
	 * @return the binInput
	 */
	BufferedInputStream getBinInput() {
		return binInput;
	}
	
	/**
	 * Converts a string of 1's and 0's to one or more bytes. 
	 * 1) Appends the string encoding of the current character to the current string. 
	 * 2) while the string length is >= 8
	 *    convert the first 8 characters to the equivalent byte value
	 *    write the byte to a file
	 *    remove the first 8 characters
	 *    repeat as necessary
	 *
	 * @param inBinStr the incoming binary string for the current character
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void convStrToBin(String inBinStr) throws IOException {
		// TODO: Write this method
	
		binStr += inBinStr; 
		while (binStr.length() >= 8) {
			byte b = bitsToByte(binStr.substring(0,8));
			binOutput.write(b);

			binStr = binStr.substring(8);
			
		}
	}
	
	private byte bitsToByte (String in) {
		int decimal = 0;
		for (int i = 0; i < 8; i++) {
			decimal = (decimal << 1) + (in.charAt(i)-'0');
		}
		return (byte)decimal;
		//return (byte)(Integer.parseInt(in,2));
	}
	
	
	/**
	 * Convert a byte value into a string of 1's and 0's (MSB to LSB).
	 *
	 * @param aByte the byte to convert
	 * @return the binary string of 1's and 0's that represents aByte
	 */
	public String convBinToStr(byte aByte) {
		String str = "";
		int mask = 0x80;
		for (int i = 0; i < 8; i++) {
			str += ((mask & aByte)>0)?"1":"0";
			mask = mask >>> 1;
		}
		
		return str;
	}
	
	/**
	 * Write EOF. 
	 * 1) Append the EOF character to the stream of 1's and 0;s
	 * 2) While the (length of the stream)%8 != 0, append a "0"
	 * 3) Convert the string to 1 or more bytes (until consumed)
	 * 4) flush and close the file;
	 * 5) make sure to clear binStr (in case converting another file later)
	 *
	 * @param EOF_binStr the binary string of 1's and 0's that represents the EOF character
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void writeEOF(String EOF_binStr) throws IOException {
		// TODO: write this method
		binStr+=EOF_binStr;
		while (binStr.length()%8 != 0) {
			binStr+= "0";
		}
		convStrToBin("");
		fio.closeStream(binOutput);
		binStr = "";
	}
	

	
	/**
	 * Open binary output file.
	 *
	 * @param binFile the bin file
	 * @return the buffered output stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public BufferedOutputStream openOutputFile(File binFile) {
		binOutput = fio.openBufferedOutputStream(binFile);
		return binOutput;
	}
	
	/**
	 * Open binary input file.
	 *
	 * @param binFile the bin file
	 * @return the buffered input stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public BufferedInputStream openInputFile(File binFile) {
		binInput = fio.openBufferedInputStream(binFile);
		return binInput;
	}

}

