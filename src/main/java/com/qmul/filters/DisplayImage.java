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

public class DisplayImage extends AbstractCommandFilter{
    
    public DisplayImage(PathPanel panel, BufferedImage bi) {
        super(panel, bi);
        this.commandName = "Display Image";
    }
  
  /**
   * This method provides override transform to add loaded buffered image to the panel 
   */     
    @Override
    protected void transform() {
        panel.add(new FImagePath(this.bufferImg));
    }    
}
