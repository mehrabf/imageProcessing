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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThresholdingFilter extends AbstractCommandFilter{
  
    private final int threshold;
    public ThresholdingFilter(PathPanel panel, BufferedImage bi, int threshold) {
        super(panel, bi);
        this.commandName = String.format("Thresholing filter using %s level", threshold);      
        this.threshold = threshold;
    }
  /**
   * This method provides override transform which is used to execute the command.
   * the processed buffered image will be added to the panel.
   */     
    @Override
    protected void transform() {
        FImagePath transformedImage = null;
        try {
            transformedImage = new FImagePath(thresholding(this.bufferImg, this.threshold));
        } catch (IOException ex) {
            Logger.getLogger(ThresholdingFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (transformedImage != null){
            transformedImage.setCurrentAngle(this.angle);
            panel.add(transformedImage);
        }
    }    

  /**
   * This method apply thresholding filter to the buffered image
   * 
   * @param bi          image rgb channels
   * @param threshold   selected threshold
   */     
    private BufferedImage thresholding(BufferedImage bi, int threshold) throws IOException{
                // get the  RGB channels
        int[][][] channels = ImageUtil.convertImageToMatrix(bi);
        // convert the RGB channels to grayscale
        var grayScaleImage = ImageUtil.convertRgbToGrayscale(channels[0], channels[1], channels[2]); 
        BufferedImage binaryImage = calculateThresholdingImg(grayScaleImage, threshold);
        return binaryImage;
    }
    
  /**
   * This method apply smoothing filter to the buffered image
   * 
   * @param grayScaleImage  image grayscale
   * @param threshold        selected threshold
   */     
    private BufferedImage calculateThresholdingImg(BufferedImage grayScaleImage, int threshold) throws IOException{
        int binVal;
        int width = grayScaleImage.getWidth();
        int height = grayScaleImage.getHeight();
        int pixel[];
        BufferedImage outputBinaryImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                pixel = grayScaleImage.getRaster().getPixel(row, col, new int[3]);
                if (pixel[0] >= threshold){
                    binVal = 0;
                }
                else {
                    binVal = 255;
                }
                int binaryValue = (binVal << 16) + (binVal << 8) + binVal; 
                outputBinaryImage.setRGB(row, col, binaryValue);
            }
        }
        return outputBinaryImage;
    }    
    
   

}
