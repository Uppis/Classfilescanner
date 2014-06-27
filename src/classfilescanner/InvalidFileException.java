/*
 * InvalidFileException.java
 * 
 * Created on 28.6.2007, 15:39:03
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package classfilescanner;

/**
 *
 * @author z705692
 */
public class InvalidFileException extends Exception {
    /**
     * Constructs an instance of <code>InvalidFileException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public InvalidFileException(String msg) {
        super(msg);
    }
}
