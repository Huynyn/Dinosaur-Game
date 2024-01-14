
/**
 * GameManager is main class of game, holding all other classes and translating communication between them.  GameManager class runs game through frame system
 * wherein actions will be called every frame
 *
 * @author (your name)
 * @version (a version number or a date)
 */

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import java.io.FileInputStream;

import java.util.ArrayList;

public class GameManager
{
    //---GLOBAL VARIABLES---
    //REFERENCES OF INSTACES OF CLASSES
    private static GraphicsInterface graphicsInterface; //manages visuals of game and game input
    static Watergun wtgPlayerWeapon; //player weapon
    //private static DinosaurImages dinimg_DinosaurImages; 
    private static WaterImpact waterImpact; //manages bullet/splash animation and properties 
    
    //ARRAYS CONTAINING DINOSAUR IMAGES -> POPULATED ONCE AND THEN REUSED BY EVERY NEW CREATED DINOSAUR
    private static Image[] img_GreenDinoImages;
    private static Image[] img_RedDinoImages; 
    private static Image[] img_YellowDinoImages;
    private static Image[] img_BlueDinoImages;

    //DINOSAUR LISTS
    //Although only one list is needed to hold all active dinosaurs, storing inactive dinosaurs allows them to be reused again instead of creating new dinosuars
    //each time during runtime which wasts memory and resources 
    private static ArrayList<Dinosaur> objlst_EnabledDinosaurs = new ArrayList<Dinosaur>(); //Holds all currently active dinosaurs
    private static ArrayList<Dinosaur> objlst_GreenDinosaurs = new ArrayList<Dinosaur>(); //holds green dinosaurs not currently in use
    private static ArrayList<Dinosaur> objlst_RedDinosaurs = new ArrayList<Dinosaur>(); //holds red dinosaurs not currently in use
    private static ArrayList<Dinosaur> objlst_YellowDinosaurs = new ArrayList<Dinosaur>(); //holds yellow dinosaurs not currently in use
    private static ArrayList<Dinosaur> objlst_BlueDinosaurs = new ArrayList<Dinosaur>(); //holds Blue dinosaurs not currently in use

    
    //WAVE SYSTEM
    //private static Wave[] wav_Waves;
    private static byte[][] byt_Waves; //contains waves in the following format: {number of seconds for wave, number of green dinos, number of red dinosaurs, number of yellow dinosaurs, number of blue dinosaurs}
    private static byte bytCurrentWave; //holds current wave being played 
    private static boolean bolWaveComplete; //holds whether a wave is currently being played
    private static byte bytTimeLeft; //holds time left for player to beat wave
    
    //FRAME SYSTEM
    final static short SHT_FRAMESPERSECOND = 120; //amount of frames, and subsequent actions per frame, that occur every second (although value can be byte, short allows very high frame rates)
    private static int intCounter; //Used to perform actions at less than set framerate  
     
    //SCORE 
    public static short shtScore; //player scre
    private static AnimationTimer aniTim_Frames; //animationTimer class which runs asynchronously, allowing actions to be run per frame without impacting runtime of other classes and actions
    
    public static void main(String[] args) throws java.io.FileNotFoundException
    {   
        //Create stage to be passed into graphicsInterface to launch javafx application
        Stage stage = new Stage(); 
        
        //INTITIALIZE INSTANCES OF CLASSES
        graphicsInterface = new GraphicsInterface(); 
        wtgPlayerWeapon = new Watergun((byte)9, PopulateImageArray("Assets/Watergun images/Pixelated/", (byte)18), (byte)9); //pass in starting model for weapon (9th image in array)
                                                                                                                      //images of weapon models, using PopolateArrayMethod(), and the starting
                                                                                                                      //bytWeaponPostionalValue to starting position (9)
                                                                                                                      
        
        //POPULATE IMAGE ARRAYS FOR EACH DINOSAUR, passing in file locations, length of arrays, and desired size of images
        img_GreenDinoImages = PopulateImageArray("Assets/Dinosaurs/Green Dino/All/", (byte)32,(byte)125); 
        img_RedDinoImages = PopulateImageArray("Assets/Dinosaurs/Red Dino/All/", (byte)32,(byte)125); 
        img_YellowDinoImages = PopulateImageArray("Assets/Dinosaurs/Yellow Dino/All/", (byte)32,(byte)125); 
        img_BlueDinoImages = PopulateImageArray("Assets/Dinosaurs/Blue Dino/All/", (byte)32,(byte)125); 
        
        /*
        dinimg_DinosaurImages = new DinosaurImages(
                                                    PopulateImageArray("Assets/Dinosaurs/Yellow Dino/All/", (byte)32,(byte)125),
                                                    PopulateImageArray("Assets/Dinosaurs/Blue Dino/All/", (byte)32,(byte)125),
                                                    PopulateImageArray("Assets/Dinosaurs/Red Dino/All/", (byte)32,(byte)125),
                                                    PopulateImageArray("Assets/Dinosaurs/Green Dino/All/", (byte)32,(byte)125)
                                                  ); //yellow, blue, red green
       */
        //INITIALIZE WATERIMPACY OBJECT, passing in if enabled to false by default (something has not been shot yet), default x position, and images,
        waterImpact = new WaterImpact(false, (short)0, PopulateImageArray("Assets/Impact/WaterImpact/", (byte)3, (byte)50)); 
               
        //CREATE WAVES OF DINOSAURS -> each row is a new wave, using the following order for columns: seconds player has to complete wave, num green dinos, num red dinos, 
        byt_Waves = new byte[][] {                                                                   //num yellow dinos, num blue dinos
                                {60,1,0,0,0}, //introduce green dinosaur
                                {50,2,0,0,0}, //introduce more than one dinosaur
                                {5,1,0,0,0}, //introduce time constraint
                                {7,3,0,0,0}, //wave that's a little bit challenging
                                {30,0,1,0,0}, //introduce red dinosaur
                                {15,10,0,0,0}, 
                                {8,1,2,0,0}, 
                                {11,2,3,0,0}, 
                                {120,0,0,1,0}, //introduce yellow dinosaur
                                {15,5,5,0,0}, 
                                {20,2,1,1,0}, 
                                {15,0,0,3,0}, 
                                {45,0,0,0,1}, //introduce blue dinosaur
                                {16,0,0,0,2}, 
                                {16,2,0,0,3}, 
                                {10,1,1,1,1}, 
                                {3,1,0,0,1}, 
                                {3,0,0,1,0},
                                {15,0,1,5,1},
                                {12,0,5,0,5}, 
                                
                                //start ramping up difficulty
                                {10,0,10,0,0}, 
                                {8,30,0,0,0},
                                {10,0,0,12,0},
                                {3,0,0,2,2},
                                {3,2,1,1,1},
                                {10,0,0,20,0},
                                {5,0,2,1,2},
                                {2,0,0,1,0},
                                {10,0,28,1,0},
                                {5,0,0,1,2},
                                {10,0,0,0,30},
                                {2,0,0,0,2},
                                {9,0,0,15,3},
                                {1,2,0,0,0},
                                {1,0,1,0,0},
                                {1,0,0,1,0},
                                {5,0,1,6,0},
                                {14,35,1,1,1},
                                {1,0,0,0,2},
                                {6,0,1,3,3},
                                {6,4,3,2,1},
                                
                                
        }; 
                                                
        bytCurrentWave = 0; //set current wave to 0, first row of byt_Waves
        bolWaveComplete = true; //set bolWaveComplete to true, though a wave has not been complete, to tell program they can start a new wave (wave 1)
        
        shtScore = 0; //set score to 0 
   
        graphicsInterface.start(stage);  //start javafx application
        Frames(); //start frames 
        
        
        
    }
    private static void Frames() //MANAGES FRAME RATE (actions that occur x amount of times each second) AND ANIMATION OF GAME
    {       
       final long LNG_INTERVAL = 1000000000/SHT_FRAMESPERSECOND;// animation timer defaults to nanoseconds -> 1000000000 nanoseconds is 1 second, holds number of nanoseconds needed 
                                                            //to be passes in order to have a frame to run SHT_FRAMESPERSECOND times a second
        
       aniTim_Frames = new AnimationTimer() { //set AnimationTimer handle method 
             //intCounter = 0; 
            long lngPriorTime = 0;
            long lngPriorTime1; //
        
            @Override
            public void handle(long lngCurrentTime) {
                //MANAGES ACTIONS AT 1 SECOND INTERVALS -> actions that need to be performed at a 1 second interval, or not at high frequency, otherwise leading to unnecessary actions performed
                if (lngCurrentTime - lngPriorTime1 >= 1000000000) //when currentTime has become 1 whole second greator than last tracked time
                {
                    if (objlst_EnabledDinosaurs.size() > 0) //when there are still dinosaurs alive, do the following actions
                    {
                        bytTimeLeft--; //lower time user has left to beat wave by 1 second, every second
                        //update score board with new time left, user score, and current wave by calling changeText method in graphicsInterface
                        //format numbers to have certain amount of digits with leading zeros if number does not reached desired number of digits
                        graphicsInterface.changeText(String.format("%d:%02d | %06d | Wave %01d", bytTimeLeft / 60, bytTimeLeft % 60, shtScore, bytCurrentWave  + 1));
                    } 
                    
                    if (bolWaveComplete == true && bytCurrentWave <= 99) //if the wave has been completed, do the following actions
                    {
                        spawnWave(bytCurrentWave); //spawn next wave
                        //update score board to reflect the new wave and the new time user has left for new wave
                        //format numbers to have certain amount of digits with leading zeros if number does not reached desired number of digits
                        graphicsInterface.changeText(String.format("%d:%02d | %06d | Wave %01d", bytTimeLeft / 60, bytTimeLeft % 60, shtScore, bytCurrentWave  + 1)); 
                    } 
                    else if (bytTimeLeft <= 0) //if the wave has not ended yet and time left has reached 0 (user failed to beat wave within allocated time)
                    { 
                        GameOver("GAME OVER!!!!", true); //run game over method
                    }
                    
                    lngPriorTime1 = lngCurrentTime; //set lngPriorTime1 to lngCurrentTime in order for timer to have to wait another second for lngCurrentTime to be 1 second greator than lngPriorTime1
                }
                
                //MANAGES ACTIONS AT SHT_FRAMESPERSECOND SPEED 
                if (lngCurrentTime - lngPriorTime >= LNG_INTERVAL) {
                    intCounter++; //increases each frame.  intCounter cannot reach interger.MAX_VALUE due to completing game taking less time.
                    //run gun model animation by calling graphicsInterface setWaterGunImage method with image from wtgPlayerWeapon.changeWeaponModel(graphicsInterface.getMouseX())
                    //changeWeaponModel takes in current mouse x coordinate and will return image relevant to passed in coordinate
                    graphicsInterface.setWaterGunImage(wtgPlayerWeapon.changeWeaponModel(graphicsInterface.getMouseX()));

                    if (intCounter % 4 == 0) //run animations at 1 fourth frequency of framerate  
                    {
                        for (byte i = 0; i < objlst_EnabledDinosaurs.size(); i++) //for all dinosaurs enabled run them by running their animate and move actions, passing in  
                        {                                                          //method returns to graphicInterface.setImageViewerDinosaur() method for visual display 
                            graphicsInterface.setImageViewerDinosaur(objlst_EnabledDinosaurs.get(i).animate(), objlst_EnabledDinosaurs.get(i).move(), objlst_EnabledDinosaurs.get(i).getDisplayIndex());     
                        }  
                        
                        if (waterImpact.getEnabled()) //if waterImpact has been enabled (due to a shot being fired), run animations
                            //call runWaterAnimation method, passing in current image from waterImpact.animate(), and its x coordinate position 
                            graphicsInterface.runWaterAnimation(waterImpact.animate(), waterImpact.getPositionX());
                    }

                    lngPriorTime = lngCurrentTime; //set lngPriorTime to lngCurrentTime in order for timer to have to wait another LNG_INTERVAL for lngCurrentTime to be LNG_INTERVAL nanoseconds greator than lngPriorTime                    
                }
            }
       };
       aniTim_Frames.start(); //start timer
    }
    private static void spawnWave(byte bytWaveNum) //used for spawning in dinosaurs and setting variables for certain wave
    {
        bytTimeLeft = byt_Waves[bytCurrentWave][0]; //set the time user has to left to the new wave's time limit 
        for (byte j = 0; j < byt_Waves[bytWaveNum][1]; j++) //go through number of green dinos in wave
        {
            if (objlst_GreenDinosaurs.size() > 0) //if there are dinosaurs to reuse
            {
                //access last dinosaur in list as to not resize the entire list everytime 
                objlst_GreenDinosaurs.get(objlst_GreenDinosaurs.size() - 1).spawn(); //run dino's spawn method
                objlst_EnabledDinosaurs.add(objlst_GreenDinosaurs.get(objlst_GreenDinosaurs.size() - 1)); //add to active dinosaurs
                objlst_GreenDinosaurs.remove(objlst_GreenDinosaurs.get(objlst_GreenDinosaurs.size() - 1)); //remove from inactive dinosaurs list
            }
            else 
            {
                //create new dinosaur by setting its images and creating an imageViewer specific for it
                GreenDinosaur greenDinosaur = new GreenDinosaur(img_GreenDinoImages, graphicsInterface.addDinosaurImageViewer());
                objlst_EnabledDinosaurs.add(greenDinosaur); //add to enabled dinosaurs list
            }
        }
        for (byte j = 0; j < byt_Waves[bytWaveNum][2]; j++) //go through number of red dinos in wave
        {
            if (objlst_RedDinosaurs.size() > 0) //if there are dinosaurs to reuse
            {
                //access last dinosaur in list as to not resize the entire list everytime 
                objlst_RedDinosaurs.get(objlst_RedDinosaurs.size() - 1).spawn(); //run dino's spawn method
                objlst_EnabledDinosaurs.add(objlst_RedDinosaurs.get(objlst_RedDinosaurs.size() - 1)); //add to active dinosaurs
                objlst_RedDinosaurs.remove(objlst_RedDinosaurs.get(objlst_RedDinosaurs.size() - 1)); //remove from inactive dinosaurs list
            }
            else 
            {
                //create new dinosaur by setting its images and creating an imageViewer specific for it
                RedDinosaur redDinosaur = new RedDinosaur(img_RedDinoImages, graphicsInterface.addDinosaurImageViewer()); //run dino's spawn method
                objlst_EnabledDinosaurs.add(redDinosaur); //remove from inactive dinosaurs list
            }
        }
        for (byte j = 0; j < byt_Waves[bytWaveNum][3]; j++) //go through number of yellow dinos in wave
        {
            if (objlst_YellowDinosaurs.size() > 0) //if there are dinosaurs to reuse
            {
                //access last dinosaur in list as to not resize the entire list everytime 
                objlst_YellowDinosaurs.get(objlst_YellowDinosaurs.size() - 1).spawn(); //run dino's spawn method
                objlst_EnabledDinosaurs.add(objlst_YellowDinosaurs.get(objlst_YellowDinosaurs.size() - 1)); //add to active dinosaurs
                objlst_YellowDinosaurs.remove(objlst_YellowDinosaurs.get(objlst_YellowDinosaurs.size() - 1)); //remove from inactive dinosaurs list
            }
            else 
            {
                //create new dinosaur by setting its images and creating an imageViewer specific for it
                YellowDinosaur yellowDinosaur = new YellowDinosaur(img_YellowDinoImages, graphicsInterface.addDinosaurImageViewer());//run dino's spawn method
                objlst_EnabledDinosaurs.add(yellowDinosaur); //remove from inactive dinosaurs list
            } 
        }
        for (byte j = 0; j < byt_Waves[bytWaveNum][4]; j++) //go through number of blue dinos in wave
        {
            if (objlst_BlueDinosaurs.size() > 0) //if there are dinosaurs to reuse
            {
                //access last dinosaur in list as to not resize the entire list everytime 
                objlst_BlueDinosaurs.get(objlst_BlueDinosaurs.size() - 1).spawn(); //run dino's spawn method
                objlst_EnabledDinosaurs.add(objlst_BlueDinosaurs.get(objlst_BlueDinosaurs.size() - 1)); //add to active dinosaurs
                objlst_BlueDinosaurs.remove(objlst_BlueDinosaurs.get(objlst_BlueDinosaurs.size() - 1)); //remove from inactive dinosaurs list
            }
            else 
            {
                //create new dinosaur by setting its images and creating an imageViewer specific for it
                BlueDinosaur blueDinosaur = new BlueDinosaur(img_BlueDinoImages, graphicsInterface.addDinosaurImageViewer()); //run dino's spawn method
                objlst_EnabledDinosaurs.add(blueDinosaur); //remove from inactive dinosaurs list
            }
        } 
        bolWaveComplete = false;         
    }
    public static void shoot(short shtPositionX) //this method is public static so that it can be called from the graphicsInterface method when scene detects mouse click
    {
        //passed in x coordinate for shoot location is manipulated by water bullet size to get rightmost edge of bullet as x coordinate and the leftmost edge of bullet as x coordinate
        short shtMaxX = (short)(shtPositionX + (short)waterImpact.getBulletSize()); //holds the rightmost edge of bullet as x coordinate
        short shtMinX = (short)(shtPositionX - (short)waterImpact.getBulletSize()); //holds the leftmost edge of bullet as x coordinate
    
        waterImpact.runWaterImpact(shtPositionX); //run water impact as shot as been  fired 
        
        for (Dinosaur i : objlst_EnabledDinosaurs) //go through all dinosaurs that are enabled
        {
            if(i.checkCollision(shtMinX, shtMaxX)) //check each one if they have experienced collision 
            {
                //if collision, add dinosaur to their relevant list by type 
                switch (i.bytType) 
                {
                    case 0: 
                        objlst_GreenDinosaurs.add(i); 
                        break;
                    case 1: 
                        objlst_RedDinosaurs.add(i);
                        break; 
                    case 2: 
                        objlst_YellowDinosaurs.add(i);
                        break; 
                    case 3:
                        objlst_BlueDinosaurs.add(i);
                        break; 
                        
                }
                shtScore += i.shtNumPoints; //increase user score by how much dinosaur is worth 
                graphicsInterface.removeDinoImage(i.getDisplayIndex()); //remove dinosaur image 
                objlst_EnabledDinosaurs.remove(i); //remove dinosaur from enabled dinosaurs
                
                if (objlst_EnabledDinosaurs.size() == 0) //if enabled dinosaurs reaches 0 that means all dinosaurs have been shot by user
                {
                    //check if user has beat game
                    bolWaveComplete = true; 
                    bytCurrentWave++; //move onto next wave
                    
                    if (bytCurrentWave == byt_Waves.length) //check if player has beat game by seeing if current wave has reached length of waves
                        GameOver("🎉VICTORY🎉",false);
                }
                break;  //break so that once dinosaur has experienced collision, no longer check through list of dinoaurs for collision.
                        //although this results in only one dinosaur being killed at a time, this removes runtime error in which for loop is checking through 
                        //list while list is being changed

            }  
        }  
    }
    //GAME STATE METHODS
    public static void pause() //Called to pause game and launch paused game menu
    {
        aniTim_Frames.stop(); //pause timer/frame rate running
        graphicsInterface.pauseMenu(String.format("SCORE %06d", shtScore)); //pause menu, passing in score formated as 6 digits with leading zeros
    }
    public static void resume() //called to resume game and close paused game or game over menu 
    {
        graphicsInterface.closeMenus(); //close menu
        aniTim_Frames.start(); //resume timer/frame rate runnign
    }
    public static void reset() //used to reset game variables and dinosaurs
    {
        shtScore = 0; //reset score
        intCounter = 0; //reset counter
        bytCurrentWave = 0; //reset current wave
        
        while (objlst_EnabledDinosaurs.size() > 0) //remove all dinosaurs until there are none left -> when dinosaur objlst_EnabledDinosaurs.size() is no longer > 0
        {
            switch (objlst_EnabledDinosaurs.get(0).bytType) //add dinosaur to corresponding list
            {
                case 0: 
                    objlst_GreenDinosaurs.add(objlst_EnabledDinosaurs.get(0)); 
                    break;
                case 1: 
                    objlst_RedDinosaurs.add(objlst_EnabledDinosaurs.get(0)); 
                    break; 
                case 2: 
                    objlst_YellowDinosaurs.add(objlst_EnabledDinosaurs.get(0)); 
                    break; 
                case 3:
                    objlst_BlueDinosaurs.add(objlst_EnabledDinosaurs.get(0)); 
                    break; 
            }  
            graphicsInterface.removeDinoImage(objlst_EnabledDinosaurs.get(0).getDisplayIndex()); //remove dinosaur image
            objlst_EnabledDinosaurs.remove(objlst_EnabledDinosaurs.get(0)); //remove dinosaur from objlst_EnabledDinosaurs
        }
        
        spawnWave((byte)0);  //spawn first wave
    }
    private static void GameOver(String strText, boolean bolShowRules)
    {
        aniTim_Frames.stop(); //pause timer/frame rate running
        graphicsInterface.GameOverMenu(String.format("SCORE %06d", shtScore), strText, bolShowRules); //pause menu, passing in score formated as 6 digits with leading zeros  
    }
    //METHODS USED FOR FETCHING IMAGES FROM ASSET FILE AND RETURNING TO CALLER AS ARRAY
    private static Image[] PopulateImageArray(String strFolderPath, byte bytLength) throws java.io.FileNotFoundException //passes in file path and and lenght of array to output //throws java.io.FileNotFoundException is for when attempt to find file fails, thereby 
    {                                                                                                                     //then tells caller of exception 
        Image[] img_Temp = new Image[bytLength]; //creates temporary image array to hold image files
        
        for (byte i = 0; i < bytLength; i++) //goes through desired length and populates with images found at passed in path
        {
           img_Temp[i] = new Image(new FileInputStream(strFolderPath + i + ".png" ));  //create new iamge from file path
        }
       return img_Temp; 
    }
    private static Image[] PopulateImageArray(String strFolderPath, byte bytLength, byte bytSize) throws java.io.FileNotFoundException //same as method above, but a paramter is for size, letting caller choose desired size of image to populate in 
    {                                                                                                                                  //throws java.io.FileNotFoundException is for when attempt to find file fails, then tells caller of exception 
        Image[] img_Temp = new Image[bytLength];
        
        for (byte i = 0; i < bytLength; i++)
        {
           img_Temp[i] = new Image(new FileInputStream(strFolderPath + i + ".png" ), bytSize, bytSize, false,false); //create new iamge from file path and set desired size
        }
       return img_Temp; 
    }

}
