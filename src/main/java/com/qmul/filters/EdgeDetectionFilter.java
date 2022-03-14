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

public class EdgeDetectionFilter extends AbstractCommandFilter{
    private final double[][] selectedKernel;
    private final boolean enableGrayScale;
    
    private static final double[][] KERNEL_VERTICAL     = {{1, 0, -1}, {1, 0, -1}, {1, 0, -1}};
    private static final double[][] KERNEL_HORIZONTAL   = {{1, 1, 1}, {0, 0, 0}, {-1, -1, -1}};

    private static final double[][] KERNEL_SOBEL_V  = {{1, 0, -1}, {2, 0, -2}, {1, 0, -1}};
    private static final double[][] KERNEL_SOBEL_H  = {{1, 2, 1}, {0, 0, 0}, {-1, -2, -1}};

    private static final double[][] KERNEL_SCHARR_V = {{3, 0, -3}, {10, 0, -10}, {3, 0, -3}};
    private static final double[][] KERNEL_SCHARR_H = {{3, 10, 3}, {0, 0, 0}, {-3, -10, -3}};    
    
    public EdgeDetectionFilter(PathPanel panel, BufferedImage bi, String kernelName, boolean enableGrayScale) {
        super(panel, bi);
        this.commandName = String.format("Edgae Detection filter using %s kernel", kernelName);        
        this.selectedKernel = selectKernel(kernelName);
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
            transformedImage = new FImagePath(edgeDetectionProcess(this.bufferImg, this.imgWidth, this.imgHeight, this.selectedKernel, this.enableGrayScale));
        } catch (IOException ex) {
            Logger.getLogger(EdgeDetectionFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (transformedImage != null){
            transformedImage.setCurrentAngle(this.angle);
            panel.add(transformedImage);
        }
    }    

  /**
   * This method process edgeDetection on the buffered image using convolution
   * 
   * @param bi      buffered image
   * @param width   image width
   * @param height  image height
   * @param kernel  kernel array 2d
   * @param enableGrayScale enable the output image be in grayscale or rgb
   */     
    private BufferedImage edgeDetectionProcess(BufferedImage bi, int width, int height, double[][] kernel, boolean enableGrayScale) throws IOException{
        int kernelWidth = kernel.length;
        int kernelHeight = kernel[0].length;
        int kernelDivisor = 1;
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
   * This method return the selected kernel 
   * 
   * @param kernelName  kernel name
   */     
    private double[][] selectKernel(String kernelName){
        
       double[][] kernel = null;       
        switch(kernelName){
            case Reference.ED_H:
                kernel = KERNEL_VERTICAL;
                break;
            case Reference.ED_V:
                kernel = KERNEL_HORIZONTAL;
                break;
            case Reference.ED_SOBEL_V:
                kernel = KERNEL_SOBEL_V;
                break;
            case Reference.ED_SOBEL_H:
                kernel = KERNEL_SOBEL_H;
                break;
            case Reference.ED_SCHARR_V:
                kernel = KERNEL_SCHARR_V;
                break;   
            case Reference.ED_SCHARR_H:
                kernel = KERNEL_SCHARR_H;
                break;                 
            }
        return kernel;
    }
}
