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
	
		
	public static void generateAggregateFromSpriteFolders(int nWidthInTiles, int nHeightInTiles, int nSizeOfTilesPx, String sDirectory){
		
		File fileDirectory = new File(sDirectory);
		
		if(fileDirectory.isDirectory()){
			
			Path pathDirectory = fileDirectory.toPath();
			
			// Get a list of ALL file names in the given directory.
			List<String> listFileNames = getAllFileNames(pathDirectory);
			Collections.reverse(listFileNames);
			
			// Generate the output image.
			BufferedImage biOut = generateImage(nWidthInTiles, nHeightInTiles, nSizeOfTilesPx, listFileNames);
			
			// Save the image to memory.
			saveImage(biOut, sDirectory, OUTPUT_FILENAME);
			
		} // if(fileChosen.isDirectory())
	} // END
	
	
	/**
	 * Saves a given BufferedImage to memory. Images are saved as type PNG.
	 * @param biOutput The image to be saved.
	 * @param sDirectory The String representing the directory to which the image will be saved.
	 * @param sFileName The name of the file to be saved. Must include '\' at the beginning, and ".png" at the end.
	 * */
	private static void saveImage(BufferedImage biOutput, String sDirectory , String sFileName){
		// Save the given image.
		File fileOutput = new File(sDirectory + sFileName);
		try{
			ImageIO.write(biOutput, "png", fileOutput);
			
			System.out.println("File saved to " + sDirectory + OUTPUT_FILENAME);
			
		} catch (IOException e){
			e.printStackTrace();
		}
	} // END
	
	
	/**
	 * Generates an aggregate image from a list of smaller image files.
	 * @param nWidthInTiles Width of final picture in number of Sprite pictures.
	 * @param nHeightInTiles Height of final picture in number of Sprite pictures.
	 * @param nSizeOfTilesPx Size of a side length of the given Sprite pictures.
	 * @param listFileNames List of strings corresponding to the filepaths of the Sprite images from which the output will be constructed.
	 * */
	public static BufferedImage generateImage(int nWidthInTiles, int nHeightInTiles, int nSizeOfTilesPx, List<String> listFileNames){
		
		int nWidth = nWidthInTiles * nSizeOfTilesPx;
		int nHeight = nHeightInTiles * nSizeOfTilesPx;
		int nTotalTiles = nWidthInTiles * nHeightInTiles;
		BufferedImage biOutput = new BufferedImage(nWidth, nHeight, BufferedImage.TYPE_INT_ARGB);
		
		
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
						
						addImageToOutput(bi, biOutput, nStartX, nStartY, nSizeOfTilesPx);
						
						nFilesProcessed++;
						
						System.out.println("Files Processed: " + nFilesProcessed);
						
					} catch (IOException e){
						e.printStackTrace();
					}
					
				} // if(sFileName.contains(...)...
			} // if(fileCurrent.isDirectory() == false)
		} // for sFileName
		
		return biOutput;
	} // END
	
	
	
	/**
	 * @param biToAdd A single buffered Sprite image to be added to buffered image biOutput
	 * @param biOutput The output image to which biToAdd will be added.
	 * @param nStartX The X-tile position at which biToAdd will be placed in biOutput.
	 * @param nStartY The Y-tile position at which biToAdd will be placed in biOutput.
	 * @param nSizeOfTilesPx The size of biToAdd (or other Sprite images) in pixels.
	 * */
	private static void addImageToOutput(BufferedImage biToAdd, BufferedImage biOutput, int nStartX, int nStartY, int nSizeOfTilesPx){
		
		for(int nX = 0; nX < nSizeOfTilesPx; nX++){
			for(int nY = 0; nY < nSizeOfTilesPx; nY++){
				// Get pixel of single buffered image at current coordinates.
				int nARGB = biToAdd.getRGB(nX, nY);
				
				// Translate the pixel coordinates of the single buffered image to the output buffered image.
				int nDrawX = nStartX + nX;
				int nDrawY = nStartY + nY;
				
				try{
					biOutput.setRGB(nDrawX, nDrawY, nARGB);
				} catch(Exception e){
					e.printStackTrace();
					return;
				}
			} // for nY
		} // for nX
		
	} // END
	
	
	/***
	 * Given a directory, this method gathers all immediate subdirectories, and gathers together a list of only files contained within those directories.
	 * Note that this method only goes "one layer deep", and will not recursively dig through sub-sub-directories to find more files.
	 * @param sDirectory The top-most directory containing the folders to be searched.
	 */
	public static List<String> getAllFileNames(Path sDirectory){
		
		System.out.println("\n getAllFileNames -> " + sDirectory.toAbsolutePath().toString());
		
		List<Path> listSubDirectories = getDirectoryNames(sDirectory);
		List<String> listAllFileNames = new ArrayList<String>();
		
		for(Path pathSubDirectory : listSubDirectories){
			listAllFileNames.addAll(getFileNames(pathSubDirectory.toAbsolutePath()));
		} // for

		return listAllFileNames;
		
	} // END
	
	
	/**
	 * Returns a List of Path objects contained at the given Path
	 * @param sDirectory A file path in which to look.
	 * */
	public static List<Path> getDirectoryNames(Path sDirectory){
		List<Path> listPathDirectories = new ArrayList<Path>();
		
		try(DirectoryStream<Path> dsPath = Files.newDirectoryStream(sDirectory.toAbsolutePath())){
			
			System.out.println("\n\t getDirectoryNames -> " + sDirectory.toAbsolutePath().toString());
			for(Path path : dsPath){
				if(path.toFile().isDirectory()){
					listPathDirectories.add(path);
					System.out.println("\t\t" + path.toString());
				}
			} // for Path path
		} catch(IOException e){
			e.printStackTrace();
		}
		
		return listPathDirectories;
		
	} // END
	
	
	/**
	 * Returns a list of files contained within a given directory.
	 * @param sDirectory The directory in which the method looks for files.
	 * */
	public static List<String> getFileNames(Path sDirectory){
		List<String> listFileNames = new ArrayList<String>();
		
		try(DirectoryStream<Path> dsPath = Files.newDirectoryStream(sDirectory.toAbsolutePath())){
			
			System.out.println("\n\t getFileNames -> " + sDirectory.toAbsolutePath().toString());
			for(Path filePath : dsPath){
				if(filePath.toFile().isDirectory() == false){
					listFileNames.add(filePath.toAbsolutePath().toString());
					System.out.println("\t\t" + filePath.toAbsolutePath().toString());
				}
			} // for(Path path : dsSubDir)
		} catch(IOException e){
			e.printStackTrace();
		}
		return listFileNames;
	} // END
	
	
	
} // END
