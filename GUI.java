/* Derek Saffie
 * 12/10/2015
 * CSC2620
 * GUI.java
 * Class is a GUI to display the game, as well as the interactive controls.
 */

package breakout;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI extends JFrame {
    private Game curGame; //The current game
    private JPanel controls; //Panel that contains game buttons
    private JButton quit; //Button to quit the program
    private JButton play; //Button to play the game
    private JLabel hint; //Label to display hint to play game
    
    public GUI() {
        //Call superclass' constructor with name of frame
        super("Breakout Version 1.0");
        
        //Set the frame layout to border layout
        setLayout(new BorderLayout());    
        curGame = new Game();   
        
        //Initialize the button to quit the game
        quit = new JButton("QUIT");
        //Add action listener to quit button
        quit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Check if the quit button was pressed; if so exit program
                if(e.getSource()==quit)
                    System.exit(0);
            }
        });
        
        //Initialize the button to play the game
        play = new JButton("PLAY");
        //Add action listener to play button
        play.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //Check if the play button was pressed; if so start a new game
               if(e.getSource()==play)
                   //Start the current game
                   curGame.start();
            }
        });
        
        //Initalize the hint label to tell user how to start new game
        hint = new JLabel(" Press Play to Start a New Game");
        
        //Set the score label's font to plain, Serif, and size 12
        hint.setFont(new Font("Serif", Font.PLAIN, 12));
        
        //Initialize, setup, and add components to the control panel
        controls = new JPanel();
        controls.setLayout(new GridLayout(1,3,5,5));
        controls.add(play);
        controls.add(hint);
        controls.add(quit);
        
        //Set the frame to not resizable
        this.setResizable(false);
        
        //Add both panels to the GUI
        add(controls, BorderLayout.NORTH);
        add(curGame, BorderLayout.CENTER);
    }
}
