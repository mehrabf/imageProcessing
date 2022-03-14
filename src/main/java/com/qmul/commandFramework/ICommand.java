/* 
 * Description: Image processing application
 * Author: Mehrab Firouzi Moghadam
 * Date: 26/03/2021  
 */
package com.qmul.commandFramework;

public interface ICommand {
    public void execute();
    public void undo();
    public void redo();
}
