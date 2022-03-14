/* 
 * Description: This class provides bit plane filter
 * Author: Mehrab Firouzi Moghadam
 * Date: 26/03/2021  
 */
package com.qmul.filters;

import com.qmul.commandFramework.AbstractCommandFilter;
import com.qmul.model.FImagePath;
import com.qmul.model.PathPanel;
import com.qmul.imgUtil.ImageUtil;
import static com.qmul.imgUtil.ImageUtil.convertRgbToGrayscale;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BitPlaneFilter extends AbstractCommandFilter{
    private final int bitPlane;
    public BitPlaneFilter(PathPanel panel, BufferedImage bi, int bitPlane) {
        super(panel, bi);
        this.commandName = String.format("Bit plane %d filter using", bitPlane);   
        this.bitPlane = bitPlane;
    }
  /**
   * This method provides override transform which is used to execute the command.
   * the processed buffered image will be added to the panel.
   */     
    @Override
    protected void transform() {
        FImagePath transformedImage = null;
        try {
            transformedImage = new FImagePath(bitPlaneProcessing(this.bufferImg, this.imgWidth, this.imgHeight, this.bitPlane));
        } catch (IOException ex) {
            Logger.getLogger(BitPlaneFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (transformedImage != null){
            transformedImage.setCurrentAngle(this.angle);
            panel.add(transformedImage);
        }
    }    
   
  /**
   * This method process the bitplane filter
   * convert buffered image into an array of 3d
   * convert the 3d to grayscale image
   * process the grayscale image to calculate the bitplane
   * convert the bitplane image to buffered image 
   * 
   * @param bi      buffered image
   * @param width   image width
   * @param height  image height
   * @param bitPlane bit number/position
   */     
    private BufferedImage bitPlaneProcessing(BufferedImage bi, int width, int height, int bitPlane) throws IOException{
        // get all channels
        int[][][] channels = ImageUtil.convertImageToMatrix(bi);
        // convert the RGB channels to grayscale
        var grayScaleImage = ImageUtil.convertRgbToGrayscale(channels[0], channels[1], channels[2]); 
        int[][][] bitPlaneImg = getBitPlaneMatrix(grayScaleImage, width, height, bitPlane);
        return convertRgbToGrayscale(bitPlaneImg[0], bitPlaneImg[1], bitPlaneImg[2]); 
    }
    
  /**
   * This method calculate the bitplane image
   * 
   * @param bi      buffered image
   * @param width   image width
   * @param height  image height
   * @param bitPlane bit number/position
   */     
    private int[][][] getBitPlaneMatrix(BufferedImage bi, int width, int height, int bitPlane) throws IOException{
        int redPixel, greenPixel, bluePixel;
        int[][][] outputBitPlane = new int[3][width][height];
        int[][][] inputImage = ImageUtil.convertImageToMatrix(bi);
        for(int row = 0; row < width; row++){
            for(int col = 0; col< height; col++){
                redPixel   = (inputImage[0][row][col]>> bitPlane)&1;
                greenPixel = (inputImage[1][row][col]>> bitPlane)&1;
                bluePixel  = (inputImage[2][row][col]>> bitPlane)&1;
                outputBitPlane[0][row][col] = redPixel   == 1 ? 255 : 0;
                outputBitPlane[1][row][col] = greenPixel == 1 ? 255 : 0;
                outputBitPlane[2][row][col] = bluePixel  == 1 ? 255 : 0;
            }
        }
        return outputBitPlane;
    } 
}