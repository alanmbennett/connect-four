
import javafx.scene.paint.Paint;

public class Player {

    private String name; // stores name of player
    private boolean turn; // stores current turn information
    private Paint color; // stores color of player
    private String winMsg; // stores winning message
    private String turnMsg; // stores current turn message
   
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setTurn(boolean turn)
    { 
        this.turn = turn; // sets the turn of this class to the parameter turn
    }
    public boolean getTurn()
    {
        return turn; // returns the player's turn information
    }
 
    public void setColor(Paint color)
    {
        this.color = color;
    }
    
    public Paint getColor()
    {
        return color;
    }
    
    public String getWinMsg()
    {
        winMsg = " " + name + " wins";
        return winMsg;
    }
    
    public String getTurnMsg()
    {
        turnMsg = " " + name + "'s turn";
        return turnMsg;
    }
    
    public void changeTurn()
    {
        turn = turn != true;
    }
}
