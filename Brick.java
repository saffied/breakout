/* Derek Saffie
 * 12/10/2015
 * CSC2620
 * Brick.java
 * This class represents a single brick in the game. A brick has a hardness, a
 * width, a height, a color, and an x,y position. The class contains methods to 
 * hit a block and check if a block has been broken. 
 */

package breakout;

import java.awt.Color;
import java.awt.Graphics;

public class Brick extends DrawableObject {
    private int hardness; //Hardness of the brick
    private int numHits = 0; //Number of times the brick has been hit; set to 0

    //Constructor initializes a brick with the correct specifications.
    //Pre: none
    //Post: brick is initialized with proper fields
    public Brick(Color color, int x, int y, int h, int w, int hard) {
        //Call superclass constructor
        super(color, x, y, h, w);
        //Set hardness
        hardness = hard;
    }
    
    //Method checks if a block has been broken.
    //Pre: none
    //Post: returns true if block is broken
    public boolean blockBroken() {
        return numHits>=hardness;
        
    }
    
    //Method overrides the superclass method for an object to be able to 
    //draw itself.
    //Pre: none
    //Post: a filled rectangle is drawn to the Graphics argument
    @Override
    public void draw(Graphics g) {
        //Draw solid rectangle with brick's coordinates and size
        g.fillRect((int)getXPos(), (int)getYPos(), getWidth(), getHeight());
    }
    
    //This method adds another hit to the total amount of times a brick has 
    //been hit.
    //Pre: none
    //Post: numHits is incremented by one
    public void blockHit() {
        numHits++;
    }
    
    //This method gets the hardness of the brick.
    //Pre: none
    //Post: hardness is returned
    public int getHardness() {
        return hardness;
    }
}
