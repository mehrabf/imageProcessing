/* 
 * Description: Image processing application
 * This class extends the JPanel to display the image
 * Author: Mehrab Firouzi Moghadam
 * Date: 26/03/2021  
 */
package com.qmul.frame;

import java.awt.*;
import javax.swing.*;
import com.qmul.model.*;

public class ImagePanel extends JPanel {

    private final IDisplay displayable;

    public ImagePanel(IDisplay displayable) {
        this.displayable = displayable;
    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, getSize().width, getSize().height);
        displayable.displayOn(g);
    }
}
