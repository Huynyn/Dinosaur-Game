
/**
 * GraphicsInterface class manages the visual aspects and input of the game using javaFx
 *
 * @author (Huy Nguyen)
 * @version (2023-06-07)
 */

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color; 
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;  
import java.io.File;
import java.io.FileInputStream;
import javafx.scene.text.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode; 
import java.util.ArrayList;

public class GraphicsInterface extends Application
{
    //GLOBAL VARIABLES -> for ease of access among methods in class
    private double dblMouseX; //holds x coodinate of mouse.  Although short can hold range of mouse location, double is more accurate and leads to smoother movement
    private Pane pneGamePane; //Root, pane, acts as a type of container for nodes, gui objects.  Pane has been chosen as roog type due to its coordinate system 
                              //-> good for moving nodes by coordinate
                              
    private static ArrayList<ImageView> imglst_Dinosaurs = new ArrayList<ImageView>(); //holds image viewers for dinosaurs   
    private ArrayList<Node> ndelst_MenuNodes = new ArrayList<Node>(); //holds nodes used for menus

    //booleans holds game states
    private boolean bolPaused; 
    private boolean bolGameOver; 
    
    public void GraphicsInterface() //default constructor
    {
        this.dblMouseX = 0; 
        this.pneGamePane = null; 
        this.imglst_Dinosaurs.clear(); 
        ndelst_MenuNodes.clear(); 
        this.bolPaused = false; 
        this.bolGameOver = false; 
    }
       
    @Override 
    @SuppressWarnings("restriction") //suppresses warning message from appearing
    public void start(Stage stage) throws java.io.FileNotFoundException //throws java.io.FileNotFoundException is for when attempt to find file fails, then tells caller of excep
    {
        
        //Set pneGamePane to populated root with desired nodes from the createPane method
        pneGamePane = createPane(); 
        
        //create scene with a 2:1 aspect ratio of 1000 px to 500 px and set root to root we have created
        Scene scnGameScene = new Scene(pneGamePane,1000,500);
        stage.setTitle("Dino Hunt"); //set title of window
        stage.setScene(scnGameScene); //set scene of stage
        stage.setResizable(false); //do not allow resizing due to complications that may arise (ex: making backgroud bigger, recentering text)
        
        //center crosshair to middle of screen and at y coordinate 300 using moveNode method, passing in the circle, and half the screen (1000/2) - half of circle (radius)
        //since javafx nodes build visually from the right, the circle will need to be set to halfway (500) and shifted by its radius to the left. 
        moveNode(pneGamePane.getChildren().get(5), 1000/2 - ((Circle)(pneGamePane.getChildren().get(5))).getRadius(), 300); 
    
        //ACTIVE LISTENERS FOR SCENE
        scnGameScene.setOnKeyPressed(new EventHandler<KeyEvent>() //when key on keyboard is pressed
        {
          @Override
          public void handle(KeyEvent event) //handle() method handles KeyEvent, passing in event of type KeyEvent from scnGameScene.setOnKeyPressed
          {
              if (event.getCode().equals(KeyCode.P)) //if key is q 
              {
                  if (bolPaused && !bolGameOver) //if game is paused and game is not over, resume game
                  {
                      GameManager.resume(); //call resume method in GameManager to resume game  
                  }
                  else if (!bolGameOver) //if game is not over, pause game
                  {
                      GameManager.pause(); //call pause method in GameManager to pause game  
                  }
              }
          }
        });
        
       
        //required to completely terminate java virtual machine, otherwise java virtual machine will keep running and javafx application cannot be run again  
        stage.setOnCloseRequest(event -> {
            System.exit(0);
            //Platform.exit(); // Exit the application
        });
    
        stage.show(); //launch stage
    }
    public Pane createPane() throws java.io.FileNotFoundException //throws java.io.FileNotFoundException is for when attempt to find file fails, then tells caller of excep
    {
        Pane pneTempPane = new Pane(); //create a temporary root pane to return.  
      
      
        //NODES
        //Crosshair       
        Circle cirCrosshair = new Circle(); //acts as the crosshair of the player
      
        //Image Viewers -> nodes that can hold and display an image
        ImageView imgvewBackground = new ImageView(new Image(new FileInputStream("Assets/PixelArtBackground_Pixelated1.png"))); //holds background image, load from assets
        ImageView imgvewWatergun = new ImageView(); //holds rotational images of watergun model
        ImageView imgvewWaterBullet =  new ImageView(); //holds water bullet splash animation images
      
        /*
        * Text -> the text that the text nodes have been set to (third parameter) are simply placeholder text, indicative of string they will hold and display during 
        * game runtime 
        * First parameter for all text is x position, 250.  Nodes in javafx build from the right, meaning these text nodes will build to the right from x coordinate 250.  
        * In combination with a "text box size" of 500 and text alignment to center, the text will be centered on screen.  250 to the right + 500 + 250 remaining effectively 
        * places text in middle.  Text box sizing and text alignment will be set further down.  
        * Second paramter is y coordinate of text
        */ 
        Text txtGameInfo = new Text(250, 50, "Time: 1:00 | Score: 10000 | Wave 10"); //displays current time left for wave, current score, and current wave
        Text txtGameState = new Text(250, 150, "GAME PAUSED"); //Displays if game is paused or over
        Text txtGameScore = new Text(250, 220, "SCORE: 00000"); //Displays current score of player
        
        Text txtHowToPlay = new Text(40,50,"HOW TO PLAY\n\n1.Shoot dinosaurs before the time per wave runs out!\n2.Make it to the last wave!\n\nControls:" + 
                                            "\nMove water gun using mouse\n\nLeft-click to shoot\n\nP to pause");                                 
        Text txtPtoPause = new Text(850, 50, "P TO PAUSE"); 
      
        //Buttons -> parameter is text that will be displayed on button
        Button btnResume = new Button("RESUME"); //for resuming game
        Button btnReset = new Button("RESET"); //for reseting game
        Button btnExit = new Button("EXIT"); //for exiting game

      
        //FONT
        Font fntMunro = Font.loadFont("file:Assets/Fonts/munro.ttf", 30); //load munro font from assets and set font size to 30
        Font fntMunroBig = new Font(fntMunro.getName(),80); //create new font from already intialized munro font and set size to 80 for bigger font
        Font fntMunroSmall = new Font(fntMunro.getName(),24); //create new font from already intialized munro font and set size to 24 for smaller font
      
      
        //CHANGE PROPERTIES OF NODES   
        //Crosshair 
        cirCrosshair.setCenterY(-70); //set the center of the circle's y location to be -70 pixels below midpoint of scene
        cirCrosshair.setFill(Color.rgb(32,54,89)); //using built in javaFX setFill, set the crosshair colour to blue -- same colour as watergun tip
        cirCrosshair.setRadius(10); //set size of crosshair to 10 pixels at the radius (20 pixels in diameter)
       
        //Image viewer for water splash/bullet animation
        imgvewWaterBullet.setY(200);
      
        //Text
        //set wrapping width ("text box width") to 500
        txtGameInfo.setWrappingWidth(500);
        txtGameState.setWrappingWidth(500);
        txtGameScore.setWrappingWidth(500);
        //set text wrapping width to 200
        txtHowToPlay.setWrappingWidth(180);
        //set alignment of text to center
        txtGameInfo.setTextAlignment(TextAlignment.CENTER);
        txtGameState.setTextAlignment(TextAlignment.CENTER);
        txtGameScore.setTextAlignment(TextAlignment.CENTER);
        //set font 
        txtGameInfo.setFont(fntMunro);
        txtGameState.setFont(fntMunroBig);
        txtGameScore.setFont(fntMunroBig);
        txtHowToPlay.setFont(fntMunroSmall);
        txtPtoPause.setFont(fntMunro);
        //make txtGameState and txtGameScore non-visible by default due to them showing only when the game over or game paused menus are shown 
        txtGameState.setVisible(false); 
        txtGameScore.setVisible(false); 
        txtHowToPlay.setVisible(false);
        //add white outline to txtGameState and txtGameScore to make them more readable since they will be overlayed over many visuals
        txtGameState.setStrokeWidth(3);
        txtGameScore.setStrokeWidth(3);
        txtGameState.setStroke(Color.WHITE);
        txtGameScore.setStroke(Color.WHITE);
      
        //Buttons
        //set y coodinate of buttons to 230 
        btnResume.setLayoutY(230);
        btnReset.setLayoutY(230); 
        btnExit.setLayoutY(230); 
        //change font on button's text
        btnResume.setFont(fntMunro); 
        btnReset.setFont(fntMunro); 
        btnExit.setFont(fntMunro); 
        //make buttons non-visible, and un-clickable by conseqeuance, by default due to them showing only when the game over or game paused menus are shown 
        btnResume.setVisible(false); 
        btnReset.setVisible(false); 
        btnExit.setVisible(false); 
      
      
        //PANE ACTIVE LISTENERS
        pneTempPane.setOnMouseMoved(event -> moveNode(cirCrosshair, event.getX())); //set an event listener to call moveNode method anytime it 
                                                                                 //detects movement of cursor 
                                                                                 //passes in cirCrosshair and current mouse x position as parameters
        
        pneTempPane.setOnMousePressed(event -> { //On mouse click, call shoot method in GameManager to shoot gun (running code to shoot gun).  
            if (!bolGameOver && !bolPaused) //Only allow gun to shoot when the game is not over and the game is not paused, otherwise the user will be able to shoot              
            {                               //dinosaurs regardless of game being over or paused
                GameManager.shoot((short) event.getX()); //call method
            }
        });
       
        //on button clicks perform relevant actions
        btnResume.setOnAction(event -> {
            GameManager.resume(); //call resume method in GameManager to resume game 
        }); 
        btnReset.setOnAction(event -> {
            GameManager.reset(); //reset game through reset method in gamemanager
            bolGameOver = false; 
            GameManager.resume(); //call resume method in GameManager to resume game  
        });
        btnExit.setOnAction(event -> {
            System.exit(0); //exit program          
        });
        
        
        //ADD NODES THAT ARE USED IN GAMEOVER AND PAUSE MENU TO LIST
        ndelst_MenuNodes.add(txtHowToPlay);
        ndelst_MenuNodes.add(txtGameState);
        ndelst_MenuNodes.add(txtGameScore); 
        ndelst_MenuNodes.add(btnResume); 
        ndelst_MenuNodes.add(btnReset); 
        ndelst_MenuNodes.add(btnExit); 

        
        //ADD ALL NODES TO ROOT
        pneTempPane.getChildren().addAll(imgvewBackground, imgvewWatergun, imgvewWaterBullet, txtGameInfo, txtPtoPause, cirCrosshair, btnResume, btnReset, btnExit, txtHowToPlay, txtGameScore, txtGameState); 

        return pneTempPane;  
    }
    public double getMouseX() //getter for current mouse x coordinate from other classes 
    {
       return dblMouseX;  
    }
    public void changeText(String strText) //used to change txtGameInfo text from other classes
    {
        ((Text)(pneGamePane.getChildren().get(3))).setText(strText); 
    }
    //MOVEMENT METHODS
    public void moveNode(Node nodNodeToMove, double dblNewX) //method moves node 
    {
        nodNodeToMove.setLayoutX(dblNewX);    
        dblMouseX = dblNewX; 
    }
    public static void moveNode(Node nodNodeToMove, double dblNewX, double dblNewY) //method moves node 
    {
        nodNodeToMove.setLayoutX(dblNewX);  
        nodNodeToMove.setLayoutY(dblNewY);   
    }
    //ANIMATION METHODS
    public void setWaterGunImage(Image imgCurrentModel) //Method takes in image and changes watergun image viewer to new image, creating animation as images are passed in each frame
    {
        ((ImageView)pneGamePane.getChildren().get(1)).setImage(imgCurrentModel); //set image viewer's to passed in image
    }
    public void runWaterAnimation(Image imgCurrentModel, short shtPosition)
    {
        ((ImageView)pneGamePane.getChildren().get(2)).setImage(imgCurrentModel); //set image passed in from another class, changing each frame, thereby creating animation
        ((ImageView)pneGamePane.getChildren().get(2)).setX(shtPosition); //change position to position of shot fired, passed in from gameManager classs
    }
    public byte addDinosaurImageViewer() 
    {
        ImageView imgvewDinosaur = new ImageView(); //when creating a new dinosaur create a new imageViewer to hold this dinosaur
        imgvewDinosaur.setY(140); //set y coordinate of imageViewer
        imglst_Dinosaurs.add(imgvewDinosaur); //add imageViewer to list 
        pneGamePane.getChildren().add(5, imgvewDinosaur); //insert image viewer in at index four of pane so that cirCrosshair, btnResume, btnReset, btnExit, txtGameScore, 
                                                      //txtGameState remain on top
        
        return (byte)(imglst_Dinosaurs.size() - 1); //return the byte corrosponding to dinoaur image viewer index in imglst_Dinosaurs.  Code catching return statement will 
                                                    //have dinosaur hold this index, thereby allowing each dinosaur to reference their own image viewer
        
    }
    public static void setImageViewerDinosaur(Image imgDinosaur, short shtPosition, byte bytImageViewerIndex) //called to update dinosaur animation and location on screen
    {
        imglst_Dinosaurs.get(bytImageViewerIndex).setImage(imgDinosaur); //set image viewer of dinosaur to new image
        imglst_Dinosaurs.get(bytImageViewerIndex).setX(shtPosition); //set new position of image viewer as dinosaur moves
    }
    public void removeDinoImage(byte bytImageViewerIndex) //called when dinosaur dies
    {
        imglst_Dinosaurs.get(bytImageViewerIndex).setImage(null); //remove image
    }
    //MENU METHODS
    public void pauseMenu(String strScore)
    {
        bolPaused = true; //set bolPaused to true
        
        ((Text)ndelst_MenuNodes.get(1)).setText("GAME PAUSED"); //set txtGameState to game paused
        ndelst_MenuNodes.get(0).setVisible(true); //make txtHowToPlay, txtGameScore and txtGameState visible
        ndelst_MenuNodes.get(1).setVisible(true); 
        ndelst_MenuNodes.get(2).setVisible(true); 
        
        //set score of txtGameScore
        ((Text)ndelst_MenuNodes.get(2)).setText(strScore); 
        
        //loop through remaining buttons in list
        for (byte i = 3; i < ndelst_MenuNodes.size(); i++)
        {
            ndelst_MenuNodes.get(i).setVisible(true); //make visible and interactable
            ((Button)ndelst_MenuNodes.get(i)).setMinWidth(400/3); //set their size to one third of allocated 400 pixels.  The length of txtGameScore and txtGameState is approximately 400 pixels
            ndelst_MenuNodes.get(i).setLayoutX(400/3 * (i - 3) + 295); //center buttons by offsetting each button by multiple of 400/3 and shifting 295 pixels to the right
        }
    }
    public void closeMenus()
    {       
        //loop through all nodes in ndelst_MenuNodes, making all of them non-visble as the menu has closed as well as non-interactable by consequence 
        for (byte i = 0; i < ndelst_MenuNodes.size(); i++)
        {
            ndelst_MenuNodes.get(i).setVisible(false); 
        }
        bolPaused = false; //set to false since game is no longer paused
    }
    public void GameOverMenu(String strScore, String strText, boolean bolShowRules)
    {
        bolPaused = true; //set paused to true in order to prevent user from pausing menu and accessing functions in the pause menu
        bolGameOver = true; //set to true to prevent certain actions
        
        ((Text)ndelst_MenuNodes.get(1)).setText(strText); //set big text to game over
        ndelst_MenuNodes.get(0).setVisible(bolShowRules); //make txtHowToPlay, txtGameScore and txtGameState visible
        ndelst_MenuNodes.get(1).setVisible(true); 
        ndelst_MenuNodes.get(2).setVisible(true);  
        
        //set score of txtGameScore
        ((Text)ndelst_MenuNodes.get(2)).setText(strScore); 
        
        //loop through last two nodes of ndelst_MenuNodes (buttons btnReset and btnExit) 
        for (byte i = 4; i < ndelst_MenuNodes.size(); i++)
        {
            ndelst_MenuNodes.get(i).setVisible(true); //make visible and interactable
            ((Button)ndelst_MenuNodes.get(i)).setMinWidth(400/2);  //set their size to one half of allocated 400 pixels.  The length of txtGameScore and txtGameState is approximat
            ndelst_MenuNodes.get(i).setLayoutX(400/2 * (i-4) + 295); //center buttons by offsetting each button by multiple of 400/2 and shifting 295 pixels to the right
        }
    }

}
