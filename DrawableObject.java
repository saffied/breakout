/* Derek Saffie
 * 12/10/2015
 * CSC2620
 * DrawableObject.java
 * Abstract class represents any object that can be drawn. Drawn objects all
 * require general information including the size, coordinate position, and 
 * color. Objects also have the ability to draw themselves. 
 */

package breakout;

import java.awt.Color;
import java.awt.Graphics;

public abstract class DrawableObject {
    private Color color; //Stores the color
    private double posX; //Stores the x position
    private double posY; //Stores the y position
    private int width; //Stores the width
    private int height; //Stores the height
    
    //Constructor initializes an object with a color, size, and coordinate 
    //position.
    //Pre: none
    //Post: object initialized with correct specifications
    DrawableObject(Color _color, int x, int y, int h, int w) {
        //Set the fields to their respective arguments
        color = _color; 
        posX = x;
        posY = y;
        height = h;
        width = w;   
    }
    
    //This method allows the position of an object to be changed given a new
    //x and y coordinate.
    //Pre: none
    //Post: object is moved to new x,y position
    public void changePosition(double x, double y) {
        posX = x;
        posY = y;
    }
    
    //Method gets the color of an object.
    //Pre: none
    //Post: returns color
    public Color getColor() {
        return color;
    }
    
    //Method gets the x position of an object.
    //Pre: none
    //Post: x position is returned
    public double getXPos() {
        return posX;
    }
    
    //Method gets the y position of an object.
    //Pre: none
    //Post: y position is returned
    public double getYPos() {
        return posY;
    }
    
    //Method gets the width of an object.
    //Pre: none
    //Post: width is returned
    public int getWidth() {
        return width;
    }
    
    //Method gets the height of an object.
    //Pre: none
    //Post: height is returned
    public int getHeight() {
        return height;
    }
    
    //Abstract method to draw an object
    abstract public void draw(Graphics g);
}
