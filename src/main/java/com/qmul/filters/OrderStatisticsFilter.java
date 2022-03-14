/* 
 * Description: Image processing application
 * Author: Mehrab Firouzi Moghadam
 * Date: 26/03/2021  
 */
package com.qmul.filters;

import com.qmul.commandFramework.AbstractCommandFilter;
import com.qmul.frame.Reference;
import com.qmul.model.FImagePath;
import com.qmul.model.PathPanel;
import com.qmul.imgUtil.ImageUtil;
import static com.qmul.imgUtil.ImageUtil.convertArrayToBufferedImg;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderStatisticsFilter  extends AbstractCommandFilter{
    private final String filterType;
    public OrderStatisticsFilter(PathPanel panel, BufferedImage bi, String filterType) {
        super(panel, bi);
        this.commandName = String.format("Order statistics Enhancement %s", filterType);  
        this.filterType = filterType;
    }
  /**
   * This method provides override transform which is used to execute the command.
   * the processed buffered image will be added to the panel.
   */     
    @Override
    protected void transform() {
        FImagePath transformedImage = null;
        try {
            transformedImage = new FImagePath(orderStatisticsProcess(this.bufferImg, this.imgWidth, this.imgHeight, this.filterType));
        } catch (IOException ex) {
            Logger.getLogger(OrderStatisticsFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (transformedImage != null){
            transformedImage.setCurrentAngle(this.angle);
            panel.add(transformedImage);
        }
    }

  /**
   * This method apply order statistics filters to enhance the image
   * 
   * @param bi      buffered image
   * @param width   image width
   * @param height  image height
   * @param filterType selected filter type
   */     
    private BufferedImage orderStatisticsProcess(BufferedImage bi, int width, int height, String filterType) throws IOException{
        
        
        int[][][] shiftedImg;
        // get the  RGB channels
        int[][][] channels = ImageUtil.convertImageToMatrix(bi);
        // apply the statistic filter based on selected filter type
        if(filterType == null ? Reference.OS_MID == null : filterType.equals(Reference.OS_MID)){
            shiftedImg = getMidPoint(channels, width, height);
        }
        else {
            shiftedImg = getOrderStatistics(channels, width, height, filterType);
        }
        return convertArrayToBufferedImg(shiftedImg[0], shiftedImg[1], shiftedImg[2], shiftedImg[3]);           
    }
 
  /**
   * This method apply order statistics filters to enhance the image
   * 
   * @param bi      buffered image
   * @param width   image width
   * @param height  image height
   * @param filterType selected filter type
   */ 
    private int[][][] getOrderStatistics(int[][][] input, int width, int height, String filterType){
           
        int [][][] filteredMatrix = new int[4][width][height];
        int winSize = 9;
        int channelNum = 3;
        // get the index of the osWindow. This index is used to select pixel.
        int winIdx = getOrderedIndex(filterType, winSize);
        int[][] osWindow = new int[channelNum][winSize];
        int winCounter;
        for(int row = 1; row < width - 1; row++){
            for(int col = 1; col < height - 1; col++){
                // get the neighbour pixel values
                winCounter = 0;
                for(int winRow = -1; winRow <= 1; winRow++){
                    for(int winCol = -1; winCol <= 1; winCol++){
                        osWindow[0][winCounter] = input[0][row + winRow][col + winCol];   // red channel
                        osWindow[1][winCounter] = input[1][row + winRow][col + winCol];   // green channel
                        osWindow[2][winCounter] = input[2][row + winRow][col + winCol];   // blue channel
                        winCounter++;
                    }
                }
                // sort the neighbouring pixel values 
                Arrays.sort(osWindow[0]);
                Arrays.sort(osWindow[1]);
                Arrays.sort(osWindow[2]);
                // select pixel value based on selected index
                filteredMatrix[0][row][col] = osWindow[0][winIdx];
                filteredMatrix[1][row][col] = osWindow[1][winIdx];
                filteredMatrix[2][row][col] = osWindow[2][winIdx];
            }
        }   
        return filteredMatrix;
    }  
    
  /**
   * This method apply order statistics mid point filter to enhance the image
   * 
   * @param input      buffered image
   * @param width   image width
   * @param height  image height
   */ 
    private int[][][] getMidPoint(int[][][] input, int width, int height){
           
        int [][][] filteredMatrix = new int[4][width][height];
        int winSize = 9;
        int channelNum = 3;
        int[][] osWindow = new int[channelNum][winSize];
        int winCounter = 0;
        for(int row = 1; row < width - 1; row++){
            for(int col = 1; col < height - 1; col++){
                // get the neighbour pixel values
                winCounter = 0;
                for(int winRow = -1; winRow <= 1; winRow++){
                    for(int winCol = -1; winCol <= 1; winCol++){
                        osWindow[0][winCounter] = input[0][row + winRow][col + winCol];   // red channel
                        osWindow[1][winCounter] = input[1][row + winRow][col + winCol];   // green channel
                        osWindow[2][winCounter] = input[2][row + winRow][col + winCol];   // blue channel
                        winCounter++;
                    }
                }
                // sort the neighbouring pixel values 
                Arrays.sort(osWindow[0]);
                Arrays.sort(osWindow[1]);
                Arrays.sort(osWindow[2]);
                // select pixel value based on min index value + max min value
                filteredMatrix[0][row][col] = (int)((osWindow[0][0] + osWindow[0][winSize - 1]) / 2.0);
                filteredMatrix[1][row][col] = (int)((osWindow[1][0] + osWindow[1][winSize - 1]) / 2.0);
                filteredMatrix[2][row][col] = (int)((osWindow[2][0] + osWindow[2][winSize - 1]) / 2.0);
            }
        }   
        return filteredMatrix;
    }  
    
  /**
   * This method apply order statistics mid point filter to enhance the image
   * 
   * @param filterType     selected filter mode
   * @param windowLength   array length
   */     
    private int getOrderedIndex(String filterType, int windowLength){
        int idx = 0;
        switch(filterType){
            case Reference.OS_MEDIAN:
                idx = windowLength / 2;
                break;
            case Reference.OS_MIN:
                idx = 0;
                break;
            case Reference.OS_MAX:
                idx = windowLength - 1;
                break;                 
            }
        return idx;
    }
    
}
