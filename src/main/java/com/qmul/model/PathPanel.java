/* 
 * Description: Image processing application
 * Author: Mehrab Firouzi Moghadam
 * Date: 26/03/2021  
 */
package com.qmul.model;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class PathPanel extends Observable implements IMomentoOrigin, IDisplay {
    private List<FImagePath> paths = new ArrayList<FImagePath>();

    public List<FImagePath> paths() {
       return paths;
    }

    public void add(FImagePath path) {
       paths.add(path);
       changed();
    }
    
    /**
    * This method return the current image in the stack
    * 
    * @return buffered image
    */      
    @Override
    public BufferedImage getImage()
    {
        BufferedImage image = null;
        FImagePath path = getCurrentPath();
        if (path != null){
            image = path.getImage();
        }
        return image;
    }

    /**
    * This method display the current image
    * 
    */      
    @Override
    public void displayOn(Graphics graphics) {  
        FImagePath path = getCurrentPath();
        if (path != null){
            graphics.drawImage(path.getImage(), 0, 0, null);
        }
    }
    
    /**
    * This method display all the image append to the panel
    * 
    */    
    public void displayOn(Graphics graphics, boolean bAppend) {  
        for (FImagePath path: paths) {
            graphics.drawImage(path.getImage(), 0, 0, null);
        }     
    }

    /**
    * This method return the momento 
    * 
    * @return buffered image
    */       
    @Override
    public Momento getMomento() {
        List<FImagePath> pathCopies = new ArrayList<FImagePath>();
        pathCopies.addAll(paths);
        return new Momento(pathCopies);
    }

    /**
    * This method set the momento 
    * 
    */      
    @Override
    public void setMomento(Momento momento) {
       paths.clear();
       paths.addAll(momento.getPaths());
       changed();
    }

    /**
    * This method return the current image width 
    * 
    * @return int
    */      
    @Override
    public int getCurrentImageWidth() {
        int width = 0;
        FImagePath path = getCurrentPath();
        if (path != null){
            width = getCurrentPath().getImageWidth();
        }
        return width;
    }

    /**
    * This method return the current image height
    * 
    * @return int
    */      
    @Override
    public int getCurrentImageHeight() {
        int height = 0;
        FImagePath path = getCurrentPath();
        if (path != null){
            height = getCurrentPath().getImageHeight();
        }
        return height;
    }    

    /**
    * This method return the current angle
    * 
    * @return double
    */      
    @Override
    public double getCurrentAngle() {
        double angle = 0.0;
        FImagePath path = getCurrentPath();
        if (path != null){
            angle = getCurrentPath().getCurrentAngle();
        }
        return angle;
    }

    /**
    * This method set the current angle
    * 
    */      
    @Override
    public void setCurrentAngle(double angle) {
        FImagePath path = getCurrentPath();
        if (path != null){
            getCurrentPath().setCurrentAngle(angle);
        }
    }    

    /**
    * This method return the current path
    * 
    * @return FImagePath
    */    
    private FImagePath getCurrentPath(){
        FImagePath path = null;
        if (paths != null && !paths.isEmpty()) {
            path = paths.get(paths.size()-1);
        }   
        return path;
    }
    
    private void changed() {
       setChanged();
       notifyObservers();
    }


}