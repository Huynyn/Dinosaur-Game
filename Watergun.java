
/**
 * Manages animation of watergun model by shifting right of left depending on mouse cursor x coordinate
 *
 * @author (Huy Nguyen)
 * @version (2023-06-07)
 */
import javafx.scene.image.Image;

public class Watergun
{
    private Image[] img_WatergunImages; //array of images of watergun model 
    private byte bytCurrentImageIndex; //holds the current model of weapon
    private byte bytWeaponPostionalValue; //holds the mouse cursor's x coordinate as a value ranging from 0 to 18 inclusive

    public Watergun() //default constructor
    {
        this.img_WatergunImages = new Image[0];
        this.bytCurrentImageIndex = 0;
        this.bytWeaponPostionalValue = 0; 
    }
    public Watergun(byte bytCurrentImageIndex, Image[] img_WatergunImages, byte bytWeaponPostionalValue)//constructor
    {
        this.bytCurrentImageIndex = bytCurrentImageIndex;
        this.img_WatergunImages = img_WatergunImages;
        this.bytWeaponPostionalValue = bytWeaponPostionalValue; 
    }
    public Image changeWeaponModel(double dblMouseX)
    { 
        bytWeaponPostionalValue = (byte)(dblMouseX / 52.63); //mouse x coordinate has a 1:52.63 ratio to 19 numbers (0 - 18 in bytCurrentImageIndex and bytWeaponPostionalValue)
                                                             //effectively this gives each number, 0 to 18, a 52.63 pixel range on 1000 pixel wide screen
                                                              //using divison chop off, convert passed in mouse x coordinate into corresponding number of 0 to 18
                                                              
        if (bytCurrentImageIndex < bytWeaponPostionalValue) //if current bytCurrentImageIndex (number 0 to 18) weapon model is set to is less than 
        {                                                   //bytWeaponPostionalValue, that means cursor has moved 52.63 pixels to the right of current pixel range of bytCurrentImageIndex, so increase bytCurrentImageIndex by 1
           if (bytCurrentImageIndex < img_WatergunImages.length - 1) //do not let bytCurrentImageIndex go greator than the length of image array (images available)
               bytCurrentImageIndex++;  
        }
        else if (bytCurrentImageIndex > bytWeaponPostionalValue) //if current bytCurrentImageIndex (number 0 to 18) weapon model is set to is less than 
        {                                                        //bytWeaponPostionalValue, that means cursor has moved 52.63 pixels to the left of current pixel range of bytCurrentImageIndex, so decrease bytCurrentImageIndex by 1
            bytCurrentImageIndex--; 
        }
        
        return img_WatergunImages[bytCurrentImageIndex]; //return weapon model from image array at bytCurrentImageIndex
    }
}
