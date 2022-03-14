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
import com.qmul.imageTransformation.Transformation;
import com.qmul.imgUtil.ImageUtil;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SmoothingFilter extends AbstractCommandFilter{
    private double kernelDivisor = 0.0;
    private final double[][] selectedKernel;
    private final boolean enableGrayScale;
    
    private static final double[][] KERNEL_BOX                = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
    private static final double[][] KERNEL_WEIGHTED_AVERAGE   = {{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};
    private static final double[][] KERNEL_GAUSSIAN           = {{0.3679, 0.6065, 0.3679}, {0.6065, 1.0, 0.6065}, {0.3679, 0.6065, 0.3679}};
    
    public SmoothingFilter(PathPanel panel, BufferedImage bi, String kernelName, boolean enableGrayScale) {
        super(panel, bi);
        this.commandName = String.format("Smooth filter using %s kernel", kernelName);        
        this.selectedKernel = selectKernel(kernelName);
        this.kernelDivisor  = selectKernelDivisor(kernelName);
        Reference.filterFlag = true;
        this.enableGrayScale = enableGrayScale;
    }
  /**
   * This method provides override transform which is used to execute the command.
   * the processed buffered image will be added to the panel.
   */     
    @Override
    protected void transform() {
        FImagePath transformedImage = null;
        try {
            transformedImage = new FImagePath(smoothingProcess(this.bufferImg, this.imgWidth, this.imgHeight, this.selectedKernel, this.kernelDivisor, this.enableGrayScale));
        } catch (IOException ex) {
            Logger.getLogger(SmoothingFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (transformedImage != null){
            transformedImage.setCurrentAngle(this.angle);
            panel.add(transformedImage);
        }
    }    
 
  /**
   * This method apply smoothing filter to the buffered image
   * 
   * @param channels     image rgb channels
   * @param width        image width
   * @param height       image height
   * @param kernel          selected kernel
   * @param kernelDivisor   kernel divisor 
   * @param enableGrayScale output grayscale
   */     
    private BufferedImage smoothingProcess(BufferedImage bi, int width, int height, double[][] kernel, double kernelDivisor, boolean enableGrayScale) throws IOException{
        int kernelWidth = kernel.length;
        int kernelHeight = kernel[0].length;
        BufferedImage convolutedImage;
        int[][][] image = ImageUtil.convertImageToMatrix(bi);      
        int[][] redConv   = Transformation.convolution(image[0], width, height, kernel, kernelWidth, kernelHeight, "None", kernelDivisor);
        int[][] greenConv = Transformation.convolution(image[1], width, height, kernel, kernelWidth, kernelHeight, "None", kernelDivisor);
        int[][] blueConv  = Transformation.convolution(image[2], width, height, kernel, kernelWidth, kernelHeight, "None", kernelDivisor);
        int[][] alphaConv = Transformation.convolution(image[3], width, height, kernel, kernelWidth, kernelHeight, "None", kernelDivisor);
        if (enableGrayScale == true){
            convolutedImage = ImageUtil.convertRgbToGrayscale(redConv, greenConv, blueConv);       
        }
        else{
            convolutedImage = ImageUtil.convertArrayToBufferedImg(redConv, greenConv, blueConv, alphaConv);        
        }
        return convolutedImage;
    }    

  /**
   * This method get the selected kernel
   * 
   * @param kernelName     selected kernel
   */       
    private double[][] selectKernel(String kernelName){
        
       double[][] kernel = null;       
        switch(kernelName){
            case Reference.SMOOTH_BOX:
                kernel = KERNEL_BOX;
                break;
            case Reference.SMOOTH_WEIGHTED_AVERAGE:
                kernel = KERNEL_WEIGHTED_AVERAGE;
                break;                 
            case Reference.SMOOTH_GAUSSIAN:
                kernel = KERNEL_GAUSSIAN;
                break;                  
            }
        return kernel;
    }
    
  /**
   * This method get the selected kernel divisor
   * 
   * @param kernelName     selected kernel
   */       
    private double selectKernelDivisor(String kernelName){
        
       double kDiv = 0.0;       
        switch(kernelName){
            case Reference.SMOOTH_BOX:
                kDiv = 9.0;
                break;
            case Reference.SMOOTH_WEIGHTED_AVERAGE:
                kDiv = 16.0;
                break;                 
            case Reference.SMOOTH_GAUSSIAN:
                kDiv = 4.8976;
                break;                  
            }
        return kDiv;
    }
}
