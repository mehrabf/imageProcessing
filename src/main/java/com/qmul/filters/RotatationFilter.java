/* 
 * Description: Image processing application
 * Author: Mehrab Firouzi Moghadam
 * Date: 26/03/2021  
 */
package com.qmul.filters;

import com.qmul.commandFramework.AbstractCommandFilter;
import com.qmul.model.FImagePath;
import com.qmul.model.PathPanel;
import java.awt.image.BufferedImage;

public class RotatationFilter extends AbstractCommandFilter{
    private double sin, cos;
    private final double coord[] = new double[2];
    private int newWidth, newHeight;
    private double originX, originY;
    private boolean rotationDirection = false;
    
    public RotatationFilter(PathPanel panel, BufferedImage bi, double angle, boolean bEnableDistortionEffect, boolean bClockwise) {
        super(panel, bi);
        this.bufferImg = bi;
        this.commandName = String.format("Rotation by %f", angle);            
        
        // Check if the distortion option is enabled then use the angle and make sure the image starts from previous angle
        // Otherwise the image rotation starts from angle = 0 and then added new requested angle the previous
        if (bEnableDistortionEffect == true){
            this.angle = angle;
        }
        else{
            this.angle += angle;
        }       
        // Ignore negative angles in degree
        this.angle = Math.abs(this.angle)% 360;
        this.commandName = String.format("new rotation by %f", this.angle);            
        calculateImageSize(this.angle);
        this.rotationDirection = bClockwise;
    }
  /**
   * This method provides override transform which is used to execute the command.
   * the processed buffered image will be added to the panel.
   */     
    @Override
    public void transform()
    {
        FImagePath transformedImage;
        if (this.rotationDirection == true){
            transformedImage = new FImagePath(rotateClockwise());
        }
        else{
            transformedImage = new FImagePath(rotateAntiClockwise());
        }
        if (transformedImage == null){
            System.out.println("transformed Image is null!");
        } else {
            transformedImage.setCurrentAngle(this.angle);
            panel.add(transformedImage);
        }
    }
    
  /**
   * This method rotate image in clockwise
   * 
   */     
    private BufferedImage rotateClockwise(){
        BufferedImage rotatedImage = new BufferedImage(this.newWidth, this.newHeight, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < this.newWidth; x++) {
            for (int y = 0; y < this.newHeight; y++) {
                double x0 = x - this.originX;
                double y0 = y - this.originY;
                int xx = (int) ((x0 * this.cos ) + (y0 * this.sin) + this.originX);
                int yy = (int) ((y0 * this.cos ) - (x0 * this.sin) + this.originY);
                if (xx >= 0 && xx < this.imgWidth && yy >= 0 && yy < this.imgHeight) {
                    rotatedImage.setRGB(x, y, this.bufferImg.getRGB(xx, yy));
                }
            }
        }
        return rotatedImage;
    }
    
  /**
   * This method rotate input in anti clockwise direction
   */     
    private BufferedImage rotateAntiClockwise(){
        BufferedImage rotatedImage = new BufferedImage(this.newWidth, this.newHeight, BufferedImage.TYPE_INT_ARGB);
        for (int row = 0; row < this.newWidth; row++) {
            for (int col = 0; col < this.newHeight; col++) {
                double x0 = row - this.originX;
                double y0 = col - this.originY;
                int xx = (int) ((x0 * this.cos )- (y0 * this.sin) + this.originX);
                int yy = (int) ((y0 * this.cos) + (x0 * this.sin) + this.originY);
                if (xx >= 0 && xx < this.imgWidth && yy >= 0 && yy < this.imgHeight) {
                    rotatedImage.setRGB(row, col, this.bufferImg.getRGB(xx, yy));
                }
            }
        }
        return rotatedImage;
    }      
  /**
   * This method calculate the image size
   * 
   * @param angle     requested angle
   */     
    private void calculateImageSize(double angle){
        double angleRadians;             
        angleRadians = Math.toRadians(angle);
        this.sin = Math.sin(angleRadians);
        this.cos = Math.cos(angleRadians); 
        
        if (angle == 90 || angle == 270)
        {
           //The width and length will be swapped for right angle rotations.
           this.newWidth = this.imgHeight;
           this.newHeight = this.imgWidth;               
        }
        else if (angle == 180 || angle == 360){
                // The size of image will be same for 180 degrees rotations.
            this.newWidth = this.imgWidth;
            this.newHeight = this.imgHeight;             
        }
        else{ 
            
            // Calculate the length and width of the output image. This
            // equation is used from International Image Interoperability Framework (IIIF)
            //this.newWidth  = (int) Math.floor( Math.abs((imW * this.cos)) + Math.abs((imL * this.sin)));
            //this.newHeight = (int) Math.floor( Math.abs((imL * this.cos)) + Math.abs((imW * this.sin)));   
            this.newWidth  = this.imgHeight;
            this.newHeight = this.imgWidth; 
        }
        this.originX = 0.5 * this.imgWidth;     // point to rotate about
        this.originY = 0.5 * this.imgHeight;     // center of image
    }
}
