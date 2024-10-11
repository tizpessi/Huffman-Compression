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
class HF_Encode_Test {

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
	@Order(1)
	void test_encode_simple_optimize() {
		enc_dec = new EncodeDecode(null);
		huffUtil = new HuffmanCompressionUtilities();
		gw = new GenWeights();
		String base = "simple";
		//enc_dec.setMac(true);
		checkWeightsFile(base);
		huffUtil.setWeights(new int[128]);
		removeBinaryFile(base);
		enc_dec.encode(base+".txt", base+".bin",base+".csv",true);
		// recreate file handle to test existence of binary file
		binOutf = new File("output/"+base+".bin");
		int[] expectData;
		if (os.contains("Win")) {
			assertTrue(checkBinaryOutput(binOutf, 17));
			expectData = new int[]{189,163,114,143,129,123,78,202};
		} else {
			assertTrue(checkBinaryOutput(binOutf, 16));
			expectData = new int[] {191,201,178,176,139,127,159,202};
		} 
		BufferedInputStream bis = binUtils.openInputFile(binOutf);
		assertTrue(read8Bytes(bis,expectData));
	}
	
		
		
		
	@Test
	@Order(2)
	void test_encode_simple_full() {
		enc_dec = new EncodeDecode(null);
		huffUtil = new HuffmanCompressionUtilities();
		gw = new GenWeights();
		String base = "simple";
		
		checkWeightsFile(base);
		huffUtil.setWeights(new int[128]);
		removeBinaryFile(base+"_full");
		enc_dec.encode(base+".txt", base+"_full.bin",base+".csv",false);
		// recreate file handle to test existence of binary file
		binOutf = new File("output/"+base+"_full.bin");
		int[] expectData;
		if (os.contains("Win")) {
			assertTrue(checkBinaryOutput(binOutf, 17));
			expectData = new int[]{189,163,114,143,129,123,78,202};
		} else {
			assertTrue(checkBinaryOutput(binOutf, 16));
			expectData = new int[] {191,201,178,176,139,127,159,202};
		} 
		BufferedInputStream bis = binUtils.openInputFile(binOutf);
		assertTrue(read8Bytes(bis,expectData));
	}
	
	@Test
	@Order(3)
	void test_encode_GEAH_optimize() {
		enc_dec = new EncodeDecode(null);
		huffUtil = new HuffmanCompressionUtilities();
		gw = new GenWeights();
		String base = "Green Eggs and Ham";
		
		checkWeightsFile(base);
		huffUtil.setWeights(new int[128]);
		removeBinaryFile(base);
		enc_dec.encode(base+".txt", base+".bin",base+".csv",true);
		// recreate file handle to test existence of binary file
		binOutf = new File("output/"+base+".bin");
		int[] expectData;
		if (os.contains("Win")) {
			assertTrue(checkBinaryOutput(binOutf, 2047));
			expectData = new int[]{87,104,240,173,20,48,173,30};
		} else {
			assertTrue(checkBinaryOutput(binOutf, 1920));
			expectData = new int[] {96,243,61,94,107,234,243,24};
		} 
		BufferedInputStream bis = binUtils.openInputFile(binOutf);
		assertTrue(read8Bytes(bis,expectData));
		
	}
	
	@Test
	@Order(4)
	void test_encode_TCITH_optimize() {
		enc_dec = new EncodeDecode(null);
		huffUtil = new HuffmanCompressionUtilities();
		gw = new GenWeights();
		String base = "The Cat in the Hat";
		
		checkWeightsFile(base);
		huffUtil.setWeights(new int[128]);
		removeBinaryFile(base);
		enc_dec.encode(base+".txt", base+".bin",base+".csv",true);
		// recreate file handle to test existence of binary file
		binOutf = new File("output/"+base+".bin");
		int[] expectData;
		if (os.contains("Win")) {
			assertTrue(checkBinaryOutput(binOutf, 4297));
			expectData = new int[]{201,127,173,203,196,227,202,179};
		} else {
			assertTrue(checkBinaryOutput(binOutf, 4047));
			expectData = new int[] {218,142,3,63,52,159,59,222};
		} 
		BufferedInputStream bis = binUtils.openInputFile(binOutf);
		assertTrue(read8Bytes(bis,expectData));		
	}
	
	@Test
	@Order(5)
	void test_encode_HPATS_optimize() {
		enc_dec = new EncodeDecode(null);
		huffUtil = new HuffmanCompressionUtilities();
		gw = new GenWeights();
		String base = "Harry Potter and the Sorcerer";
		
		checkWeightsFile(base);
		huffUtil.setWeights(new int[128]);
		removeBinaryFile(base);
		enc_dec.encode(base+".txt", base+".bin",base+".csv",true);
		// recreate file handle to test existence of binary file
		binOutf = new File("output/"+base+".bin");
		int[] expectData;
		if (os.contains("Win")) {
			assertTrue(checkBinaryOutput(binOutf, 259670));
			expectData = new int[]{97,6,119,242,131,204,131,240};
		} else {
			assertTrue(checkBinaryOutput(binOutf, 253916));
			expectData = new int[] {101,6,120,117,3,204,131,240};
		} 
		BufferedInputStream bis = binUtils.openInputFile(binOutf);
		assertTrue(read8Bytes(bis,expectData));

	}
	
	@Test
	@Order(6)
	void test_encode_WAP_optimize() {
		enc_dec = new EncodeDecode(null);
		huffUtil = new HuffmanCompressionUtilities();
		gw = new GenWeights();
		String base = "warAndPeace";
		
		checkWeightsFile(base);
		huffUtil.setWeights(new int[128]);
		removeBinaryFile(base);
		enc_dec.encode(base+".txt", base+".bin",base+".csv",true);
		// recreate file handle to test existence of binary file
		binOutf = new File("output/"+base+".bin");
		int[] expectData;
		if (os.contains("Win")) {
			assertTrue(checkBinaryOutput(binOutf, 1875089));
			expectData = new int[]{154,152,204,255,59,34,47,172};
		} else {
			assertTrue(checkBinaryOutput(binOutf, 1816470));
			expectData = new int[] {178,152,203,255,186,226,57,172};
		} 
		BufferedInputStream bis = binUtils.openInputFile(binOutf);
		assertTrue(read8Bytes(bis,expectData));
		
	}
	
	@Test
	@Order(7)
	void test_encode_WAP_full() {
		enc_dec = new EncodeDecode(null);
		huffUtil = new HuffmanCompressionUtilities();
		gw = new GenWeights();
		String base = "warAndPeace";
		
		checkWeightsFile(base);
		huffUtil.setWeights(new int[128]);
		removeBinaryFile(base+"_full");
		enc_dec.encode(base+".txt", base+"_full.bin",base+".csv",false);
		// recreate file handle to test existence of binary file
		binOutf = new File("output/"+base+"_full.bin");
		int[] expectData;
		if (os.contains("Win")) {
			assertTrue(checkBinaryOutput(binOutf, 1875089));
			expectData = new int[]{154,152,204,255,59,34,47,172};
		} else {
			assertTrue(checkBinaryOutput(binOutf, 1816470));
			expectData = new int[] {178,152,203,255,186,226,57,172};
		} 
		BufferedInputStream bis = binUtils.openInputFile(binOutf);
		assertTrue(read8Bytes(bis,expectData));
	}
	
	private boolean read8Bytes(BufferedInputStream bis, int[] expect) {
		int [] actual = new int[8];
		boolean status = true;
		try {
			for (int i = 0; i < 8; i++) {
				actual[i] = (bis.read() & 0xff);
				status = status && (actual[i] == expect[i]);
//				System.out.printf("%d,",actual[i]);
			}
			bis.close();
		} catch (IOException e) {
			System.out.println("IO Exception occurred while reading binary file\n");
			e.printStackTrace();
		}
		return status;
	}

	private boolean checkBinaryOutput(File binOutf, int expectedSize) {
		if (!binOutf.exists()) {
			System.out.println("   Encode did not create file "+binOutf.getPath());
			return false;
		}
		long bfLen = binOutf.length();
		if (bfLen == 0) {
			System.out.println("   Encode created empty file "+binOutf.getPath());
			return false;			
		} else if (bfLen != expectedSize) {
			System.out.println("   Encode created file "+binOutf.getPath()+", but file size does not match expectations");
			System.out.println("   Expected Length: "+expectedSize);
			System.out.println("   Actual Length:   "+bfLen);
			return false;			
		}
		return true;
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
	
	private boolean removeBinaryFile(String base) {
		binOutf = new File("output/"+base+".bin");

		System.out.println("Checking if file exists: "+binOutf.getPath());
		if (binOutf.exists()) {
			if (binOutf.delete()) {
				System.out.println("Deleted file: "+binOutf.getPath());
				return true;
			} else {
				return false;
			}
		}
		return true;
	}	
		
}
