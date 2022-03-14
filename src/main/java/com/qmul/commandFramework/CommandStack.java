/* 
 * Description: This class creats a stack of commands to store the state of each command
 * 
 * Author: Mehrab Firouzi Moghadam
 * Reference: https://www.developer.com/design/article.php/3720566/Working-With-Design-Patterns-Memento.htm
 * Date: 26/03/2021  
 */
package com.qmul.commandFramework;

import java.util.LinkedList;

public class CommandStack {
    private LinkedList<ICommand> commandStack = new LinkedList<ICommand>();
    private LinkedList<ICommand> redoStack =  new LinkedList<ICommand>();
  /**
   * This method provides execute
   *
   */ 
    public void execute(ICommand command) {
       command.execute();
       commandStack.addFirst(command);
       redoStack.clear();
    }
  /**
   * This method provides undo
   *
   */ 
    public void undo() {
       if (commandStack.isEmpty())
          return;
       ICommand command = commandStack.removeFirst();
       command.undo();
       redoStack.addFirst(command);
    }
  /**
   * This method provides redo
   *
   */ 
    public void redo() {
       if (redoStack.isEmpty())
          return;
       ICommand command = redoStack.removeFirst();
       command.redo();
       commandStack.addFirst(command);
    }
}
