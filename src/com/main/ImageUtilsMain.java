package com.main;

import java.io.IOException;

import com.objects.LabeledCheckBox;
import com.objects.LabeledDirectoryChooser;
import com.objects.LabeledTextField;
import com.utils.GifFromSpriteFolders;
import com.utils.ImageAggregator;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ImageUtilsMain extends Application{

	private static final String APP_TITLE = "Voyager Games Image Utilities";
	private static final String VG_LOGO_FPATH = "res/VoyagerGamesLogo-192x192.png";
	private static final double WINDOW_WIDTH = 400.0;
	private static final double WINDOW_HEIGHT = 600.0;

	private static final int SPRITE_SIZE_PX = 32; 
	private static final String MSEC_BETWEEN_FRAMES = "250";
	private static final String GIF_DEFAULT_NAME = "\\OutputAnimation.gif";
	
	Stage window;
	Scene scene;
	
	VBox layoutMain;
	
	ImageView ivLogo;
	LabeledTextField ltfWidth;
	LabeledTextField ltfHeight;
	LabeledTextField ltfGifName;
	LabeledTextField ltfTimeBetweenFrames;
	LabeledCheckBox lcbWillGifLoop;
	LabeledCheckBox lcbOpenDirOnComplete;
	LabeledDirectoryChooser ldcDirectory;
	Button btnSubmit;
	
	HBox radioButtonGroup;
	RadioButton rbGif;
	RadioButton rbPic;
	ToggleGroup tgroup;
	
	
	public static void main(String[] args) {
		launch(args);
	} // END

	@Override
	public void start(Stage primaryStage) throws Exception {
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
		ltfGifName = new LabeledTextField("Name of GIF: ");
		ltfTimeBetweenFrames = new LabeledTextField("Time between frames (ms): ");
		lcbWillGifLoop = new LabeledCheckBox("Loop GIF Continuously?");
		lcbOpenDirOnComplete = new LabeledCheckBox("Open Directory on Completion?");
		btnSubmit = new Button("SUBMIT");
		
		// Set initial states of GUI elements related to GIF-making
		lcbWillGifLoop.setDisable(true);
		lcbWillGifLoop.getCheckBox().setSelected(true);		// Default value
		ltfTimeBetweenFrames.setDisable(true);
		ltfTimeBetweenFrames.getTextField().setText(MSEC_BETWEEN_FRAMES);	// Default value
		ltfGifName.setDisable(true);
		ltfGifName.getTextField().setText(GIF_DEFAULT_NAME);	// Default value
		
		// Setup radio buttons
		tgroup = new ToggleGroup();
		rbGif = new RadioButton("GIF");
		rbPic = new RadioButton("Picture");
		rbGif.setToggleGroup(tgroup);
		rbPic.setToggleGroup(tgroup);
		rbPic.setSelected(true);
		
		radioButtonGroup = new HBox();
		radioButtonGroup.setSpacing(10.0);
		radioButtonGroup.setAlignment(Pos.CENTER);
		radioButtonGroup.getChildren().addAll(rbGif, rbPic);
		
		tgroup.selectedToggleProperty().addListener(
				new ChangeListener<Toggle>(){

					@Override
					public void changed(
							ObservableValue<? extends Toggle> observable,
							Toggle oldValue, Toggle newValue) {
						
						if(tgroup.getSelectedToggle() != rbGif){
							lcbWillGifLoop.setDisable(true);
							ltfTimeBetweenFrames.setDisable(true);
							ltfGifName.setDisable(true);
						} else{
							lcbWillGifLoop.setDisable(false);
							ltfTimeBetweenFrames.setDisable(false);
							ltfGifName.setDisable(false);
						}
					} // END
				} // ChangeListener<Toggle>
				);
		
		
		// Code governing the image/GIF creation processes will go
		//	in a method referenced by this event-handler.
		btnSubmit.setOnAction(e -> eventSubmit());
		
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
										radioButtonGroup,
										ltfGifName,
										ltfTimeBetweenFrames,
										lcbWillGifLoop,
										new Label(),
										btnSubmit,
										lcbOpenDirOnComplete,
										new Label(),
										new Label(),
										new Label("Created by Zachary D. Montross"));
		layoutMain.setSpacing(10.0);
		layoutMain.setAlignment(Pos.CENTER);
		layoutMain.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
		
		scene = new Scene(layoutMain);
		window.setScene(scene);
		window.show();
	} // END

	
	
	private void eventSubmit() {
		// This is some admittedly lazy programming,
		//	and the very reason my freshman CS professor decided to wait until semester's end to teach try/catch,
		//  but this is far simpler than some horrible-looking nested-if-else tree.
		try{
			if(rbGif.isSelected()){
				// Make a GIF
				GifFromSpriteFolders.compileFramesFromFoldersAndBuildGif(ltfWidth.getTextAsInteger(),
																		ltfHeight.getTextAsInteger(),
																		SPRITE_SIZE_PX,
																		ltfTimeBetweenFrames.getTextAsInteger(),
																		lcbWillGifLoop.isSelected(),
																		ldcDirectory.getDirectory(),
																		ltfGifName.getText());
			}
			else{
				// Make a WxH picture
				ImageAggregator.generateAggregateFromSpriteFolders(ltfWidth.getTextAsInteger(),
																	ltfHeight.getTextAsInteger(),
																	SPRITE_SIZE_PX,
																	ldcDirectory.getDirectory());
			}

			
			if(lcbOpenDirOnComplete.isSelected()){
				try {
					openDirectory(ldcDirectory.getDirectory());
				} catch (IOException ioe) {
					ioe.printStackTrace();
					System.out.println("Problem opening directory on completion!");
				}
			}
		} catch(Exception e){
			//e.printStackTrace();
			System.out.println("Check your inputs!");
		}
		
	} // END

	
	
	private void openDirectory(String dir) throws IOException{
		System.out.println("\t\tOpening directory " + dir);
		//ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", dir);
		new ProcessBuilder("explorer.exe", "/select", dir).start();
	} // END
	
	
} // END