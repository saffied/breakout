/* Derek Saffie
 * 12/10/2015
 * CSC2620
 * Paddle.java
 * Class represents a paddle, a movable rectangle drawn to the screen, that is
 * moved by the player to keep ball from falling "off" the screen. Class is
 * able to check for collision with side walls, update it's position, as well
 * as draw itself.
 */

package breakout;

import java.awt.Color;
import java.awt.Graphics;

public class Paddle extends DrawableObject implements MovableObject {
    
    //Constructor sets up the physical specs of the paddle
    //Pre: none
    //Post: Paddle initialized to proper specs
    public Paddle(Color color, int x, int y, int w, int h) {
        //Call the superclass' constructor
        super(color, x, y, h, w);
    }
    
    //This method draws a paddle object with the correct specifications.
    //Pre: none
    //Post: paddle drawn to g
    @Override
    public void draw(Graphics g) {
        //Draw paddle as a filled 3D rectangle
        g.fill3DRect((int)super.getXPos(), (int)super.getYPos(), super.getWidth(), super.getHeight(), true);
    }

    //This method handles the collisions between the sides of the paddle and the
    //side walls of the game.
    //Pre: none
    //Post: returns true if there is a collision; false otherwise
    public boolean collisionHandler(int xD, int yD) {
        //Checks if the left side of the paddle will be moved beyond the left
        //bound, or if the right side of the paddle will be moved beyond the
        //right bound; if so return false
        if(getXPos()+xD<0||getXPos()+getWidth()+xD>500)
            return false;
        return true;   
    }

    //This method moves the paddle either left or right, depending or arguments.
    //Pre: none
    //Post: if there is no collision paddle's position is moved
    @Override
    public void move(int distanceTraveled) {
        //Checks if there is a collision
        if(collisionHandler(distanceTraveled, 0))
            //If not, change the position accordingly
            changePosition(getXPos()+distanceTraveled, getYPos());
    }
}
