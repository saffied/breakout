/* Derek Saffie
 * 12/10/2015
 * CSC2620
 * Ball.java
 * This class represents the ball in the game. The ball moves autonomously of 
 * the user's paddle movements. Ball collisions can break a brick, and a ball
 * can bounce off the solid sides.
 */

package breakout;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Ball extends DrawableObject implements MovableObject {
    int xDir; //The x direction the ball is moving in
    int yDir; //The y direction the ball is moving in
    double speed = .00008; //The speed the ball is moving

    //Constructor initializes the ball to the proper dimensions, color, and 
    //placement.
    //Pre: none
    //Post: the ball is initialized to proper specifications
    public Ball(Color _color, int x, int y, int h, int w) {
        super(_color, x, y, h, w);
    }
    
    //This method handles collisions between the ball and all the other elements
    //of the game. Collisions may change direction of ball, as well as break
    //bricks. 
    //Pre: none
    //Post: direction of ball and bricks are updated depending on collisions;
    //returns true if there was a collision
    public boolean collisionHandler(ArrayList<Brick> bricks, Paddle p, int xD, int yD) {
        //Check if the left wall is hit; if so change x direction
        if(getXPos() + xDir < 0)
            xDir *= -1;
        //Check if the right wall is hit; if so change x direction
        else if(getXPos() + getWidth() + xDir > 500)
            xDir *= -1;
        //Check if the top boundary is hit; if so change y direction
        else if(getYPos() + yDir < 20)
            yDir *= -1;
        //Check if the top of the paddle was hit; if so change y direction
        else if(topBoundCollision(p))
            yDir *= -1;
        //Check either side of the paddle was hit; if so change x direction
        else if(leftBoundCollision(p)||rightBoundCollision(p))
            xDir *= -1;
        //Else, check for collisions with bricks
        else {
            //Loop through all bricks and check for collisions
            for(int i = 0; i<bricks.size(); i++) {
                //Check if the ball collided with the top or bottom of a brick
                if(topBoundCollision(bricks.get(i))||bottomBoundCollision(bricks.get(i))) {
                    //A block was hit; check if the brick was broken
                    if(brickHit(bricks.get(i))) {
                        //Remove the brick from the list of bricks
                        bricks.remove(bricks.get(i));
                    }
                    //Otherwise the brick is not broken; change y direction
                    else
                        yDir *= -1; 
                }
                //Check if the ball collided with either side of a brick
                else if(leftBoundCollision(bricks.get(i))||rightBoundCollision(bricks.get(i))) {
                    //A brick was hit; check if the brick was broken
                    if(brickHit(bricks.get(i))) {
                        //Remove the brick from the list of bricks
                        bricks.remove(bricks.get(i));
                    }
                    //Otherwise the brick is not broken; change x direction
                    else
                        xDir *= -1; 
                }    
            }
        }
        return true;
    }
    
    //This method checks if hitting a brick has broken it.
    //Pre: o is initialized
    //Post: return true if block is broken; false otherwise
    private boolean brickHit(Brick o) {
        //Check if o has been initialized
        if(o!=null) {
            //Call blockHit to record hit
            o.blockHit();
            //Check if the block has been broken
            if(o.blockBroken())
                return true;
        }
        return false;
    }
    
    //Method checks for a collision with the top bound of a DrawableObject. There
    //is an allowance for .1 pixels as ball does not move in integer pixels.
    //Pre: none
    //Post: true is returned if there was a collision; false otherwise
    private boolean topBoundCollision(DrawableObject o) {
        //Check if the y position of the ball is near the top of the object
        if(getYPos() + getHeight() >= o.getYPos()-.1 && getYPos() <= o.getYPos() + .1) {
            //Check if the x position of the ball is within the width of the object
            if(getXPos() + getWidth() >= o.getXPos() + .1 && getXPos() <= o.getXPos() + o.getWidth() -.1)
                return true;
        }
        return false;
    }
    
    //Method checks for a collision with the bottom bound of a DrawableObject.
    //There is an allowance for .1 pixels as ball does not move in integer pixels.
    //Pre: none
    //Post: true is returned if there was a collision; false otherwise
    private boolean bottomBoundCollision(DrawableObject o) {
        //Check if the y position of the ball is near the bottom of the object
        if(getYPos() + getHeight() >= o.getYPos() + o.getHeight() - .1 && getYPos() <= o.getYPos() + o.getHeight() + .1) {
            //Check if the x position of the ball is within the width of the object
            if(getXPos() >= o.getXPos() - .1 && getXPos() <= o.getXPos() + o.getWidth() + .1)
                return true;
        }
        return false;
    }
    
    //Method checks for a collision with the left bound of a DrawableObject.
    //There is an allowance for .1 pixels as ball does not move in integer pixels.
    //Pre: none
    //Post: true is returned if there was a collision; false otherwise
    private boolean leftBoundCollision(DrawableObject o) {
        //Check if the y position of the ball is near the y position of the object
        if(getYPos() >= o.getYPos() - .1 && getYPos() <= o.getYPos() + o.getHeight() + .1) {
            //Check if the x position of the ball is near the x position of left side
            if(getXPos() + getWidth() >=o.getXPos()-.1 && getXPos() + getWidth() <=o.getXPos()+.1)
                return true;
        }
        return false;
    }
    
    //Method checks for a collision with the right bound of a DrawableObject.
    //There is an allowance for .1 pixels as ball does not move in integer pixels.
    //Pre: none
    //Post: true is returned if there was a collision; false otherwise
    private boolean rightBoundCollision(DrawableObject o) {
        //Check if the y position of the ball is near the y position of the object
        if(getYPos() >= o.getYPos() - .1 && getYPos() <= o.getYPos() + o.getHeight() + .1) {
            //Check if the x position of the ball is near the x position of the right side
            if(getXPos()>=o.getXPos() + o.getWidth() -.1 && getXPos()<=o.getXPos()+o.getWidth()+.1)
                return true;
        }
        return false;        
    }

    //This method implements the interface's move method. The ball is moved 
    //base upon the direction it is travelling, its speed, and the amount of
    //time that has changed since the last time it was moved.
    //Pre: none
    //Post: ball is moved according to movement factors
    @Override
    public void move(int timePassed) {
        //Calculate the change in distance using the amount of time passed
        double delta = .00125 * timePassed;
        //Change the ball's x and y position
        super.changePosition(getXPos()+(delta*xDir)*speed, getYPos()+(delta*yDir)*speed);
    }
    
    //This method draws a ball object with the correct specifications.
    //Pre: none
    //Post: ball drawn to g
    @Override
    public void draw(Graphics g) {
        //Draw ball as a solid oval
        g.fillOval((int)getXPos(), (int)getYPos(), getWidth(), getHeight());
    }
    
    //This method sets x and y direction the ball will move in.
    //Pre: none
    //Post: the x and y directions are changed
    public void move(int x, int y) {
        //Set xDir and yDir to method's arguments
        xDir = x;
        yDir = y;
    }
    
    //This method checks if the ball has been lost. A ball is considered lost
    //if it falls below the bottom bound of the frame the game is in.
    //Pre: none
    //Post: true is returned if ball is lost; false otherwise
    public boolean ballLost() {
        return (getYPos()>=440);
    }
    
}
