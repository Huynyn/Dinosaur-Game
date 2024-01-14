
/**
 * Manages enabling of water impact graphic, its animation, and its size
 *
 * @author (your name)
 * @version (a version number or a date)
 */
import javafx.scene.image.Image;

public class WaterImpact
{
    private Image[] img_WaterImpactImages; //holds images of water impact animation
    private boolean bolEnabled; //holds whether or not water impact has been enabled
    private short shtPositionX; //holds x coordinate of water impact
    private byte bytFrame; //holds frame in animation 
    
    private final byte BYT_IMPACTSIZE = 5; //holds half the size of water impact area of affect BYT_IMPACTSIZE + BYT_IMPACTSIZE = total area of affect right to left

    public WaterImpact()
    {
        this.bolEnabled = false;
        this.shtPositionX = 0; 
        this.img_WaterImpactImages = new Image[0];
        this.bytFrame = 0; 
    }
    public WaterImpact(boolean bolEnabled,short shtPositionX,Image[] img_WaterImpactImages)
    {
        this.bolEnabled = bolEnabled;
        this.shtPositionX = shtPositionX; 
        this.img_WaterImpactImages = img_WaterImpactImages;
    }
    public Image animate()
    {   
        bytFrame++; 
        
        if (bytFrame == 3)//once bytFrame has reached 3, that means animation has finished 
        {
            bolEnabled = false; 
            return null; //return an empty image to set water impact image to in graphicsInterface
        }
        
        return img_WaterImpactImages[bytFrame]; 
    }
    public void runWaterImpact(short shtPositionX)
    {
       bytFrame = -1; //set frame to zero because animate method run after increases it by one, making it 0 to fetch first index of image array
       this.shtPositionX = (short)(shtPositionX - BYT_IMPACTSIZE); //set position of water impact, - BYT_IMPACTSIZE because graphics are built from right, so shift to left to center
       bolEnabled = true;  //enable
    }
    public byte getBulletSize()
    {
        return BYT_IMPACTSIZE; 
    }
    public short getPositionX()
    {
        return shtPositionX; 
    }
    public boolean getEnabled()
    {
        return bolEnabled; 
    }

}
