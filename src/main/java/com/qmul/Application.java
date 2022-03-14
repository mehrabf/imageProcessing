/* 
 * Description: Image processing application
 * Author: Mehrab Firouzi Moghadam
 * Date: 26/03/2021  
 */

package com.qmul;

import com.qmul.frame.ImageEditorFrame;
import java.awt.Dimension;

import javax.swing.*;

public class Application extends JDialog {
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ImageEditorFrame frm2 = new ImageEditorFrame();
                frm2.setPreferredSize(new Dimension(1500, 800));
                frm2.pack();
                frm2.setLocationRelativeTo(null);
                frm2.setVisible(true);

                
            }
        });
    }
}
