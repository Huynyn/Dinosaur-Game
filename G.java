

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;

/**
 * Inherits javaFx application class. This is basically the window of the application and it will display and give functionality to the scenes in the program -- the sole 
 * functionality  this class is used for.
 * 
 *
 * @author (Huy Nguyen)
 * @version (Jan 9 2024)
 */
public class G extends Application
{ 
    private Scene scene; 
    private StartPane startPane;
    private GamePane gamePane; 
    
    public void test()
    {
        start(new Stage()); 
    }
    
    @Override
    public void start(Stage stage)
    {
        this.startPane = new StartPane();
        this.gamePane = new GamePane(); 
        
        this.scene = new Scene(startPane, 500, 1000);
        stage.setScene(scene); 
        
        System.out.println("here");
        
        
        //required to completely terminate java virtual machine, otherwise java virtual machine will keep running and javafx application cannot be run again  
        stage.setOnCloseRequest(event -> {
            System.exit(0);
            //Platform.exit(); // Exit the application
        });
    
        stage.show(); //launch stage
    }
    
    public void switchPane()
    {
        this.scene.setRoot(this.startPane); 
    }
}
