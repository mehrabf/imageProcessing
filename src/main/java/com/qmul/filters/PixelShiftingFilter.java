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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PixelShiftingFilter extends AbstractCommandFilter{
    private final int shiftVal;
    private final double scalingFactor;
    public PixelShiftingFilter(PathPanel panel, BufferedImage bi, int shiftVal, double scalingFactor) {
        super(panel, bi);
        this.commandName = String.format("Shift pixel by %d with scaling factor %f", shiftVal, scalingFactor);      
        this.shiftVal = shiftVal;
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
            transformedImage = new FImagePath(pixelShiftingProcess(this.bufferImg, this.imgWidth, this.imgHeight, this.shiftVal, this.scalingFactor));
        } catch (IOException ex) {
            Logger.getLogger(PixelShiftingFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (transformedImage != null){
            transformedImage.setCurrentAngle(this.angle);
            panel.add(transformedImage);
        }
    }
    
  /**
   * This method apply order statistics mid point filter to enhance the image
   * 
   * @param bi      buffered image
   * @param width   image width
   * @param height  image height
   * @param shiftVal shift value
   * @param scalingFactor scaling factor
   */      
    private BufferedImage pixelShiftingProcess(BufferedImage bi, int width, int height, int shiftVal, double scalingFactor) throws IOException{
        // get the  RGB channels
        int[][][] channels = ImageUtil.convertImageToMatrix(bi);
        
        int[][][] shiftedImg = getPixelShiftMatrix(channels, width, height, shiftVal, scalingFactor);
        return convertArrayToBufferedImg(shiftedImg[0], shiftedImg[1], shiftedImg[2], shiftedImg[3]);         
    }
    
  /**
   * This method apply order statistics mid point filter to enhance the image
   * 
   * @param channels     image rgb channels
   * @param width        image width
   * @param height       image height
   * @param shiftVal     shift value
   * @param scalingFactor scaling factor
   */      
    int[][][] getPixelShiftMatrix(int[][][] channels,int width, int height, int shiftVal, double scalingFactor){
        int redMin, redMax, greenMin, greenMax, blueMin, blueMax;
        int [][][] outputMatrix = new int[4][width][height];
        
        // initialise the rgb min and max
        redMax = redMin = (int)Math.round(scalingFactor * (channels[0][0][0] + shiftVal)); 
        greenMax = greenMin = (int)Math.round(scalingFactor * (channels[1][0][0] + shiftVal)); 
        blueMax = blueMin = (int)Math.round(scalingFactor * (channels[2][0][0] + shiftVal)); 
        
        for(int row = 0; row < width; row++){
            for(int col = 0; col < height; col++){
                
                outputMatrix[0][row][col] = (int)Math.round(scalingFactor * (channels[0][row][col] + shiftVal)); 
                outputMatrix[1][row][col] = (int)Math.round(scalingFactor * (channels[1][row][col] + shiftVal)); 
                outputMatrix[2][row][col] = (int)Math.round(scalingFactor * (channels[2][row][col] + shiftVal)); 
                
                // update red pixel minimum value
                if (redMin > outputMatrix[0][row][col]) { 
                    redMin = outputMatrix[0][row][col]; 
                }
                // update red pixel maximum value
                if (redMax < outputMatrix[0][row][col]) { 
                    redMax = outputMatrix[0][row][col]; 
                } 
                // update green pixel minimum value
                if (greenMin > outputMatrix[1][row][col]) { 
                    greenMin = outputMatrix[1][row][col]; 
                }
                // update green pixel maximum value
                if (greenMax < outputMatrix[1][row][col]) { 
                    greenMax = outputMatrix[1][row][col]; 
                } 
                // update blue pixel minimum value
                if (blueMin > outputMatrix[2][row][col]) { 
                    blueMin = outputMatrix[2][row][col]; 
                }
                // update blue pixel maximum value
                if (blueMax < outputMatrix[2][row][col]) { 
                    blueMax = outputMatrix[2][row][col]; 
                }
            }
        }

        // apply general Method
        for(int row = 0; row < width; row++){
            for(int col = 0; col < width; col++){
                outputMatrix[0][row][col] = 255 * (outputMatrix[0][row][col] - redMin)   / ( redMax - redMin);
                outputMatrix[1][row][col] = 255 * (outputMatrix[1][row][col] - greenMin) / ( greenMax - greenMin);
                outputMatrix[2][row][col] = 255 * (outputMatrix[2][row][col] - blueMin)  / ( blueMax - blueMin);
            }
        }   
        return outputMatrix;        
    }
    
}
