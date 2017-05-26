/* Derek Saffie
 * 12/10/2015
 * CSC2620
 * MovableObject.java
 * MovableObject is an interface for game objects that move. The interface 
 * contains an abstract method for the movement of the object.
 */

package breakout;

public interface MovableObject {
    //Class moves the object to the new x,y position
    public void move(int distanceTraveled);
}
