/* 
 * Description: Image processing application
 * Author: Mehrab Firouzi Moghadam
 * Date: 26/03/2021  
 */
package com.qmul.filters;

import com.qmul.commandFramework.AbstractCommandFilter;
import com.qmul.model.FImagePath;
import com.qmul.model.PathPanel;
import com.qmul.imgUtil.ImageUtil;
import static com.qmul.imgUtil.ImageUtil.convertArrayToBufferedImg;
import static com.qmul.imgUtil.ImageUtil.normalisePixelRange;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BrightnessFilter extends AbstractCommandFilter{
    private final float scalingFactor;
    public BrightnessFilter(PathPanel panel, BufferedImage bi, float scalingFactor) {
        super(panel, bi);
        this.commandName = String.format("Rescaling by %f", scalingFactor);        
        this.scalingFactor = scalingFactor;
    }
  /**
   * This method provides override transform which is used to execute the command.
   * the processed buffered image will be added to the panel.
   */     
    @Override
    protected void transform() {
        FImagePath transformedImage = null;
        try {
            transformedImage = new FImagePath(rescalingImageProcess(this.bufferImg, this.imgWidth, this.imgHeight, this.scalingFactor));
        } catch (IOException ex) {
            Logger.getLogger(BrightnessFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (transformedImage != null){
            transformedImage.setCurrentAngle(this.angle);
            panel.add(transformedImage);
        }
    }  
  /**
   * This method calculate the bitplane image
   * 
   * @param bi      buffered image
   * @param width   image width
   * @param height  image height
   * @param brightnessFactor scaling factor to rescale the brightness
   */       
    private BufferedImage rescalingImageProcess(BufferedImage bi, int width, int height, float brightnessFactor) throws IOException{
        // get the  RGB channels
        int[][][] channels = ImageUtil.convertImageToMatrix(bi);
        int[][][] scaledBrightnessImg = getRescalingMatrix(channels, width, height, brightnessFactor);
        return convertArrayToBufferedImg(scaledBrightnessImg[0], scaledBrightnessImg[1], scaledBrightnessImg[2], scaledBrightnessImg[3]); 
    }
  /**
   * This method apply the scaling brightness factor to each pixel and adjust the brightness
   * 
   * @param bi      buffered image
   * @param width   image width
   * @param height  image height
   * @param brightnessFactor scaling factor to rescale the brightness
   */       
    private int[][][] getRescalingMatrix(int[][][] inputImage, int width, int height, float brightnessFactor) throws IOException{   
        int [][][] outputMatrix = new int[4][width][height];
        for(int row = 0; row < width; row++){
            for(int col = 0; col < height; col++){
                outputMatrix[0][row][col] = Math.round(brightnessFactor * (inputImage[0][row][col]));
                outputMatrix[1][row][col] = Math.round(brightnessFactor * (inputImage[1][row][col]));
                outputMatrix[2][row][col] = Math.round(brightnessFactor * (inputImage[2][row][col])); 
                // Normalization of pixel values
                outputMatrix[0][row][col] = normalisePixelRange(outputMatrix[0][row][col]);
                outputMatrix[1][row][col] = normalisePixelRange(outputMatrix[1][row][col]);
                outputMatrix[2][row][col] = normalisePixelRange(outputMatrix[2][row][col]);
            }
        }        
        return outputMatrix;
    }    
}
