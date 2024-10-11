import java.io.File;
import java.util.Optional;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

// TODO: Auto-generated Javadoc
/**
 * The Class EncodeDecodeGUI. This is the view for the Huffman Project.
 * This should be sufficient for you to implement the required aspects of the project.
 * You can choose to modify the layout/appearance, but you should not change the 
 * textfields, buttons or checkboxes. All error checking should be done in the controller 
 * (EncodeDecode class), but any alerts should be handled here.
 */
public class EncodeDecodeGUI extends Application {
	
	/** The pane. */
	private BorderPane pane = new BorderPane();
    
    /** The gp. */
    private GridPane gp = new GridPane();
    
    /** The btn box. */
    private HBox btnBox = new HBox(15);
    
    /** The outf. */
    private File inf,outf;
    
    /** The weights. */
    private int[] weights = new int[128]; 
    
    /** The ck optimize. */
    private CheckBox ckOptimize;
    
    /** The hf. */
    private EncodeDecode hf;
    
    /** The optimize. */
    private boolean optimize;
    
    /**
     * Instantiates a new encode decode GUI.
     */
    public EncodeDecodeGUI() {
    	hf = new EncodeDecode(this);
    	optimize = true;
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
		Label hdr = new Label("Huffman File Encode/Decode");
		hdr.setPrefHeight(40);
		Label lblInput = new Label("Text File: ");
		Label lblOutput = new Label("Compressed File: ");
		Label lblWeights = new Label("Weights File");
		TextField txtFile = new TextField();
		txtFile.setPrefWidth(300);
		TextField binFile = new TextField();
		binFile.setPrefWidth(300);
		TextField weightsFile = new TextField();
		weightsFile.setPrefWidth(300);
		gp.setVgap(15);
		gp.setHgap(2);

		gp.add(lblInput, 0, 0);
		gp.add(txtFile, 1, 0);
		gp.add(lblOutput, 0, 1);
		gp.add(binFile, 1, 1);
		gp.add(lblWeights, 0, 2);
		gp.add(weightsFile, 1, 2);
		pane.setTop(hdr);
		pane.setCenter(gp);
		Button encode = new Button("Encode");
		Button decode = new Button("Decode");
		ckOptimize = new CheckBox("Optimize Weights");
		ckOptimize.setSelected(true);
		
		encode.setOnAction(e -> hf.encode(txtFile.getText(),
				                           binFile.getText(),
				                           weightsFile.getText(),
				                           optimize));
		decode.setOnAction(e -> hf.decode(binFile.getText(),
										  txtFile.getText(),
										  weightsFile.getText(),
										  optimize));
		ckOptimize.setOnAction(e -> optimize = ckOptimize.isSelected());
		
		btnBox.setAlignment(Pos.CENTER);
		btnBox.getChildren().addAll(encode,decode,ckOptimize);
		pane.setBottom(btnBox);
		
		Scene scene = new Scene(pane, 400,200);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
        Application.launch(args);
	}
	

	/**
	 * Input alert - called by EncodeDecode when there is an
	 *               issue with trying to read a file.
	 *
	 * @param errorMsg the error msg to reflect to the users
	 */
	void inputAlert(String errorMsg) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText("Input File Error");
		alert.setContentText(errorMsg);
		alert.showAndWait();
	}

	/**
	 * Output alert- called by EncodeDecode when there is an
	 *               issue with trying to write a file.
	 *               
	 * @param errorMsg the error msg
	 */
	void outputAlert(String errorMsg) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText("Output File Error");
		alert.setContentText(errorMsg);
		alert.showAndWait();
	}

	/**
	 * Confirm alert- called by EncodeDecode when writing the file
	 *                would overwrite existing content...
	 *
	 * @param msg the msg
	 * @return true, if successful
	 */
	boolean confirmAlert(String msg) {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setHeaderText("Overwriting Weights File!");
		alert.setContentText(msg);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent())
			return(result.get() == ButtonType.OK);
		return false;
	}

	/**
	 * Done alert- called by EncodeDecode when the executing 
	 *             operation has completed.
	 *
	 * @param success the success
	 */
	void doneAlert(String success) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Saved Weights!");
		alert.setContentText(success);
		alert.showAndWait();
	}

}
