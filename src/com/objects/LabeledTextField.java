package com.objects;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class LabeledTextField extends HBox{

	private Label label;
	private TextField tf;
	
	
	public LabeledTextField(String title){
		label = new Label(title);
		tf = new TextField();
		tf.setPromptText("Enter data...");
		
		this.getChildren().addAll(label, tf);
		this.setAlignment(Pos.CENTER);
		this.setSpacing(10.0);
		
	} // END
	
	
	public TextField getTextField(){
		return this.tf;
	} // END
	
	public String getText(){
		return this.tf.getText();
	} // END
	
	public int getTextAsInteger(){
		int iVal;
		try{
			iVal = Integer.parseInt(this.tf.getText());
		} catch(NumberFormatException e){
			e.printStackTrace();
			iVal = 0;
		}
		return iVal;
	} // END
	
} // END
