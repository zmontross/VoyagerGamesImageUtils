package com.main;

import java.io.IOException;

import com.objects.LabeledCheckBox;
import com.objects.LabeledDirectoryChooser;
import com.objects.LabeledTextField;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ImageUtilsMain extends Application{

	private static final String APP_TITLE = "Voyager Games Image Utilities";
	private static final String VG_LOGO_FPATH = "res/VoyagerGamesLogo-192x192.png";
	private static final double WINDOW_WIDTH = 400.0;
	private static final double WINDOW_HEIGHT = 600.0;
	
	
	Stage window;
	Scene scene;
	
	VBox layoutMain;
	
	ImageView ivLogo;
	LabeledTextField ltfWidth;
	LabeledTextField ltfHeight;
	LabeledTextField ltfTimeBetweenFrames;
	LabeledCheckBox lcbWillMakeGif;
	LabeledCheckBox lcbWillGifLoop;
	LabeledCheckBox lcbOpenDirOnComplete;
	LabeledDirectoryChooser ldcDirectory;
	Button btnSubmit;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	} // END

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		window = new Stage();
		window = primaryStage;
		window.setTitle(APP_TITLE);
		window.setWidth(WINDOW_WIDTH);
		window.setHeight(WINDOW_HEIGHT);
		/**************************************************************/
		/**************************************************************/
		/**************************************************************/
		
		// Create a Voyager Games logo
		ivLogo = new ImageView();
		ivLogo.setImage( new Image(("file:" + VG_LOGO_FPATH), 96, 96, false, false) );
		
		// Create User Parameter inputs
		ltfWidth = new LabeledTextField("Width: ");
		ltfHeight = new LabeledTextField("Height: ");
		ldcDirectory = new LabeledDirectoryChooser(window);
		ltfTimeBetweenFrames = new LabeledTextField("Time between frames (ms): ");
		lcbWillMakeGif = new LabeledCheckBox("Make a GIF?");
		lcbWillGifLoop = new LabeledCheckBox("Loop GIF Continuously?");
		lcbOpenDirOnComplete = new LabeledCheckBox("Open Directory on Completion?");
		btnSubmit = new Button("SUBMIT");
		
		// Set initial states of GUI elements related to GIF-making
		lcbWillGifLoop.setDisable(true);
		ltfTimeBetweenFrames.setDisable(true);
		lcbWillMakeGif.getCheckBox().selectedProperty().addListener(new ChangeListener<Boolean>(){
			public void changed(ObservableValue<? extends Boolean> ov, Boolean oldVal, Boolean newVal){
				if(lcbWillMakeGif.getCheckBox().isSelected()){
					lcbWillGifLoop.setDisable(false);
					ltfTimeBetweenFrames.setDisable(false);
				} else{
					lcbWillGifLoop.setDisable(true);
					ltfTimeBetweenFrames.setDisable(true);
				} // if
			} // END
		});
		
		// Code governing the image/GIF creation processes will go
		//	in a method referenced by this event-handler.
		btnSubmit.setOnAction(e -> eventSubmit(e));
		
		/**************************************************************/
		/**************************************************************/
		/**************************************************************/
		
		// Assemble GUI
		layoutMain = new VBox();
		layoutMain.getChildren().addAll(ivLogo,
										new Separator(),
										ltfWidth,
										ltfHeight,
										ldcDirectory,
										new Separator(),
										lcbWillMakeGif,
										lcbWillGifLoop,
										ltfTimeBetweenFrames,
										new Label(),
										btnSubmit,
										lcbOpenDirOnComplete,
										new Label(),
										new Label(),
										new Label("Created by Zachary Douglas Montross"));
		layoutMain.setSpacing(10.0);
		layoutMain.setAlignment(Pos.CENTER);
		layoutMain.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
		
		scene = new Scene(layoutMain);
		window.setScene(scene);
		window.show();
	} // END

	
	
	private void eventSubmit(ActionEvent e) {
		System.out.println();
		System.out.println("Width\t" + ltfWidth.getTextAsInteger());
		System.out.println("Height\t" + ltfHeight.getTextAsInteger());
		System.out.println("Dir.\t" + ldcDirectory.getDirectory());
		System.out.println("Making GIF?\t" + lcbWillMakeGif.isSelected());
		System.out.println("Looping GIF?\t" + lcbWillGifLoop.isSelected());
		System.out.println("Time Between Frames\t" + ltfTimeBetweenFrames.getTextAsInteger());
		System.out.println("Open Dir?\t" + lcbOpenDirOnComplete.isSelected());
		
		if(lcbOpenDirOnComplete.isSelected()){
			try {
				openDirectory(ldcDirectory.getDirectory());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	} // END

	
	private void openDirectory(String dir) throws IOException{
		System.out.println("\t\tOpening directory " + dir);
		//ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", dir);
		new ProcessBuilder("explorer.exe", "/select", dir).start();
	} // END
	
	
} // END