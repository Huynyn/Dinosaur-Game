
/**
 * Abstract class containing properties of dinosaurs.  Children class will change certain variables in order to have different movement patterns and 
 * point value.  Class manages dinosaur movement, animation, collision, and spawning.  Dinosaur movement is dictated by random number generators wherein certain
 * numbers will result in certain changes or stagnation in movement  
 
 * @author (Huy Nguyen)
 * @version (2023-06-07)
 */

import javafx.scene.image.Image;
import java.util.Random;

public abstract class Dinosaur
{
    //Each image array holds an animation: dinosaur standing idle, dinosaur walking to the right, dinosaur walking to the left, and 
    private Image[] img_IdleImages;
    private Image[] img_WalkingRightImages; 
    private Image[] img_CrouchingRightImages; 
    private Image[] img_WalkingLeftImages; 
    private Image[] img_CrouchingLeftImages; 
    
    //dinosaur variables
    private final short shtHixBoxLength = 63; //holds the length (to one side) of the hitbox of the dinosaur in pixels.  shtHixBoxLength * 2 = total length of hitbox 
  
    //dinosaur state variables 
    private byte bytState; //holds the current state of the dinosaur: 0 for idle, 1 for walk, 2 for crouch 
    private byte bytDirection; //holds the current direction of dinosaur: -1 for left, 1 for right. 
    private short shtPositionX; //holds current x coordindate of dinosaur
    private byte bytCurrentIdleCycle; //holds the cycle of idling that dinosaur is currently on 
    
    //VARIABLES VARYING PER EACH DINOSAUR -> not changed in any constructor as they are set in child classes
    protected byte bytSpeed; //holds the speed of the dinosaur -> translates to number of pixels moved in a direction each frame
    protected byte bytCrouchSpeed; //holds speed of dinosaur when crouching -> slower than normal walking speeed
    //Each have a value 1 - 1000 and dictate the chance that an event will occur
    protected short shtChanceChangeDirection;  //chance dinosaur will change direction -> higher chance = harder
    //sum of chance crouch and chance idle have to less than 1000 with room for unwritten chance for dinosaur to walk
    protected short shtChanceIdle; //chance dinosaur will stop moving and start idling -> higher chance = easier 
    protected short shtChanceCrouch; //chance dinosaur will crouch instead of walk, -> higher chance = easier
    protected short shtChanceStopCrouching; //chance dinosaur will stop crouching, -> higher chance = harder 
    //Differing values
    protected short shtNumPoints; //number of points dinosaur is worth 
    protected byte bytType; //type: 0 for green, 1 for red, 2 for yellow, 3 for blue 
    protected byte bytIdleCycles; //the amount of idle cycles dinosaur will undergo before changing movement -> higher = easier

    //variables used in class functions 
    private Random rand;  //random number class
    private int intRandomNum; //int holds random number used in state changes
    private int intRandomNumForDirection; //int holds random number used in direction changes
    private byte bytFrame; //holds frame of animation
    
    private byte byt_displayIndex; //holds corresponding index of imageviewer in imageviewer list to this dinosaur 
    
    public Dinosaur()
    {
        this.img_IdleImages = new Image[0];
        this.img_WalkingRightImages = new Image[0];
        this.img_CrouchingRightImages = new Image[0];
        this.img_WalkingLeftImages = new Image[0];
        this.img_CrouchingLeftImages = new Image[0];
        this.bytState = 0;
        this.bytDirection = 0; 
        this.shtPositionX = 0;
        this.bytCurrentIdleCycle = 0; 
        this.rand = null; 
        this.intRandomNum = 0; 
        this.intRandomNumForDirection = 0; 
        this.bytFrame = 0; 
        this.byt_displayIndex = 0; 
        
    }
    public Dinosaur(Image[] img_AllDinoImages, byte byt_displayIndex)
    {
        rand = new Random(); //set rand to new Random class
        this.byt_displayIndex = byt_displayIndex; //set display index of dinosaur 
        
        //set image arrays for dino 
        this.img_IdleImages = new Image[4];
        this.img_WalkingRightImages = new Image[7];
        this.img_CrouchingRightImages = new Image[7];
        this.img_WalkingLeftImages = new Image[7]; 
        this.img_CrouchingLeftImages = new Image[7]; 

        //loop through passed images and populate designated image arrays with each animation 
        for (byte i = 0; i < img_AllDinoImages.length; i++)
        {
            if (i <= 3)//images 0 to 3 inclusive are for idle animation
            {
               img_IdleImages[i] = img_AllDinoImages[i];  
            }
            else if (i <= 10)//images four to ten inclusive are for walking right animation
            {
                img_WalkingRightImages[i - 4] = img_AllDinoImages[i];  
            }
            else if (i <= 17)//images 11 to 17 inclusive are for crouching animation
            {
                img_CrouchingRightImages[i - 11] = img_AllDinoImages[i];  
            }
            else if (i <= 24)//images 18 to 24 inclusive are for walking left animation
            {
                img_WalkingLeftImages[i - 18] = img_AllDinoImages[i];
            }
            else if (i <= 31)//images 25 to 31 inclusive are for crouching left animation
            {
                img_CrouchingLeftImages[i - 25] = img_AllDinoImages[i];
            }
            
        }         
        spawn(); //run spawn method
    }
    public void spawn() //actions that dinosaur goes through on spawn and respawns 
    {
        shtPositionX = (short)((10 * (rand.nextInt(44)+ 1)) + 50); //45 possible spawn locations ranging from 50 to 450 
        bytState = 1; //set to walking 
        bytDirection = (byte)((rand.nextInt(2) < 0.5) ? 1 : -1); //generates random number: 1 or -1
        bytFrame = 0; //set current animation  frame to 0 
        bytCurrentIdleCycle = 0; //set current idle cycle to 0 
        
    }
    public short move() //used for dinosaur movemment 
    {        
        intRandomNum = rand.nextInt(1000); //create new random numbmer to be used for random movement
        intRandomNumForDirection = rand.nextInt(1000); //random number used solely for direction changes, or chance of a direction change occuring will be
                                                      //tied to the chance of another action happening due to falling in overlapping ranges for chance
        
        switch (bytState)
        {
            case 0: //dinosaur is currently idling
                if (bytCurrentIdleCycle == bytIdleCycles) ///if dinosaur has completed all idling cycles
                {
                    bytState = (byte)rand.nextInt(3); //generates random number between 0 and 2 inclusive
                    bytCurrentIdleCycle = 0; //reset cycle
                    return move(); //call move again in order to change shtPositionX according to new state 
                }
                return (short)(shtPositionX - shtHixBoxLength); //return position unchanged, - shtHitBoxLength because visual display is built from right
                                                                //so need to shift to left by half to display from middle
            case 1: //dinosaur is currently walking or crouching 
            case 2: 
                //change direction of dinosaur 
                if (shtPositionX >= 970) //dinosaur has reached rightmost edge of screen
                {
                    bytDirection = -1; //set direction to left
                }
                else if (shtPositionX <= 0)//dinosaur has reached leftmost edge of screen
                {
                    bytDirection = 1; //set direction to right 
                }
                else if (intRandomNumForDirection < shtChanceChangeDirection) //true: dinosaur will change direction
                {
                    if (bytDirection == -1) //if currently going left
                    {
                        bytDirection = 1; //set to going right
                    }
                    else if (bytDirection == 1)// if currently going right
                    {
                        bytDirection = -1; //set to going left
                    }    
                }
                
                if (bytState == 1) //dinosuar is currently walking
                {
                    if (intRandomNum < shtChanceIdle) //true: dinosaur will begin idling
                    {
                        bytState = 0;  //set state to idle
                        bytFrame = 0; //change in state so reset animation frame
                        return (short)(shtPositionX - shtHixBoxLength);  //return position unchanged, - shtHitBoxLength because visual display is built from right
                                                                         //so need to shift to left by half to display from middle
                    }
                    else if (intRandomNum < (shtChanceIdle + shtChanceCrouch)) //true dinosaur will begin crouching
                    {
                        bytState = 2; //set state to crouch 
                        bytFrame = 0; //change in state so reset animation frame
                        
                        //return position translated by crouch speed and direction
                        //- shtHitBoxLength because visual display is built from right so need to shift to left by half to display from middle
                        shtPositionX = (short)(shtPositionX + (bytCrouchSpeed * bytDirection)); 
                        return (short)(shtPositionX - shtHixBoxLength);  
                    }
                    else //dinosaur will continue walking 
                    {
                        //return position translated by walk speed and direction
                        //- shtHitBoxLength because visual display is built from right so need to shift to left by half to display from middle
                        shtPositionX = (short)(shtPositionX + (bytSpeed * bytDirection)); 
                        return (short)(shtPositionX - shtHixBoxLength);                      }
                }
                else if (bytState == 2) //dinosaur is currently crouching
                {
                    if (intRandomNum < shtChanceStopCrouching) //if true, dinosaur will stop crouching and start walking
                    {
                        bytState = 1; //set state to walk
                        bytFrame = 0; //change in state so reset animation frame
                        
                        //return position translated by walk speed and direction
                        //- shtHitBoxLength because visual display is built from right so need to shift to left by half to display from middle
                        shtPositionX = (short)(shtPositionX + (bytSpeed * bytDirection)); 
                        return (short)(shtPositionX - shtHixBoxLength);                                                                  
                    }
                    else //dinosaur will continue crouching 
                    {
                        //return position translated by crouch speed and direction
                        //- shtHitBoxLength because visual display is built from right so need to shift to left by half to display from middle
                        shtPositionX = (short)(shtPositionX + (bytCrouchSpeed * bytDirection)); 
                        return (short)(shtPositionX - shtHixBoxLength);      
                    }
                }
                break;
        }
        return 0; //default return statement 
    }
    public Image animate()
    {
        bytFrame++; 
        
        switch (bytState)
        {
            case 0: //dinosaur is currently idling
                if (bytFrame == 4) //idling, 4 possible images 0 - 3
                {
                    bytFrame = 0; //reset frame
                    bytCurrentIdleCycle++; //increase current cycle as 1 idling cycle has been completed
                }
                return img_IdleImages[bytFrame]; 
            case 1: //dinosaur is currenly walking
                if (bytFrame == 7) //7 posible images -> 0- 6
                    bytFrame = 0; //reset frame once reaching 7th frame back to 0
                
                if (bytDirection == -1) //going left
                    return img_WalkingLeftImages[bytFrame]; //going left, return walkingleft images
                return img_WalkingRightImages[bytFrame]; //going right, return walking right images
            case 2: //dinosaur is currenly crouching
                if (bytFrame == 7) //7 posible images -> 0- 6
                    bytFrame = 0; //reset frame once reaching 7th frame back to 0
                    
                if (bytDirection == -1) //going left
                    return img_CrouchingLeftImages[bytFrame]; ///going left, return crouching left images
                return img_CrouchingRightImages[bytFrame]; ///going right, return crouching right images
        }

       return null; //default return statement 
    }
    public boolean checkCollision(short shtMinHitX, short shtMaxHitX) //checks to see if water impact has hit dinosaur and return boolean
    {
        //works by detecting if leftmost (shtMinHitX) and rightmost (shtMaxHitX) x coordinates of water impact fall between hitbox of dinosaur
        //hitbox of dinosaur is its current x coordinate + and minus its hitbox size
        if ((shtPositionX - shtHixBoxLength) < shtMinHitX && shtMinHitX < (shtPositionX + shtHixBoxLength))
        {
            return true;   
        }
        else if ((shtPositionX - shtHixBoxLength) < shtMaxHitX && shtMaxHitX < (shtPositionX + shtHixBoxLength))
        {
            return true;   
        }
        return false; //return false if no collision found
    }
    public byte getDisplayIndex() //method returns dinosaur's corresponding display index to graphicInterface's image view list of dinosaurs as byte
    {
        return byt_displayIndex; 
    }


    
}
