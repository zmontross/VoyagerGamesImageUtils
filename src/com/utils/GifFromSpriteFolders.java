package com.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;

public class GifFromSpriteFolders {

	
	/**
	 * 
	 * It will go to the given directory,
	 *  which will be the top directory containing subdirectories for each frame.
	 * Then, it'll grab each subdirectory,
	 *  and build an image from each one of those
	 *   by combining all the images it contains.
	 * This part is just using my existing code.
	 * 
	 * Next, it'll take each compiled image
	 *  and interlace the frame into an animated .gif.
	 * This you'll have to figure out...
	 * 
	 * */
	
	private static final String DEFAULT_OUTPUT_FILENAME = "\\Output.gif";
	
	
	private static final List<BufferedImage> listFrames = new ArrayList<BufferedImage>();
	
	
	/**
	 * Creates a GIF from files contained in a series of folders.
	 * @param nWidthInTiles Width of the final product in Sprites.
	 * @param nHeightInTiles Height of the final product in Sprites.
	 * @param nSizeOfTilesPx The size of each Sprite tile in Pixels.
	 * @param nTimeBetweenFramesMS The time between successive frames given in milliseconds.
	 * @param bLoopContinuously If true, the output GIF will loop continuously.
	 * @param sDirectory The top-level directory. Sub-directories contained within are searched for pictures from which the GIF will be built. The output file is saved here.
	 * @param sOutputFileName The desired name of the output file.
	 * */
	public static void compileFramesFromFoldersAndBuildGif(int nWidthInTiles,
															int nHeightInTiles,
															int nSizeOfTilesPx,
															int nTimeBetweenFramesMS,
															boolean bLoopContinuously,
															String sDirectory,
															String sOutputFileName) throws IIOException, IOException{
		List<BufferedImage> listFrames = new ArrayList<BufferedImage>();
		
		File fileTopLevelDirectory = new File(sDirectory);

		if(fileTopLevelDirectory.isDirectory()){
			
			Path pathTopLevelDirectory = fileTopLevelDirectory.toPath();
			
			//TODO Get Directories
			List<Path> listSubDirectories = ImageAggregator.getDirectoryNames(pathTopLevelDirectory);
			
			//TODO Create Frames from all pictures in each directory
			for(Path path : listSubDirectories){
				System.out.println(path.toFile().toString());
				System.out.println(path.toFile().isDirectory());
				
				if(path.toFile().isDirectory()){
					//TODO Get WIDTH * HEIGHT images located within the currently-examined directory
					List<String> listFileNames = ImageAggregator.getFileNames(path);
					
					//TODO Create a BufferedImage of size (WIDTH*PX)-by-(HEIGHT*PX). 
					//		Add all Sprites in the current directory to the BufferedImage.
					BufferedImage biFrame = ImageAggregator.generateImage(nWidthInTiles, nHeightInTiles, nSizeOfTilesPx, listFileNames);

					// Store the current Frame in the list of Frames.
					listFrames.add(biFrame);
					System.out.println(listFrames.size());
				} // if(path.toFile().isDirectory())
			} // for(Path path : listSubDirectories)
			
		} // if(fileDirectory.isDirectory())
		
		//TODO Submit frames to GifSequenceWriter class
		/**************************************************************/
		/**************************************************************/
		/**************************************************************/
		
		// grab the output image type from the first image in the sequence
		BufferedImage firstImage = listFrames.get(0);
		// create a new BufferedOutputStream with the last argument
		ImageOutputStream output = new FileImageOutputStream(new File(sDirectory + sOutputFileName));
		
		// create a gif sequence with the type of the first image, 1 second
		// between frames, which loops continuously
		
		GifSequenceWriter writer = new GifSequenceWriter(output, firstImage.getType(), nTimeBetweenFramesMS, bLoopContinuously);
		
		// write out the first image to our sequence...
		writer.writeToSequence(firstImage);
		for(int i=1; i<listFrames.size(); i++) {
			BufferedImage nextImage = listFrames.get(i);
		    writer.writeToSequence(nextImage);
		}
		
		writer.close();
		output.close();
		
	} // END
	
} //END
