
/**
 * Child of dinosaur class.  Easiest dinosaur to shoot due to little changes in direction, a lot of time spend crouching, high change to idle and a long idle time
 *
 * @author (Huy Nguyen)
 * @version (2023-06-07)
 */
import javafx.scene.image.Image;

public class GreenDinosaur extends Dinosaur
{
    public GreenDinosaur(Image[] img_AllDinosaurs, byte byt_displayIndex)
    {
        super(img_AllDinosaurs, byt_displayIndex); //call parent class (dinosaur class) method in order to set iamges and dinosau index
        
        //green dinosaur specific variables
        bytSpeed = 7; 
        bytCrouchSpeed = 2;
        shtChanceChangeDirection = 10; 
        shtChanceIdle = 100; 
        shtChanceCrouch = 200; 
        shtChanceStopCrouching = 60; 
        bytIdleCycles = 4;
        bytType = 0; 
        shtNumPoints = 10; 
    }
}
