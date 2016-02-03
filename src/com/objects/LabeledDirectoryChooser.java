package com.objects;

import java.io.File;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class LabeledDirectoryChooser extends HBox{

	private Label label;
	private TextField tf;
	private Button button;
	private DirectoryChooser dc;
	
	private Stage ownerWindow;
	
	
	public LabeledDirectoryChooser(Stage stage){
		ownerWindow = stage;
		label = new Label("Directory: ");
		tf = new TextField();
		tf.setPromptText("Select a directory...");
		button = new Button("Browse...");
		dc = new DirectoryChooser();
		dc.setTitle("Select a directory");
		
		this.getChildren().addAll(	label,
									tf,
									button);
		this.setAlignment(Pos.CENTER);
		this.setSpacing(10.0);
		
		button.setOnAction(e -> eventDisplayDirChooser());
	} // END
	
	
	/**
	 * Calls forth the DirectoryChooser
	 * */
	private void eventDisplayDirChooser(){
		File dir = dc.showDialog(ownerWindow);
		if(dir != null){
			tf.setText(dir.getAbsolutePath());
		} // if
	} // END
	
	
	/**
	 * Returns a string representing the chosen file directory.
	 * String comes from the TextField object.
	 * */
	public String getDirectory(){
		return this.tf.getText();
	} // END
	
	/**
	 * Returns the TextField object.
	 * */
	public TextField getTextField(){
		return this.tf;
	} // END
	
	
} // END
