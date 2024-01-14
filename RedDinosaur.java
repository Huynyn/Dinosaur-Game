
/**
 * Child of dinosaur class.  Slightly harder to hit than green dinosaur
 *
 * @author (Huy Nguyen)
 * @version (2023-06-07)
 */
import javafx.scene.image.Image;

public class RedDinosaur extends Dinosaur
{
    public RedDinosaur(Image[] img_AllDinosaurs, byte byt_displayIndex)
    {
        super(img_AllDinosaurs, byt_displayIndex); //call parent class (dinosaur class) method in order to set iamges and dinosau index
        
        //green dinosaur specific variables
        bytSpeed = 12; 
        bytCrouchSpeed = 6;
        shtChanceChangeDirection = 60; 
        shtChanceIdle = 70; 
        shtChanceCrouch = 40; 
        shtChanceStopCrouching = 30; 
        bytIdleCycles = 3;
        bytType = 1; 
        shtNumPoints = 20; 
    }
}
