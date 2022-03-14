/* 
 * Description: Image processing application
 * Author: Mehrab Firouzi Moghadam
 * Date: 26/03/2021  
 */
package com.qmul.imgUtil;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtil {

    public static int[][][] convertImageToMatrix(BufferedImage bi) {
        int width  = bi.getWidth(null);
        int height = bi.getHeight(null);  
        Color color;
        int[][][] imgMatrix = new int[4][width][height];
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                color = new Color(bi.getRGB(row, col));
                imgMatrix[0][row][col] = color.getRed();
                imgMatrix[1][row][col] = color.getGreen();
                imgMatrix[2][row][col] = color.getBlue();
                imgMatrix[3][row][col] = color.getAlpha();
            }
        }
        return imgMatrix;
    } 

  /**
   * This method converts 3d array to buffered image
   * 
   * @param rgbArray           image rgb channels
   * @return buffered Image
   */     
    public static BufferedImage convertArrayToBufferedImg(int[][][] rgbArray) throws IOException {

        int width = rgbArray.length;
        int height = rgbArray[0].length;

        BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        for(int row = 0; row < width; row++){
            for(int col = 0; col < height; col++){
                int pixelAlpha = rgbArray[0][row][col];
                int pixelRed   = rgbArray[1][row][col];
                int pixelGreen = rgbArray[2][row][col];
                int pixelBlue  = rgbArray[3][row][col];
                
                int pixels = (pixelAlpha << 24) | (pixelRed << 16) | (pixelGreen << 8) | pixelBlue;
                bi.setRGB(row, col, pixels);
            }
        }
        return bi;
    }  

    /**
    * This method converts 3d array to buffered image
    * 
    * @param redChannel         red channel
    * @param greenChannel       green channel
    * @param blueChannel        blue channel
    * @param alphaChannel       alpha channel
    * @return buffered Image
    */     
    public static BufferedImage convertArrayToBufferedImg(int[][] redChannel, int[][] greenChannel, int[][] blueChannel, int[][] alphaChannel) throws IOException {

        int width = alphaChannel.length;
        int height = alphaChannel[0].length;

        BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        for(int row = 0; row < width; row++){
            for(int col = 0; col < height; col++){
                int pixelAlpha = alphaChannel[row][col];
                int pixelRed   = redChannel[row][col];
                int pixelGreen = greenChannel[row][col];
                int pixelBlue  = blueChannel[row][col];
                
                int pixels = (pixelAlpha << 24) | (pixelRed << 16) | (pixelGreen << 8) | pixelBlue;
                bi.setRGB(row, col, pixels);
            }
        }
        return bi;
    }  

    /**
    * This method converts rgb to grayscale image
    * 
    * @param bi         red channel
    * @return buffered Image
    */     
    public static BufferedImage convertRgbToGrayscale(BufferedImage bi)throws IOException {
        BufferedImage outputGrayscale = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        int[][][] imageMatrix = convertImageToMatrix(bi);
        for (int row = 0; row < bi.getWidth(); ++row){
            for (int col = 0; col < bi.getHeight(); ++col)
            {                
                // Normalize and gamma correct:
                double redPixel   = Math.pow(imageMatrix[0][row][col] / 255.0, 2.2);
                double greenPixel = Math.pow(imageMatrix[1][row][col] / 255.0, 2.2);
                double bluePixel  = Math.pow(imageMatrix[2][row][col] / 255.0, 2.2);

                // Calculate luminance:
                double lum = 0.2126 * redPixel + 0.7152 * greenPixel + 0.0722 * bluePixel;

                // Gamma compand and rescale to byte range:
                int grayLevel = (int) (255.0 * Math.pow(lum, 1.0 / 2.2));
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel; 
                outputGrayscale.setRGB(row, col, gray);
            }
        }
        return outputGrayscale;
    }

    /**
    * This method converts 3d array to buffered image
    * 
    * @param redChannel         red channel
    * @param greenChannel       green channel
    * @param blueChannel        blue channel
    * @return buffered Image
    */    
    public static BufferedImage convertRgbToGrayscale(int[][] redChannel, int[][] greenChannel, int[][] blueChannel)throws IOException {
        int width  = redChannel.length;
        int height = redChannel[0].length;
        double redPixel, greenPixel, bluePixel;         
        BufferedImage outputGrayscale = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int row = 0; row < width; ++row){
            for (int col = 0; col < height; ++col)
            {                
                // Normalize and gamma correct:
                redPixel   = Math.pow(redChannel[row][col] / 255.0, 2.2);
                greenPixel = Math.pow(greenChannel[row][col] / 255.0, 2.2);
                bluePixel  = Math.pow(blueChannel[row][col] / 255.0, 2.2);

                // Calculate luminance:
                double lum = 0.2126 * redPixel + 0.7152 * greenPixel + 0.0722 * bluePixel;

                // Gamma compand and rescale to byte range:
                int grayLevel = (int) (255.0 * Math.pow(lum, 1.0 / 2.2));
                int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel; 
                outputGrayscale.setRGB(row, col, gray);
            }
        }
        return outputGrayscale;
    }
    
    /**
    * This method save the image into a file
    * 
    * @param path            path to file
    * @param imageRGB        rgb channels
    * @return File
    */     
    public static File saveMatrixAsImage(String path, int[][][] imageRGB) throws IOException {
        File outputFile = new File(path);
        ImageIO.write(convertArrayToBufferedImg(imageRGB), "png", outputFile);
        return outputFile;
    }  

    /**
    * This method normalise the pixel value
    * 
    * @param value            normalise the pixel value
    * @return int
    */       
    private static int limitRangePixel(double value) {
        if (value < 0.0) {
            value = -value;
        }
        if (value > 255) {
            return 255;
        } else {
            return (int) value;
        }
    }
    /**
    * This method normalise the pixel value
    * 
    * @param value            normalise the pixel value
    * @return int
    */     
    public static int normalisePixelRange(double value) {
        if (value < 0.0) {
            return 0;
        }
        if (value > 255) {
            return 255;
        } else {
            return (int) value;
        }
    }
    
}
