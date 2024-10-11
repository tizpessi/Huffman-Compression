import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(OrderAnnotation.class)
class HF_Decode_Test {

	EncodeDecode enc_dec;
	HuffmanCompressionUtilities huffUtil;
	GenWeights gw;
    private static boolean noCR = false; 
    int[] weights = new int[128];
    private static String os;
    
	private BufferedOutputStream bOutS;
	private BufferedInputStream bInS;
	private File binInf;
	private File binOutf;
	private BinaryIO binUtils;
	private String[] bin2nib = {"0000","0001","0010","0011","0100","0101","0110","0111",
            "1000","1001","1010","1011","1100","1101","1110","1111",};
	
    private static String getOperatingSystem() {
    	os = System.getProperty("os.name");
    	return os;
    }

    @BeforeAll
	static void setUpBeforeClass() throws Exception {
    	getOperatingSystem();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		binUtils = new BinaryIO();
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	

	@Test
	@Order(10)
	void test_decode_simple_optimize() {
		enc_dec = new EncodeDecode(null);
		huffUtil = new HuffmanCompressionUtilities();
		gw = new GenWeights();
		String base = "simple";
		
		checkWeightsFile(base);
		huffUtil.setWeights(new int[128]);
		assertTrue(removeOutputFiles(base));
		enc_dec.encode(base+".txt", base+".bin",base+".csv",true);
		enc_dec.decode(base+".bin", base+".txt",base+".csv",true);
		// recreate file handle to test existence of binary file
		File outFh = new File("output/"+base+".txt");
		assertTrue(outFh.exists());
		File origFh = new File("data/"+base+".txt");
		assertTrue(compareFiles(origFh, outFh));
		assertTrue(removeOutputFiles(base));
	}

	@Test
	@Order(11)
	void test_decode_simple_full() {
		enc_dec = new EncodeDecode(null);
		huffUtil = new HuffmanCompressionUtilities();
		gw = new GenWeights();
		String base = "simple";
		
		checkWeightsFile(base);
		huffUtil.setWeights(new int[128]);
		assertTrue(removeOutputFiles(base+"_full"));
		enc_dec.encode(base+".txt", base+"_full.bin",base+".csv",false);
		enc_dec.decode(base+"_full.bin", base+"_full.txt",base+".csv",false);
		// recreate file handle to test existence of binary file
		File outFh = new File("output/"+base+"_full.txt");
		assertTrue(outFh.exists());
		File origFh = new File("data/"+base+".txt");
		assertTrue(compareFiles(origFh, outFh));	
		assertTrue(removeOutputFiles(base+"_full"));
	}
	
	@Test
	@Order(12)
	void test_decode_GEAH_optimize() {
		enc_dec = new EncodeDecode(null);
		huffUtil = new HuffmanCompressionUtilities();
		gw = new GenWeights();
		String base = "Green Eggs and Ham";
		
		checkWeightsFile(base);
		huffUtil.setWeights(new int[128]);
		assertTrue(removeOutputFiles(base));
		enc_dec.encode(base+".txt", base+".bin",base+".csv",true);
		enc_dec.decode(base+".bin", base+".txt",base+".csv",true);
		// recreate file handle to test existence of binary file
		File outFh = new File("output/"+base+".txt");
		assertTrue(outFh.exists());
		File origFh = new File("data/"+base+".txt");
		assertTrue(compareFiles(origFh, outFh));	
		assertTrue(removeOutputFiles(base));
	}
	
	@Test
	@Order(13)
	void test_decode_TCITH_optimize() {
		enc_dec = new EncodeDecode(null);
		huffUtil = new HuffmanCompressionUtilities();
		gw = new GenWeights();
		String base = "The Cat in the Hat";
		
		checkWeightsFile(base);
		huffUtil.setWeights(new int[128]);
		assertTrue(removeOutputFiles(base));
		enc_dec.encode(base+".txt", base+".bin",base+".csv",true);
		enc_dec.decode(base+".bin", base+".txt",base+".csv",true);
		// recreate file handle to test existence of binary file
		File outFh = new File("output/"+base+".txt");
		assertTrue(outFh.exists());
		File origFh = new File("data/"+base+".txt");
		assertTrue(compareFiles(origFh, outFh));	
		assertTrue(removeOutputFiles(base));
	}
	
	@Test
	@Order(14)
	void test_decode_HPATS_optimize() {
		enc_dec = new EncodeDecode(null);
		huffUtil = new HuffmanCompressionUtilities();
		gw = new GenWeights();
		String base = "Harry Potter and the Sorcerer";
		
		checkWeightsFile(base);
		huffUtil.setWeights(new int[128]);
		assertTrue(removeOutputFiles(base));
		enc_dec.encode(base+".txt", base+".bin",base+".csv",true);
		enc_dec.decode(base+".bin", base+".txt",base+".csv",true);
		// recreate file handle to test existence of binary file
		File outFh = new File("output/"+base+".txt");
		assertTrue(outFh.exists());
		File origFh = new File("data/"+base+".txt");
		assertTrue(compareFiles(origFh, outFh));	
		assertTrue(removeOutputFiles(base));
	}
	
	@Test
	@Order(15)
	void test_decode_WAP_optimize() {
		enc_dec = new EncodeDecode(null);
		huffUtil = new HuffmanCompressionUtilities();
		gw = new GenWeights();
		String base = "warAndPeace";
		
		checkWeightsFile(base);
		huffUtil.setWeights(new int[128]);
		assertTrue(removeOutputFiles(base));
		enc_dec.encode(base+".txt", base+".bin",base+".csv",true);
		enc_dec.decode(base+".bin", base+".txt",base+".csv",true);
		// recreate file handle to test existence of binary file
		File outFh = new File("output/"+base+".txt");
		assertTrue(outFh.exists());
		File origFh = new File("data/"+base+".txt");
		assertTrue(compareFiles(origFh, outFh));	
		assertTrue(removeOutputFiles(base));
	}
	
	@Test
	@Order(16)
	void test_decode_WAP_full() {
		enc_dec = new EncodeDecode(null);
		huffUtil = new HuffmanCompressionUtilities();
		gw = new GenWeights();
		String base = "warAndPeace";
		
		checkWeightsFile(base);
		huffUtil.setWeights(new int[128]);
		assertTrue(removeOutputFiles(base+"_full"));
		enc_dec.encode(base+".txt", base+"_full.bin",base+".csv",false);
		enc_dec.decode(base+"_full.bin", base+"_full.txt",base+".csv",false);
		// recreate file handle to test existence of binary file
		File outFh = new File("output/"+base+"_full.txt");
		assertTrue(outFh.exists());
		File origFh = new File("data/"+base+".txt");
		assertTrue(compareFiles(origFh, outFh));	
		assertTrue(removeOutputFiles(base+"_full"));
	}

	private boolean compareFiles(File fh1, File fh2) {
		BufferedInputStream inFh1 = null;
		BufferedInputStream inFh2 = null;
		int byte_cnt = 0;
		System.out.println("Comparing files "+fh1.getPath()+" and "+fh2.getPath());
	
		try {
			inFh1 = new BufferedInputStream(new FileInputStream(fh1));
			inFh2 = new BufferedInputStream(new FileInputStream(fh2));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		boolean EOF = false;
		boolean match = true;
		
		while (!EOF) {
			int byte_fh1 = readByteFromInStream(inFh1);
			int byte_fh2 = readByteFromInStream(inFh2);
			byte_cnt++;
			EOF = byte_fh1 == -1;		
			match = byte_fh1 == byte_fh2;
			if (EOF && !match) {
				if (byte_fh2 == 13)   // is there a CR? if so, must be followed by a \n
				   byte_fh2 = readByteFromInStream(inFh2);
				if (byte_fh2 == 10)   // is there a \n?
					byte_fh2 = readByteFromInStream(inFh2); // should be EOF now
				match = byte_fh1 == byte_fh2;
			}
			if (!match) { 
				System.out.println("Mismatch detected between file "+fh1.getPath()+" and file "+fh2.getPath()+" at byte "+byte_cnt);
			    System.out.println("   Expected byte value = "+byte_fh1+"("+((char) byte_fh1)+")");
			    System.out.println("   Actual byte value   = "+byte_fh2+"("+((char) byte_fh2)+")");
			    closeInStream(inFh1);
			    closeInStream(inFh2);
				return false;
			}

		}	
	    closeInStream(inFh1);
	    closeInStream(inFh2);
		
		return true;
	}

	private void closeInStream(BufferedInputStream inS) {
		try {
			inS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	private boolean checkWeightsFile(String base) {
		File wtsFh = new File("output/"+base+".csv");
		System.out.println("Generating weights file: "+ base+".csv");

		System.out.println("Checking if file exists: "+wtsFh.getPath());
		if (wtsFh.exists()) 
			wtsFh.delete();
		
		weights = gw.readInputFileAndReturnWeights(base+".txt");
		writeWeightsFile(wtsFh);
		return  (wtsFh.exists()); 
	}	
	
	private void writeWeightsFile(File outf) {
		String line;
		try {
			DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outf)));
			for (int i = 0; i < weights.length; i++ ) {
					line = i+","+weights[i]+",\n";
				output.writeBytes(line);
			}
			output.flush();
			output.close();
		} catch (IOException e) {
			System.err.println("Error in writing file: "+outf.getName());
			e.printStackTrace();
		}
	}
	
	private boolean removeOutputFiles(String base) {
		boolean status = true;
		binOutf = new File("output/"+base+".txt");	
		System.out.println("Checking if file exists: "+binOutf.getPath());
		if (binOutf.exists()) {
			if (binOutf.delete()) {
				System.out.println("Deleted file: "+binOutf.getPath());
			} else {
				status = false;
			}
		}
		if (status) {
			binOutf = new File("output/"+base+".bin");	
			System.out.println("Checking if file exists: "+binOutf.getPath());
			if (binOutf.exists()) {
				if (binOutf.delete()) {
					System.out.println("Deleted file: "+binOutf.getPath());
				} else {
					status = false;
				}
			}
		}
		return status;
	}	
	
	private byte readByteFromInStream(BufferedInputStream bInS) {
		byte readByte=0;
		try {
			 readByte = (byte) bInS.read();
		} catch (IOException e) {
			System.out.println("Caught Exception while trying to read a byte");
			e.printStackTrace();
		}
		return readByte;
	}
		
}
