import javafx.scene.layout.Pane;
import javafx.scene.text.*;
import javafx.scene.control.Button;

/**
 * Write a description of class DefaultPane here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class GamePane extends DefaultPane
{
    private Text txtScore; 
    private Text txtLost;
    
    private Button btnHome; 
    private Button btnNext; 
     
    
    
    public GamePane()
    {
        //INITIALIZE NODES
        this.txtScore = new Text(50,50,"0");
        this.txtLost = new Text(50, 100, "YOU LOST!!!"); 
        this.btnHome = new Button("HOME"); 
        this.btnNext = new Button("NEXT");
        
        this.txtLost.setVisible(false);
        this.btnNext.setVisible(false);
        
        //btnHome.setLayoutY(230);
        
        //SET BUTTON FUNCTIONALITY
        this.btnHome.setOnAction(event -> {
            super.changePane(); 
        }); 
        
        this.btnNext.setOnAction(event -> {
            super.changePane(); 
        }); 
        
        //ADD ALL NODES TO PANE
        this.getChildren().addAll(txtScore, btnHome); 
        System.out.println("hello");
    }
    
    public void setScore(String strScore)
    {
        this.txtScore.setText(strScore); 
    }
    
    public void showLosingScreen()
    {
        this.txtLost.setVisible(true);
        this.btnNext.setVisible(true); 
    }
}
