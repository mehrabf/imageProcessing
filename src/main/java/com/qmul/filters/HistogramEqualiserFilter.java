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

public class HistogramEqualiserFilter  extends AbstractCommandFilter{
        public HistogramEqualiserFilter(PathPanel panel, BufferedImage bi) {
        super(panel, bi);
        this.commandName = String.format("Histogram Equalisation.");        
    }
  /**
   * This method provides override transform which is used to execute the command.
   * the processed buffered image will be added to the panel.
   */     
    @Override
    protected void transform() {
        FImagePath histogramEqualisedImg = null;
        try {
            histogramEqualisedImg = new FImagePath(imgHist(this.bufferImg, this.imgWidth, this.imgHeight));
        } catch (IOException ex) {
            Logger.getLogger(HistogramEqualiserFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (histogramEqualisedImg != null){
            histogramEqualisedImg.setCurrentAngle(this.angle);
            panel.add(histogramEqualisedImg);
        }
    }    

  /**
   * This method process histogram equalisation on the buffered image
   * 
   * @param bi      buffered image
   * @param width   image width
   * @param height  image height
   */      
    private BufferedImage imgHist(BufferedImage bi, int width, int height) throws IOException{
        // get the  RGB channels
        int[][][] channels = ImageUtil.convertImageToMatrix(bi);
        // convert the RGB channels to grayscale
        var grayScaleImage = ImageUtil.convertRgbToGrayscale(channels[0], channels[1], channels[2]);      
        // calculate the histogram
        int[] calculatedHist = calculateHistogram(grayScaleImage);
        // Calculate Cumulative Distribution Function 
        int[] calHistCDF = calculateCDF(calculatedHist, width * height);
        // Apply the CDF to the grayscale image
        return histEqualize(grayScaleImage, width, height, calHistCDF);
    }
 
  /**
   * This method calculate the histogram of the image.
   * 
   * @param bi      buffered image
   */     
    private int[] calculateHistogram(BufferedImage bi) throws IOException{
        int width = bi.getWidth();
        int height = bi.getHeight();
        int pixel[];
        // Create an empty array to store the bins 255
        int[] bins = new int[256];
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                // get each pixels value R/G/B the intensity
                pixel = bi.getRaster().getPixel(row, col, new int[3]);
                // increment the bin based on the gray scale channel
                bins[pixel[0]]++;
            }
        }
        return bins;
    }

  /**
   * This method calculate cumulative distribution function
   * 
   * @param calculatedHist      buffered image
   * @param res total image resolution
   */    
    private int[] calculateCDF(int[] calculatedHist, int res){
        long sum = 0;
        //calculate the scale factor
        float scale = (float) 255.0 / res;
        //calculate cumulative distribution function
        for (int x = 0; x < calculatedHist.length; x++) {
            sum += calculatedHist[x];
            int value = (int) (scale * sum);
            if (value > 255) {
               value = 255;
            }
            calculatedHist[x] = value;
        }   
        return calculatedHist;
    }

  /**
   * This method apply histogram equalisation on the image using calculated cumulative distribution function
   * 
   * @param bi      buffered image
   * @param width   image width
   * @param height  image height
   * @param calHist calculated histogram
   */    
    private BufferedImage histEqualize(BufferedImage bi, int width, int height, int[] calHist) {
        // calculate total number of pixel in the original image.  
        int cdfVal;
        int pixel[];
        int gray;
        BufferedImage histEqualisedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                // get the pixel value 
                pixel = bi.getRaster().getPixel(row, col, new int[3]);
                // get the cdf value
                cdfVal = calHist[pixel[0]];
                // create grayscale pixel without alpha channel
                gray = (cdfVal << 16) + (cdfVal << 8) + cdfVal; 
                histEqualisedImage.setRGB(row, col, gray);
            }
        }
        return histEqualisedImage;
    }
}
