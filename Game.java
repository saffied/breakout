/* Derek Saffie
 * 12/10/2015
 * CSC2620
 * Game.java
 * This class represents an instance of a single game of Breakout. The class
 * contains the elements necessary for a game, including the paddle, ball, and 
 * three rows of colored bricks. The class has the ability to generate random
 * bricks, calculate corrent score, calculate the number of remaining balls, 
 * paint the screen, and reset the ball and paddle.
 */

package breakout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Game extends JPanel implements Runnable {
    
    private Paddle paddle; //The paddle moved by the player
    private Ball ball; //The ball 
    private ArrayList<Brick> bricks; //ArrayList of bricks in game
    private Thread newThread; //Thread to run game
    private int maxScore; //The maximum possible score if all bricks broken
    private int score; //Game's current score
    private int numBalls; //Number of balls remaining
    private long time; //Current system time
    private boolean gameOver; //Stores if the game is over
    private boolean showGreeting = true; //Stores if a greeting should be shown
    private boolean showBlocks = false; //Stores if blocks should be shown
    private JPanel panel; //Panel to store important game information
    private JLabel ballLabel; //Label to display number of remaining balls
    private JLabel scoreLabel; //Label to display current score

    //Constructor sets the layout of the panel, sets the elements of the game,
    //and sets up keyboard input.
    //Pre: none
    //Post: initial elements and game play set up
    public Game() {
        //Set background to Black
        setBackground(Color.BLACK);
        //Initialize the single paddle to required specs
        paddle = new Paddle(Color.RED, 215, 420, 70, 20);
        //Initialize the ball to be in line with center of the paddle
        ball = new Ball(Color.WHITE, 243, 400, 15, 15); //243, 400
        //Initialize the array of the game's bricks
        bricks = new ArrayList<>();
        //Generate random bricks for the new game
        generateRandomBricks();
        
        //Set layout of panel to a border layout
        setLayout(new BorderLayout());
        //Initialize panel
        panel = new JPanel();
        //Add panel to north sector of the class panel
        add(panel, BorderLayout.NORTH);
        //Set the layout of panel to a 1 row by 2 column grid layout
        panel.setLayout(new GridLayout(1,2));
        
        //Initialize ballLabel to display proper text for number of balls
        ballLabel = new JLabel("Remaining Balls: 3" );
        //Set the font of ballLabel to Serif, bold the font, and size it to 20
        ballLabel.setFont(new Font("Serif", Font.BOLD, 20));
        //Add ballLabel to panel
        panel.add(ballLabel);
        
        //Initialize scoreLabel to display proper text for current score
        scoreLabel = new JLabel("                          Score: " + score);
        //Set the font of scoreLabel to Serif, bold, and size 20
        scoreLabel.setFont(new Font("Serif", Font.BOLD, 20));
        panel.add(scoreLabel);
        
        //Create input map for keyboard input
        InputMap inMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        //Create action map to get required action based on keyboard input
        ActionMap actMap = getActionMap();
        
        //Put the left arrow key in the input map
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT,0), "left");
        //Put key string for left arrow key in action map
        actMap.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Move paddle three pixels to the left
               paddle.move(-3);
            }
        });
        
        //Put the right arrow key in the input map
        inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT,0), "right");
        //Put key string for right arrow key in the action map
        actMap.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Move paddle three pixels to the right
               paddle.move(3);
            }
        });
    } 
    
    //Method overrides the superclass' paintComponent method. This method paints
    //all the elements of the game on the panel.
    //Pre: none
    //Post: all elements are painted
    @Override
    public void paintComponent(Graphics g) {
        //Call superclass' constructor 
        super.paintComponent(g);
        
        //Check if the greeting needs to be displayed
        if(showGreeting) {
            //Paint text with proper formatting to give game instructions
            g.setColor(Color.WHITE);
            g.setFont(new Font("Times Roman", Font.BOLD, 50));
            g.drawString("BREAKOUT", 110, 100);
            g.setFont(new Font("Times Roman", Font.BOLD, 25));
            g.drawString("Use Arrow Keys to Move Paddle", 65, 150);
            g.drawString("Goal: Break All Bricks With 3 Balls", 50, 180);
        }
        
        //Set color of g to the paddle's color
        g.setColor(paddle.getColor());
        //Draw paddle
        paddle.draw(g);
        
        //Set color of g to the ball's color
        g.setColor(ball.getColor());
        //Draw ball
        ball.draw(g);
        
        //Check if the blocks should be shown
        if(showBlocks) {
            //Loop through the ArrayList of bricks and draw each one with the
            //correct color
            try {
                for(Brick i : bricks) {
                    g.setColor(i.getColor());
                    i.draw(g);
                }
            }
            //Catches exceptions from a brick being deleted during access
            catch(ConcurrentModificationException e) {
                //Do nothing
            }
        }
        
        //Check if game is over
        if(gameOver) {
            //Paint game over message with proper formatting
            g.setColor(Color.WHITE);
            g.setFont(new Font("Times Roman", Font.BOLD, 50));
            g.drawString("GAME OVER!", 100, 300);
        }
    }
    
    
    //This method runs the game loop to move the ball, make needed changes to
    //labels, and repaint the current state of the game.
    //Pre: none
    //Post: state of game updated
    @Override
    public void run() {
        //Loop runs while the game is not over
        while(!gameOver) {
            //Handle any collision the ball may have
            ball.collisionHandler(bricks, paddle, 0, 0);
            //Calculate the game's current score
            calculateScore();
            //Change the score label to reflect the current score
            scoreLabel.setText("                          Score: " + score);
            //Get the current system time in nanoseconds
            long curTime = System.nanoTime();
            //Calculate the change in time from the last time recorded
            long deltaTime = curTime-time;
            //Set the time to the current system time
            time = curTime;
            //Move the ball according to the change in time
            ball.move((int)deltaTime);
            //Check if the ball has been lost
            if(ball.ballLost()) {
                //Decrement the number of remaining balls by one
                numBalls--;
                //Update the ball label text
                ballLabel.setText("Remaining Balls: " + numBalls);
                //Reset the ball and padle
                reset();
            }
            //Check if game is over
            isGameOver();
            //Repaint to reflect changes in game state
            repaint();
        }
    }
    
    //This method checks if the game is over. The game ends when the player
    //runs out of balls, or there are no remaining bricks.
    //Pre: none
    //Post: true returned if game over; false otherwise
    private void isGameOver() {
        //Check if there are 0 or less balls
        if(numBalls<=0)
            gameOver = true;
        //Check if there are no bricks remaining
        else if(bricks.isEmpty())
            gameOver = true;
    }
    
    //This method calculates the current score based upon the hardness of each
    //brick broken. The score for breaking a brick is equal to that brick's
    //hardness.
    //Pre: none
    //Post: score is updated
    private void calculateScore() {
        try {
            int newMaxScore = 0; //Max score for current bricks; set to 0
            //Loop through all remaining bricks and add their hardnesses
            for(Brick i : bricks) 
                newMaxScore += i.getHardness();
            //Calculate the current score using difference of max scores
            score = maxScore - newMaxScore;
        }
        //Catches exceptions from a brick being deleted during access
        catch(ConcurrentModificationException e) {
            //Do nothing
        }
    }
    
    //This method starts the game on a new thread and resets all needed fields.
    //Pre: none
    //Post: new game is started with fields reset
    public void start() {
        //Initialize a new thread
        newThread = new Thread(this);
        //Start the thread
        newThread.start();
        //Set time to the current system time
        time = System.nanoTime();
        //Reset the score to 0
        score = 0;
        //Reset the maximum score to 0
        maxScore = 0;
        //Reset number of remaining balls to 3
        numBalls = 3;
        //Reset the remaining ball labels
        ballLabel.setText("Remaining Balls: 3");
        //Set game to not over
        gameOver = false;
        //Reset the ball and paddle
        reset();
        //Clear the list of bricks in case not all bricks were broken
        bricks.clear();
        //Generate random bricks for the new game
        generateRandomBricks();
        //Set showGreeting to false; no need to display instructions again
        showGreeting = false;
        //Set showBlocks to true to show blocks on screen
        showBlocks = true;
    }
    
    //This method resets the ball and the paddle.
    //Pre: none
    //Post: paddle centered and ball centered above paddle
    private void reset() {
        //Set direction of the ball to up and to the right
        ball.move(1, -1);
        //Center the ball
        ball.changePosition(243, 400);
        //Center the paddle
        paddle.changePosition(215, 420);
    }
    
    //This method generates three rows of bricks for the game. Each brick has
    //a random width, color, and hardness. The bricks are sized so that each
    //row extends from the left side of the screen to the right, without being
    //over or under.
    //Pre: none
    //Post: bricks populated with randomly generated bricks
    private void generateRandomBricks() {
        int randR; //Stores the Red value
        int randG; //Stores the Green value
        int randB; //Stores the Blue value
        Random newRand = new Random(); //Random generator to get random numbers
        int randWidth; //Stores the randomly generated brick width
        int randHardness; //Stores the randomly generated brick hardness
        int yPosition = 100; //Stores y position of row; intialized to 100
        
        //Loop runs three times to generate three rows
        for(int i = 0; i<3; i++) {
            int nextXPos = 0; //Stores position of left corner of the next brick
            //Loop runs while the position of next brick is not beyond right side
            while(nextXPos<500) {
                //Loop generates a random color; runs while it is not too dark
                do {
                    //Random values for red, green, and blue are generated
                    randR = newRand.nextInt(250);
                    randG = newRand.nextInt(250);
                    randB = newRand.nextInt(250);
                } while (isColorBlack(randR, randG, randB));
                
                //Set the brick hardness to a random value between 1 and 3
                randHardness = newRand.nextInt(3)+1;
                //Sets the brick width to a random value between 30 and 99
                randWidth = newRand.nextInt(70) + 30;
                Brick newBrick; //Stores the newly created brick
                
                //Checks if there is enough room to generate random brick width
                //without the chance of the next brick having to be too small
                if(nextXPos+130<=500) {
                    //Initialize the new brick with correct specs
                    newBrick = new Brick(new Color(randR, randG, randB),
                        nextXPos, yPosition, 30, randWidth, randHardness);
                }
                //Else, if there is not room to randomly set brick width
                else {
                    //Initialize new brick to fill remaining row space
                    newBrick = new Brick(new Color(randR, randG, randB),
                            nextXPos, yPosition, 30, 500 - nextXPos, randHardness);
                }
                //Add the brick to bricks
                bricks.add(newBrick);
                //Add the brick's hardness to maximum possible score
                maxScore+=randHardness;
                //Update the nextXPos with width of the last brick
                nextXPos += newBrick.getWidth();
            }
            //Move the new row down 30 pixels
            yPosition+=30;
        }
    }
    
    //This method checks if the RGB color value will create a color too close
    //to black to be easily seen on the screen.
    //Pre: none
    //Post: returns true if color is too close to black; false otherwise
    private boolean isColorBlack(int r, int g, int b) {
        //Returns value of statement, all values are less than 75
        return (r<75&&g<75&&b<75);
    }
    
    //Method gets the curren score of the game.
    //Pre: none
    //Post: current score is returned
    public int getScore() {
        return score;
    }
}
