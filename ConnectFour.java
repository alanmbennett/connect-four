/*
Program Name: Connect Four
Programmer: Alan Bennett
Creation Date: 10/21/2015
Last Modification Date: 11/3/2015
Description: An interactive Connect Four game. 
*/

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ConnectFour extends Application {
    
    public static int ROWS = 6, COLUMNS = 7; // Makes number of rows/columns global constants
    public static Paint GRAY = Color.web("#7F8C8D"), WHITE = Color.WHITE; // Sets global color constants
    
    public static void main(String[] args) {
        launch(args); // launches JavaFX program from start method
    }
   
    @Override
    public void start(Stage primaryStage) {
       
       Player p1 = new Player(), p2 = new Player(); // Player objects initialized
       Circle[][] cArray = new Circle[6][7]; // Declare a 6 x 7 array of Circles
       defaultPlayer(p1,p2); // Default information stored into Player object
       
       /* Setup grid - begin */
       GridPane grid = new GridPane(); // Create a new GridPane
       
       for (int r = 0; r < ROWS; r++) // Loop until there are 6 rows
       {
           RowConstraints rConstr = new RowConstraints(); // Make constraints for the row
           rConstr.setPercentHeight(150 / ROWS); // Set the percent height of the row
           grid.getRowConstraints().add(rConstr); // Add the row to grid
       }
       
       for (int c = 0; c < COLUMNS; c++) // Loop until there are 7 columns
       {
           ColumnConstraints cConstr = new ColumnConstraints(); // Make constraints for the column
           cConstr.setPercentWidth(150 / COLUMNS); // Set the percent of the column
           grid.getColumnConstraints().add(cConstr); // Add the column to grid
       }
       
       grid.setStyle("-fx-background-color: #F1C40F;"); // Make grid the color light green 
     
       /* Setup grid - end */
     
       /* Setup body and header - begin */
       HBox header = new HBox(); // Create a new VBox
       
       header.setPadding(new Insets(5)); // Set the padding of the header
       header.setSpacing(10); // Set the spacing of the header
       
       Text msg = new Text(); // Create a new Text
       msg.setFont(Font.font("Times New Roman", 30)); // Set the formatting for msg
       msg.setText(p1.getTurnMsg()); // Set the message to player 1's turn msg by default
       msg.setFill(p1.getColor()); // Set the message color to player 1's color
      
       BorderPane body = new BorderPane(); // Create a new BorderPane
       body.setCenter(grid); // Add grid to body
       body.setTop(header); // Add header to body
       /* Setup body and header - end */
     
       /* Scene setup - begin */
       Scene scene = new Scene(body,1000,1000); // Place body in the scene
       primaryStage.setScene(scene); // Place the scene in the stage
       primaryStage.setTitle("Connect Four"); // Title of game
       
       /** Setting icon for application - start **/
       Image appIcon = new Image(getClass().getResourceAsStream("ConnectFourIcon.png"));
       primaryStage.getIcons().add(appIcon);
       /** Setting icon for application - end **/
       
       Timeline bArray[] = new Timeline[4]; // Make a new timeline animation array for blinking
       
       primaryStage.setResizable(false); // Make the main window non-resizable
       primaryStage.show(); // Display the stage
       /* Scene setup - end */
       
       for (int c = 0; c < COLUMNS; c++) // Loop until 7 columns have been traversed
       {
           for (int r = 0; r < ROWS; r++) // Loop until 6 rows have been traversed
           {
                if (r < 5) {
                grid.add(cArray[r][c] = new Circle(50,GRAY),c,r); // Add the gray circle to the current grid row and column
                }
                else {
                grid.add(cArray[r][c] = new Circle(50,WHITE),c,r); // Add the white circle to the current grid row and column
                }
                GridPane.setHalignment(cArray[r][c],HPos.CENTER); // Align the circle in the center
                
                final int i = r, j = c; // save r and c as constants in order to use in inner class
          
                cArray[i][j].setOnMouseClicked(e-> {
                    click(cArray,p1,p2,bArray, msg, i, j); // handles chip placement and move/win checks for p1
                    click(cArray,p2,p1,bArray, msg, i, j); // handles chip placement and move/win checks for p2
                } ); // if a circle is clicked
           }
       }
       
       /* Buttons - Start */
       
       // Create new buttons
       Button closeButton = new Button("Close App"), instrButton = new Button("Instructions");
       Button resetButton = new Button("New Game"), nameButton = new Button("Change Name");
       
       instrButton.setOnMouseClicked(e-> { instr(); }); // Opens instructions window
       
       closeButton.setOnMouseClicked(e-> { Platform.exit(); }); // Exits program
       
       resetButton.setOnMouseClicked(e-> { 
   
           for (int i = 0; i < 4; i++)
           {
               if (bArray[i] != null)
               {
                    if (bArray[i].getStatus() == Animation.Status.RUNNING)
                      bArray[i].stop();
               }
           }

           for (int c = 0; c < COLUMNS; c++) // Loop until 7 columns have been traversed
            {
                for (int r = 0; r < ROWS; r++) // Loop until 6 rows have been traversed
                {
                    if (r < 5) // if r < 5
                        cArray[r][c].setFill(GRAY); // Make circle gray
                        
                    else // Otherwise
                        cArray[r][c].setFill(WHITE); // Make circle white
                }
           }
           
           p1.setTurn(true); // Make it player 1's turn
           p2.setTurn(false); // Put player 2 on standby
           msg.setText(p1.getTurnMsg()); // Change display message
           msg.setFill(p1.getColor()); // Change the color of message
       }); // Resets the game
       
       nameButton.setOnMouseClicked(e-> { nameMenu(p1,p2, msg); }); // Opens change name window
       
       // Add the buttons to the header
       header.getChildren().addAll(msg, instrButton, nameButton, resetButton, closeButton);
       
       /* Buttons - End */
    }
   
    public static void defaultPlayer(Player p1, Player p2) 
    {   
       p1.setTurn(true); // Player 1 given current turn
       p2.setTurn(false); // Player 2 is set to standby
       p1.setColor(Color.BLACK); // Make black the default color for p1
       p2.setColor(Color.web("#C0392B")); // Make red the default color for p2
       p1.setName("Player 1"); // Set the default player name for Player 1
       p2.setName("Player 2"); // Set the default player name for Player 2
    }
    
    public static void turnSwitcher(Player p1, Player p2) 
    {
        p1.changeTurn(); // Change p1's turn
        p2.changeTurn(); // Change p2' turn
    }
    
     public static boolean checkWin(Circle[][] cArray, Player p, Timeline[] bArray)
    {
        /* Check horizontally */
        for (int r = 0; r < ROWS; r++)
        {
            int count = 0;
            
                for(int c = 0; c < COLUMNS; c++)
                {
                    if (cArray[r][c].getFill() == p.getColor())
                    {
                        count++;
                        
                        if (count >= 4)
                        {
                            for (int i = 0; i < 4; i++)
                            {
                                bArray[i] = makeBlink(cArray, p, r, (c - i));
                                bArray[i].play();
                            }

                            return true;
                        }
                    }
                    else
                    {
                        count = 0; 
                    }
                }
        }
        
        /* Check vertically */
        for (int c = 0; c < COLUMNS; c++)
        {
            int count = 0;
            
                for(int r = 0; r < ROWS; r++)
                {
                    if (cArray[r][c].getFill() == p.getColor())
                    {
                        count++;
                        
                        if (count >= 4)
                        {
                            for (int i = 0; i < 4; i++)
                            {
                                bArray[i] = makeBlink(cArray, p, (r - i), c);
                                bArray[i].play();
                            }
                            
                            return true;
                        }
                    }
                    else
                        count = 0; 
                }
        }
        
        /* Check for diagonals facing left */
        for (int r = 0; r < 3; r++)
        {
            for (int c = 0; c < 4; c++)
            {
                if (cArray[r][c].getFill() == p.getColor() &&
                    cArray[r + 1][c + 1].getFill() == p.getColor() &&
                    cArray[r + 2][c + 2].getFill() == p.getColor() &&
                    cArray[r + 3][c + 3].getFill() == p.getColor())
                {
                    for (int i = 0; i < 4; i++)
                    {
                            bArray[i] = makeBlink(cArray, p, (r + i), (c + i));
                            bArray[i].play();
                    }

                    return true;
                }
            }
        }
        
        /* Check for diagonals facing right */
        for (int r = 0; r < 3; r++)
        {
            for (int c = 0; c < 4; c++)
            {
                if (cArray[r][c + 3].getFill() == p.getColor() &&
                    cArray[r + 1][c + 2].getFill() == p.getColor() &&
                    cArray[r + 2][c + 1].getFill() == p.getColor() &&
                    cArray[r + 3][c].getFill() == p.getColor())
                {
                    for (int i = 0, j = 3; i < 4 && j > -1; i++, j--)
                    {
                            bArray[i] = makeBlink(cArray, p, (r + i), (c + j));
                            bArray[i].play();
                    }

                    return true;
                }
            }
        }
        
        return false; // return false if the checks don't find a winner
    }
    
    public static boolean checkFull(Circle[][] cArray)
    { 
        /* Checks to see if the grid is full */
        for (int r = 0; r < ROWS; r++)
        {
            for (int c = 0; c < COLUMNS; c++)
            {
                if (cArray[r][c].getFill() == WHITE || cArray[r][c].getFill()
                        == GRAY) // if there are still white or gray circles left
                    return false; // then return false, the grid is not full yet
            }
        }
        
        return true; // grid is full, return true
    }
    
    public static void grayOut(Circle[][] cArray)
    {
        /* Locks all remaining white circles */
        for (int r = 0; r < ROWS; r++)
        {
            for (int c = 0; c < COLUMNS; c++)
            {
                if (cArray[r][c].getFill() == WHITE) // if circle is white
                    cArray[r][c].setFill(GRAY); // make it gray
            }
        }
    }
   
    public static Timeline makeBlink(Circle[][] cArray, Player p, int r, int c)
    {
        EventHandler<ActionEvent> blinkHandler = e -> {
            if (cArray[r][c].getFill() == p.getColor()) // if the chip is the player's color
                cArray[r][c].setFill(Color.GREEN); // Make the chip green   
            
            else // Otherwise
                cArray[r][c].setFill(p.getColor()); // Make the chip the player's color
        };
        
        Timeline blinking = new Timeline(new KeyFrame(Duration.millis(400), blinkHandler)); // Make the blinking animation
        blinking.setCycleCount(Timeline.INDEFINITE); // Set the cycle of animation to indefinite
 
        return blinking; // return the animation
    }
    
    public static void click(Circle[][] cArray, Player p, Player otherP, Timeline[] bArray, 
            Text msg, int i, int j)
    { 
        if(cArray[i][j].getFill() == WHITE && p.getTurn() == true) // if valid move and p's turn
        {
            cArray[i][j].setFill(p.getColor()); // Place p's chip in spot
            if (i != 0) // if not the top row
            {
                cArray[i - 1][j].setFill(WHITE); // make the spot above the chip valid
            }   
            
            turnSwitcher(p,otherP); // switch turns
                    
            if (checkWin(cArray, p, bArray) == true) // check if p won
            {
                msg.setText(p.getWinMsg()); // display winning message
                grayOut(cArray); // lock remaining spots
            }
            else if (checkFull(cArray) == true) // check for a draw
            {
                msg.setText(" Draw"); // display draw message
                msg.setFill(Color.GREEN); // make the message's color green
            }
            
            else // Otherwise
            {
                msg.setText(otherP.getTurnMsg()); // display other p's turn message
                msg.setFill(otherP.getColor()); // make the message's color red
            }
        }
    }
  
    public static void instr() {
        /* Create a new window to display game instructions on */
        Stage instrWindow = new Stage(); // Create new stage
        Text instrMsg = new Text(); // Create a new text
        instrMsg.setFont(Font.font("Times New Roman", 18)); // Set the font and size for text
        instrMsg.setText(" To play Connect Four, click on any white area on the grid "
            + "to put\n your colored chip in there. Take turns with an opponent. "
            + "The color\n of the chip will change after each chip is placed. "
            + "The first to get four\n chips horizontally, vertically, or "
            + "diagonally will win. Gray areas on\n the grid indicate "
            + "currently invalid moves. Nothing will happen if\n you click them."
            + " If an entire grid is filled up but there is no winner\n then it "
            + "is a draw.\n\n\n\n Connect Four programmed by Alan Bennett."); // Set text message
        HBox instrPane = new HBox(); // Create a new HBox
        instrPane.getChildren().add(instrMsg); // Add the HBox to instrPane
        Scene instrScene = new Scene(instrPane,500,230); // Place body in the scene
        instrWindow.setScene(instrScene); // Place the scene in the stage
        instrWindow.setTitle("Connect Four - Instructions"); // Title of game
        instrWindow.setResizable(false); // Make window non-resizable
        instrWindow.show(); // Display window
    }
     
    public static void nameMenu(Player p1, Player p2, Text msg) {
        /* Create a new window to display game instructions on */
        Stage nameWindow = new Stage(); // Create a new stage
        TextField p1Change = new TextField(), p2Change = new TextField(); // Create two text fields
        Text p1Text = new Text("Player 1: "), p2Text = new Text("Player 2: "); // Create two texts
        p1Text.setFill(p1.getColor()); // Set p1Text to player 1's color
        p2Text.setFill(p2.getColor()); // Set p2Text to plater 2's color
        GridPane namePane = new GridPane(); // Create a new gridpane
        namePane.setHgap(5); // Set the horizontal gap of namePane to 5
        namePane.setVgap(5); // Set the vertical gap of namePane to 5
        namePane.setAlignment(Pos.CENTER); // Center namePane
        namePane.add(p1Text, 0, 0); // Add player 1 text to namePane
        namePane.add(p1Change, 1, 0); // Add player 1 text field to namePane
        namePane.add(p2Text, 0, 1); // Add player 2 text to namePane
        namePane.add(p2Change, 1, 1); // Add player 2 text field to namePane
        Button changeButton = new Button("Change"), defaultButton = new Button("Reset"); // Create two buttons
        HBox buttonBox = new HBox(); // Create new HBox
        buttonBox.setSpacing(10); // Set buttonBox spacing
        buttonBox.getChildren().addAll(changeButton, defaultButton); // Add the two buttons to buttonBox
        namePane.add(buttonBox, 1, 2); // Add buttonBox to namePane
        final int LIMIT = 12; // Create a character limit of 12, declared as constant
        
        changeButton.setOnMouseClicked(e-> { 
      
            if (p1Change.getText() != null && !((p1Change.getText()).isEmpty())) // if text field is not null or empty
            {
                if (p1Change.getText().length() <= LIMIT) // if text field's length is less than or equal to LIMIT
                    p1.setName(p1Change.getText()); // change player 1 name to the text field string
                else // Otherwise
                    p1.setName(p1Change.getText().substring(0,LIMIT));  // change plater 1 name to first 12 characters of text field string 
            }
        
            if (p2Change.getText() != null && !((p2Change.getText()).isEmpty())) // if text field is not null or empty
            {
                if (p2Change.getText().length() <= LIMIT) // if text field's length is less than or equal to LIMIT
                    p2.setName(p2Change.getText()); // change player 2 name to the text field string
                else // Otherwise
                    p2.setName(p2Change.getText().substring(0,LIMIT)); // change plater 1 name to first 12 characters of text field string 
            }
            
            if (!" Player 1 wins".equals(msg.getText()) && !" Player 2 wins".equals(msg.getText()))
            {
                if (p1.getTurn() == true) // if it is player 1's turn
                    msg.setText(p1.getTurnMsg()); // display p1's turn message

                else // Otherwise
                    msg.setText(p2.getTurnMsg()); // display p2's turn message
            }
            
            else
            {
                if (" Player 1 wins".equals(msg.getText()))
                    msg.setText(p1.getWinMsg());
                else
                    msg.setText(p2.getWinMsg());
            }

            nameWindow.close(); // Close nameWindow
        });
        
        defaultButton.setOnMouseClicked(e-> { 
            
            if (!(" " + p1.getName() + " wins").equals(msg.getText()) && 
                    !(" " + p2.getName() + " wins").equals(msg.getText()))
            {
                p1.setName("Player 1"); // Set player 1 name back to default
                p2.setName("Player 2"); // Set player 2 name back to default 
                
                if (p1.getTurn() == true) // if it is player 1's turn
                    msg.setText(p1.getTurnMsg()); // display p1's turn message

                else // Otherwise
                    msg.setText(p2.getTurnMsg()); // display p2's turn message
            }
            
            else
            {
                if ((" " + p1.getName() + " wins").equals(msg.getText()))
                {
                    p1.setName("Player 1"); // Set player 1 name back to default
                    p2.setName("Player 2"); // Set player 2 name back to default
                    msg.setText(p1.getWinMsg());
                }
                else
                {
                    p1.setName("Player 1"); // Set player 1 name back to default
                    p2.setName("Player 2"); // Set player 2 name back to default
                    msg.setText(p2.getWinMsg());
                }
            }

            
            nameWindow.close(); // Close nameWindow
        });
        
        Scene nameScene = new Scene(namePane,300,130); // Place body in the scene
        nameWindow.setScene(nameScene); // Place the scene in the stage
        nameWindow.setTitle("Connect Four - Change Name"); // Title of game
        nameWindow.setResizable(false); // Make window non-resizable
        nameWindow.show(); // Show nameWindow
    }
} // end of code
  