import javafx.scene.layout.Pane;
import javafx.scene.text.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Write a description of class DefaultPane here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class StartPane extends DefaultPane
{
    private Text txtPrompt; 
    
    private Label lblInstructions; 
    
    private Button btnPlayNormal;
    private Button btnPlayRush;
    private Button btnPlaySmall;
     
    
    
    public StartPane()
    {
        //INITIALIZE NODES
        this.txtPrompt = new Text(50,50,"WELCOME TO THE GAME");
        this.lblInstructions = new Label("This is how you play the game ayda yada ayda ay da"); 
        
        this.btnPlayNormal = new Button("NORMAL"); 
        this.btnPlayRush = new Button("RUSH"); 
        this.btnPlaySmall = new Button("SMALL"); 
        
        //SET BUTTON FUNCTIONALITY
        this.btnPlayNormal.setOnAction(event -> {
            super.changePane();
            //tell gamemanger to start game and set mode
        }); 
        
        this.btnPlayRush.setOnAction(event -> {
            super.changePane(); 
            //tell gamemanger to start game and set mode
        });
        
        this.btnPlaySmall.setOnAction(event -> {
            super.changePane(); 
            //tell gamemanger to start game and set mode
        }); 
        
        
        //ADD ALL NODES TO PANE
        this.getChildren().addAll(txtPrompt, lblInstructions, btnPlayNormal, btnPlayRush, btnPlaySmall); 
        System.out.println("hello");
    }
    
    public void setPrompt(String strPrompt)
    {
        this.txtPrompt.setText(strPrompt); 
    }
    
 
}
