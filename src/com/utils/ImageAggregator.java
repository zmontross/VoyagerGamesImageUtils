package com.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageAggregator {

	private static final String OUTPUT_FILENAME = "\\Output.png";
	
		
	public static void run(int nWidthInTiles, int nHeightInTiles, int nSizeOfTilesPx, String sDirectory){
		int nWidth = nWidthInTiles * nSizeOfTilesPx;
		int nHeight = nHeightInTiles * nSizeOfTilesPx;
		int nTotalTiles = nWidthInTiles * nHeightInTiles;
		BufferedImage biOut = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_INT_ARGB);
		
		File fileChosen = new File(sDirectory);
		if(fileChosen.isDirectory()){
			Path pathChosen = fileChosen.toPath();
			List<String> listFileNames = new ArrayList<String>();
			listFileNames = getFileNames(listFileNames, pathChosen);
			
			
			Collections.reverse(listFileNames);
			
			int nFilesProcessed = 0;
			for(String sFileName : listFileNames){
				
				if(nFilesProcessed >= nTotalTiles){
					break;
				}
				
				File fileCurrent = new File(sFileName);
				if(fileCurrent.isDirectory() == false){
					if(	(sFileName.contains(".png") ||	sFileName.contains(".jpg") || sFileName.contains(".bmp")) ){
						
						try{
							int nStartX = (nFilesProcessed / nHeightInTiles) * nSizeOfTilesPx;	// Note. Mixup between Div and Mod for X and Y was a big pain point!
							int nStartY = (nFilesProcessed % nHeightInTiles) * nSizeOfTilesPx;
							
							System.out.println("Start X: " + nStartX);
							System.out.println("Start Y: " + nStartY);
							System.out.println(fileCurrent.getName());
							
							BufferedImage bi = ImageIO.read(fileCurrent);
							
							addImageToOutput(bi, biOut, nStartX, nStartY, nSizeOfTilesPx);
							
							nFilesProcessed++;
							
							System.out.println("Files Processed: " + nFilesProcessed);
							
						} catch (IOException e){
							e.printStackTrace();
						}
						
					} // if(sFileName.contains(...)...
				} // if(fileCurrent.isDirectory() == false)
			} // for sFileName
			
			// Image is saved.
			saveImage(biOut, sDirectory);
			
		} // if(fileChosen.isDirectory())
	} // END
	
	
	
	private static void saveImage(BufferedImage biOutput, String sDirectory ){
		// Save the given image.
		File fileOutput = new File(sDirectory + OUTPUT_FILENAME);
		try{
			ImageIO.write(biOutput, "png", fileOutput);
			
			System.out.println("File " + OUTPUT_FILENAME + " saved to directory " + sDirectory);
			
		} catch (IOException e){
			e.printStackTrace();
		}
	} // END
	
	
	/***
	 * 
	 * This function behaves acceptably when nStartX == nStartY
	 * 
	 * ArrayIndexOutOfBoundsException(s) occur when they are different values.....
	 * 
	 * 
	 * biToAdd is an image of size nSizeOfTilesPx squared
	 * 
	 * biOutput is an image whose width is Width*Px by Height*Px
	 * 
	 * nStartX and nStartY should be giving this function SUB-IMAGE coordinates to start at (i.e. not PIXEL coords).
	 * We convert to pixel coords by multiplying by nSizeOfTilesPx.
	 * 
	 * biOutput.setRGB(nDrawX, nDrawY, nARGB);
	 * 		this produces exception:
	 * 			java.lang.ArrayIndexOutOfBoundsException: Coordinate out of bounds!
	 * 
	 * 
	 * 
	 * ***/
	private static void addImageToOutput(BufferedImage biToAdd, BufferedImage biOutput, int nStartX, int nStartY, int nSizeOfTilesPx){
		
		for(int nX = 0; nX < nSizeOfTilesPx; nX++){
			for(int nY = 0; nY < nSizeOfTilesPx; nY++){
				
				// Get pixel of single buffered image at current coordinates.
				int nARGB = biToAdd.getRGB(nX, nY);
				
				// Translate the pixel coordinates of the single buffered image to the output buffered image.
				int nDrawX = nStartX + nX;
				int nDrawY = nStartY + nY;
				
				System.out.println("\taddImageToOutput: " + nX + "|" + nY + " -> " + nDrawX + "|" + nDrawY);
				
				try{
					biOutput.setRGB(nDrawX, nDrawY, nARGB); // Here is the problem line. java.lang.ArrayIndexOutOfBoundsException: Coordinate out of bounds!
				} catch(Exception e){
					System.out.println("\n\tException. Tried to draw " + nX + "|" + nY + "  to  " +  nDrawX + "|" + nDrawY);
					System.out.println("Size if biToAdd: " + biToAdd.getWidth() + "|" + biToAdd.getHeight());
					System.out.println("Size if biOutput: " + biOutput.getWidth() + "|" + biOutput.getHeight());
					e.printStackTrace();
					return;
				}
				
			} // for nY
		} // for nX
		
	} // END
	
	
	
	private static List<String> getFileNames(List<String> listFileNames, Path pathParent){
		
		try(DirectoryStream<Path> dsPath = Files.newDirectoryStream(pathParent)){
			System.out.println("\tpathParent -> " + pathParent.toString());
			for(Path path : dsPath){
				
				System.out.println(path.toString());
				
				
				if(path.toFile().isDirectory()){
					getFileNames(listFileNames, path);
				} else{
					
					String sPath = path.toString();
					int nStart = sPath.lastIndexOf('\\');
					int nEnd = sPath.length() - 1;
					String sPathSubstring = sPath.substring(nStart, nEnd + 1);
					
					// Check the last segment of the filepath so that we exclude the Output file.
					if(sPathSubstring.equals(OUTPUT_FILENAME) == false){
						listFileNames.add(path.toAbsolutePath().toString());
					}
				}
				
			} // for Path path
		} catch(IOException e){
			e.printStackTrace();
		}

		
		
		return listFileNames;
		
	} // END
	
	
} // END
