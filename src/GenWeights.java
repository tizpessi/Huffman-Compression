import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import myfileio.MyFileIO;

// TODO: Auto-generated Javadoc
/**
 * The Class GenWeights. This class provides a GUI front-end for the user to 
 * specify a file for generating a character-based frequency analysis, and a 
 * file to write the results for later consumption. This is self-contained class
 * in that it does not adhere to the MVC programming model; all work is done inside
 * this class
 */
public class GenWeights extends Application {
	
	/** The pane. */
	private BorderPane pane = new BorderPane();
    
    /** The gp. */
    private GridPane gp = new GridPane();
    
    /** The btn box. */
    private HBox btnBox = new HBox(15);
    
    /**  The file handles for input (inf) and output (outf). */
    private File inf,outf;
    
    /** The weights - the # of occurences of each character with index 0-127. */
    private int[] weights = new int[128]; 
    
    /** The boolean indicating if the weights array contains valid information. */
    private boolean weightsValid = false;
    
    /**  This flag is set during JUnit testing so that any alerts can be bypassed. */
    private boolean junitTest = false;
    
    /**  fio will allow using all of the methods in MyFileIO for interacting with   text and binary files. */
    private MyFileIO fio;
    
	/**
	 * Instantiates a new GenWeights object. Instantiates fio as an instance of
	 * MyFileIO, and initializes the weights array.
	 */
	public GenWeights() {
		fio = new MyFileIO();
		initWeights();
	}

	/**
	 * Inits the weights array to 0; should be called in the constructor and
	 * as the first step when the Generate button is pressed.
	 */
	private void initWeights() {
		for (int i = 0; i < weights.length; i++) {
			weights[i] = 0;
		}
		weightsValid = false;
	}
	
	/**
	 * Start.
	 *
	 * @param primaryStage the primary stage
	 * @throws Exception the exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		Label hdr = new Label("Generate Frequency Weights");
		hdr.setPrefHeight(40);
		Label lblInput = new Label("Input File: ");
		Label lblOutput = new Label("Output File: ");
		TextField inputFile = new TextField();
		inputFile.setPrefWidth(300);
		TextField outputFile = new TextField();
		outputFile.setPrefWidth(300);
		gp.setVgap(15);
		gp.setHgap(2);

		gp.add(lblInput, 0, 0);
		gp.add(inputFile, 1, 0);
		gp.add(lblOutput, 0, 1);
		gp.add(outputFile, 1, 1);
		pane.setTop(hdr);
		pane.setCenter(gp);
		Button genWt = new Button("Generate");
		Button save = new Button("Save To File");
		
		// TODO #1 create the setOnAction() events to handle button pushes.
		genWt.setOnAction(e -> {generateWeights(inputFile.getText());});
		
		save.setOnAction(e -> {saveWeightsToFile(outputFile.getText());});
		
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll(genWt,save);
		pane.setBottom(btnBox);
		
		Scene scene = new Scene(pane, 400,150);
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	/**
	 * Generate character-based frequency weights. You will write this method,
	 * using the MyFileIO fio instance to create the File object, check, open 
	 * and close the file.
	 * 
	 * NOTE: All input files are located in the data/ directory. infName should ONLY contain
	 * the name of the file and NOT include "data/"... you need to prepend this to the path
	 * when you open the file for reading.
	 * 
	 * You should check the file for the following issues (using the getFileStatus method
	 * in MyFileIO) and take the appropriate action if they occur.
	 * 
	 * a) if the filename is empty, raise a WARNING alert with an appropriate message and
	 *    return.
	 * b) if the file does not exist or is empty, raise a WARNING alert with an appropriate 
	 *    message and return.
	 * c) if the file exists and is not empty, but is not readable, raise a WARNING alert with 
	 *    an appropriate message and return.
	 * 
	 * Each of those cases should be differentiated to the user. 
	 * 
	 * Assuming that the requirements of a, b and c have all been met successfully, 
	 * initialize the weights (yes, again!) and call the readInputFile() to read the file
	 * character by character.
	 *  
	 * Once the input file has been fully processed, you should print the weights to the console.
	 *
	 * @param infName - the name of the file to read; should come from the appropriate
	 *                  text field
	 *              
	 */
	private void generateWeights(String infName) {
		// TODO #2: write this method and any helper methods
		File fh = fio.getFileHandle("data/"+infName);
		int fileStatus = fio.getFileStatus(fh, true);
		if (fileStatus == MyFileIO.EMPTY_NAME) {
			inputAlert("File name is empty!");
			return;
		} else if (fileStatus == MyFileIO.READ_EXIST_NOT || fileStatus == MyFileIO.READ_ZERO_LENGTH) {
			inputAlert("File does not exist or is empty!");
			return;
		} else if (fileStatus == MyFileIO.NO_READ_ACCESS) {
			inputAlert("File is not readable!");
			return;
		} else {
			initWeights();
			readInputFile(fh);
			printWeights();
		}
		return;	
	}
	
	/**
	 * Prints the weights to the console. Non-printing characters (0-31, 127) 
	 * are indicated with [ ], printing characters are displayed to help with debug
	 * 
	 */
	private void printWeights() {
		for (int i = 0; i < weights.length; i++) {
			if ((i < 32) || (i == 127))  
				System.out.println("i:"+i+" [ ] = "+weights[i]);
			else 
				System.out.println("i:"+i+" ("+(char)i+") = "+weights[i]);
		}
	}
	
	/**
	 * Write the character-based frequency data to the specified file, one index per line.
	 * Use the following format:
	 *   print the index and the frequency count separated by a comma.
	 *   DO NOT PRINT THE ACTUAL CHARACTER - as this will cause problems when you
	 *   try to analyze the data with YFSS...
	 *   
	 * Again, you will use the MyFileIO methods to create the File object, check, open and close
	 * the file. 
	 * 
	 * NOTE: all output files should be written to the output/ directory. outfName should only
	 * contain the name of the file, and NOT include "output/"... You will need to prepend 
	 * "output/" to the name when you create the File object.
	 *   
	 * Assuming that there are no errors, raise an INFORMATION alert with an appropriate message
	 * to indicate to the user that the output file was created successfully. Make sure to refer to 
	 * the notes from class today for any more details. Again, the actual writing of the file might be
	 * best done in a separate helper method.
	 *   
	 * You must handle the following error conditions and take the appropriate actions,
	 * as in the generateWeights() method:
	 *   if outfName is blank, raise a WARNING alert with an appropriate message and return.
	 *   if the output file exists but is not writeable, raise a WARNING alear with an appropriate message
	 *   and return.
	 *   if the output file exists and is writeable, raise a CONFIRMATION alert with an appropriate
	 *   message to the user. if they cancel the operation, return; otherwise, continue.
	 *   otherwise, if there is no error (status == MyFileIO.FILE_OK), continue.
	 *   
	 * @param outfName the outf name
	 */
	private void saveWeightsToFile(String outfName) {
		// TODO #3: write this method (and any helper methods)
		File fh = fio.getFileHandle("output/"+outfName);
		int fileStatus = fio.getFileStatus(fh, false);
		if (fileStatus == MyFileIO.EMPTY_NAME) {
			outputAlert("File name is empty!");
			return;
		} else if (fileStatus == MyFileIO.NO_WRITE_ACCESS) {
			outputAlert("File is not writeable!");
			return;
		} else if (fileStatus == MyFileIO.WRITE_EXISTS) {
			if (confirmAlert("Overwrite file?") == false) {
				System.out.println("oOP");
				return;
			}
		}
		if (weightsValid == false) {
			outputAlert("Weights are not valid!");
			return;
		}
		if (fileStatus == MyFileIO.FILE_OK || fileStatus == MyFileIO.WRITE_EXISTS) {
			if (saveWeightsHelperMethod(fh) == true) {
				doneAlert("Sucessfully saved weights to file");
			} else {
				outputAlert("File NOT saved!");
			}
		}
	}
	
	/** write file with weights
	 * 
	 * @param fileHandle
	 */
	private boolean saveWeightsHelperMethod(File fileHandle) {
		try {
			BufferedWriter bf = fio.openBufferedWriter(fileHandle);
			for (int i = 0; i < weights.length; i++) {
				
				bf.write(i+","+weights[i]+",\n");
				
				
			}
			bf.flush();
			bf.close();
		} catch (IOException e) {
			System.out.println("error writing: "+e);
			return false;
		}

		return true;
	}
	
	/**
	 * Input alert.
	 *
	 * @param errorMsg the error msg
	 */
	private void inputAlert(String errorMsg) {
		if (!junitTest) {
			//TODO #4a Write the appropriate alert for input (read) errors
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Error reading file!");
			alert.setContentText(errorMsg);
			alert.showAndWait();
		} else {
			System.out.println("Input File Error: "+errorMsg);
		}
	}

	/**
	 * Output alert.
	 *
	 * @param errorMsg the error msg
	 */
	private void outputAlert(String errorMsg) {
		if (!junitTest) {
			//TODO #4b Write the appropriate alert for output (write) errors
			Alert alert = new Alert(AlertType.WARNING);
			alert.setHeaderText("Error writing file!");
			alert.setContentText(errorMsg);
			alert.showAndWait();
		} else {
			System.out.println("Output File Error: "+errorMsg);
		}
	}

	/**
	 * Confirm alert.
	 *
	 * @param msg the msg
	 * @return true, if successful
	 */
	private boolean confirmAlert(String msg) {	
		if (!junitTest) {
			//TODO #4c Write the appropriate alert to force user to confirm overwrite
			//         of an existing file
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Confirm Writing");
			alert.setContentText(msg);
			
//			ButtonType okButton = new ButtonType("Yes");
//			ButtonType noButton = new ButtonType("No");
//			alert.getButtonTypes().setAll(okButton, noButton);
			
			
			 Optional<ButtonType> result = alert.showAndWait();
			 if (result.isPresent() && result.get() == ButtonType.OK) {
			     return true;
			 }
			
			return false;
		} else {
			System.out.println("Overwriting File! "+msg);
			return true;
		}
	}

	/**
	 * Done alert.
	 *
	 * @param success the success
	 */
	private void doneAlert(String success) {
		if (!junitTest) {
			//TODO #4d Write the appropriate alert to indicate success!
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("Confirm Writing");
			alert.setContentText(success);
			alert.showAndWait();
		} else {
			System.out.println("Saved Weights! "+success);
		}
	}
	
	/**
	 * Read input file. This method is provided as a helper for the readInputAndReturnWeights()
	 *                  method. It expects that the input File handle inf has already been error
	 *                  checked, so while it does catch any IOExceptions, these are UNEXPECTED.
	 *
	 * @param inf       the File handle that represents the input file to be analyzed.
	 */
	private void readInputFile(File inf) {
		BufferedReader data = fio.openBufferedReader(inf);
		if (data==null) {
			System.err.println("Unable to open BufferedReader for file: "+inf.getName());				
		}
		int c;
		int charCnt = 0;
		try {
			while ((c = data.read()) != -1) { // -1 indicates the end of File....
				c = c & 0x7f;
				weights[c] ++;	
				charCnt++;
			}
			weights[0] ++;
		} catch (IOException e) {
			System.err.println("Error in reading file: "+inf.getName());
			e.printStackTrace();
		}	
		weightsValid = true;
		fio.closeFile(data);
	}

	/**
	 * Read input file and return weights. This method is protected so that it can be accessed by
	 *      JUnit tests. DO NOT CHANGE THIS METHOD!!!
	 *
	 * @param infName the inf name
	 * @return the int[]
	 */
	int[] readInputFileAndReturnWeights(String infName) {
		junitTest = true;
		System.out.println("Generating weights for: "+infName);
		generateWeights(infName);
		junitTest = false;
		return weights;
	}		
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
	}

}
