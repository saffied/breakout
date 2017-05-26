/* Derek Saffie
 * 12/10/2015
 * CSC2620
 * Breakout.java
 * The classic Breakout game! The single player game uses up to three balls and
 * a paddle in order to break as many bricks as possible. 
 */

package breakout;

import javax.swing.JFrame;

public class Breakout{

    public static void main(String[] args) {
        GUI gui = new GUI(); //Create and initialize a new GUI
        //Set gui default close operation to exit program
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set the gui to be visible
        gui.setVisible(true);
        //Set the size of the gui to be 500 x 500
        gui.setSize(500,500);
    }
}
