/* 
 * Description: Image processing application
 * Author: Mehrab Firouzi Moghadam
 * Date: 26/03/2021  
 */
package com.qmul.model;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;

public class FImagePath {
    
    private double currentAngle;
    private BufferedImage bufferedImage;
    
    public FImagePath(BufferedImage bufferedImage){
        this.bufferedImage = bufferedImage;
    } 

    /**
    * This method return the image
    * 
    * @return buffered image
    */       
    public BufferedImage getImage() {
       return bufferedImage;
    }      
    
    /**
    * This method return the current rotated angle
    * 
    * @return double
    */  
    public double getCurrentAngle() {
        return this.currentAngle;
    }
    
    /**
    * This method return the image width
    * 
    * @return int
    */      
    public int getImageWidth(){
        return this.bufferedImage.getWidth();
    }
    
    /**
    * This method return the image height
    * 
    * @return int
    */      
    public int getImageHeight(){
        return this.bufferedImage.getHeight();
    }
    
    /**
    * This method set the rotation angle
    * 
    */      
    public void setCurrentAngle(double angle) {
        this.currentAngle = angle;
    }    
}
