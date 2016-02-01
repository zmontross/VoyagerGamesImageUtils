package com.objects;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class LabeledCheckBox extends HBox{

	private CheckBox cb;

	
	public LabeledCheckBox(String title){
		cb = new CheckBox(title);
		cb.setIndeterminate(false);
		cb.setSelected(false);
		
		this.getChildren().addAll(cb);
		this.setAlignment(Pos.CENTER);
		this.setSpacing(10.0);
		
	} // END
	
	
	public CheckBox getCheckBox(){
		return this.cb;
	} // END
	
	public Boolean isSelected(){
		return this.cb.isSelected();
	}
	
} // END
