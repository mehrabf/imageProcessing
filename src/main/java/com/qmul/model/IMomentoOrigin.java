/* 
 * Description: Image processing application
 * Author: Mehrab Firouzi Moghadam
 * Date: 26/03/2021  
 */
package com.qmul.model;

import java.awt.image.BufferedImage;

public interface IMomentoOrigin {
   
    public int getCurrentImageWidth();
    public int getCurrentImageHeight();     
    public double getCurrentAngle();
    public void setCurrentAngle(double angle);
    public BufferedImage getImage();
    public Momento getMomento();
    public void setMomento(Momento momento);
}
