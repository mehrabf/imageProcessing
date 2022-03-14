/* 
 * Description: Image processing application
 * Author: Mehrab Firouzi Moghadam
 * Date: 26/03/2021  
 */
package com.qmul.imageTransformation;

public class Transformation {
    
    public Transformation(){

    }
    
  /**
   * This method apply smoothing filter to the buffered image
   * 
   * @param input           image rgb channels
   * @param width           image width
   * @param height          image height
   * @param kernel          selected kernel
   * @param kernelWidth     kernel width 
   * @param kernelHeight    kernel height
   * @param padding         padding type
   * @param kernelDivisor   kernel divisor
   * @return convoluted image
   */        
    public static int[][] convolution(int[][] input,  int width, int height,
                                           double[][] kernel, int kernelWidth, int kernelHeight, String padding, double kernelDivisor) {
        
        int left = kernelWidth / 2;
        int top = kernelHeight / 2;
        
        // Initailise 2d array
        int[][] output = new int[width][height];
        double convOutput;
        // perform pixel convolution
        for (int row = width - kernelWidth; row >= left ; row--) {
            for (int col = height - kernelHeight; col >= top ; col--) {   
                convOutput = 0.0;  
                for (int i = kernel.length - 1; i >= 0 ; i--) {
                    for (int j = kernel[0].length -1 ; j >= 0 ; j--) {
                        convOutput += (input[row + i - left][col + j - top] * kernel[i][j]);
                    }
                }
                output[row][col] = (int)(convOutput / kernelDivisor);
            }
        }
        return output;
    }      
}
