/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package s4.animator.utils;

import java.security.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dawson
 */
public class Utils {
  //Timestamp
  public static long convertDateToTimestamp(String dateStr) throws ParseException {
    SimpleDateFormat datetimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    Date date = datetimeFormatter.parse(dateStr);
    
    return date.getTime() / 1000L;
  }
  
}
