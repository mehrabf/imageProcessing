/* 
 * Description: Image processing application
 * Author: Mehrab Firouzi Moghadam
 * Date: 26/03/2021  
 */
package com.qmul.model;

import java.util.*;

public class Momento {
   private List<FImagePath> paths;

   public Momento(List<FImagePath> paths) {
      this.paths = paths;
   }

   public List<FImagePath> getPaths() {
      return paths;
   }
}
