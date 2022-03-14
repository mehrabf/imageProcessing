/* 
 * Description: This class provides an abstract for with a command strucure.
 * Author: Mehrab Firouzi Moghadam
 * Reference: https://www.developer.com/design/article.php/3720566/Working-With-Design-Patterns-Memento.htm
 * Date: 26/03/2021  
 */
package com.qmul.commandFramework;

import com.qmul.model.*;
import java.awt.image.BufferedImage;

abstract public class AbstractCommandFilter implements ICommand{
    private Momento memento;
    protected BufferedImage bufferImg;    
    protected PathPanel panel;
    protected String commandName;
    protected int imgWidth, imgHeight;
    protected double angle = 0;
    
    public AbstractCommandFilter()
    {     
    }
    
  /**
   * This constructor is used to to get PathPanel and load an image to this model.
   *
   * @param panel the given panel.
   * @param bi  the buffered Image.
   */    
    public AbstractCommandFilter(PathPanel panel, BufferedImage bi)
    {
        this.panel = panel;  
        if (bi == null)
        {
            this.bufferImg = panel.getImage();
            if (this.bufferImg == null)
            {
                System.out.println("No Image selected!");
            }                      
        }
        this.bufferImg = panel.getImage() == null ? bi : panel.getImage();
        this.angle = panel.getCurrentAngle();
        this.imgWidth = panel.getCurrentImageWidth();
        this.imgHeight = panel.getCurrentImageHeight();
    }
    
    abstract protected void transform(); 
  /**
   * This method is used to override the execute commands
   *
   */         
    @Override
    public void execute() {
        this.memento = panel.getMomento();
        transform();
        System.out.println(this.commandName); 
    }
  /**
   * This method provides override undo
   *
   */ 
    @Override
    public void undo() {
       Momento undoMemento = panel.getMomento();
       panel.setMomento(memento);
       memento = undoMemento;
       System.out.println("Undo " + this.commandName); 
    }
  /**
   * This method provides override redo feature.
   *
   */ 
    @Override
    public void redo() {
         Momento redoMemento = panel.getMomento();
         panel.setMomento(memento);
         memento = redoMemento;
         System.out.println("Redo " + this.commandName); 
    }
}
