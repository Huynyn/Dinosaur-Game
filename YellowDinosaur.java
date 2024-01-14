
/**
 * Child of dinosaur class.  Notably: really fast
 *
 * @author (Huy Nguyen)
 * @version (2023-06-07)
 */
import javafx.scene.image.Image;

public class YellowDinosaur extends Dinosaur
{
    public YellowDinosaur(Image[] img_AllDinosaurs, byte byt_displayIndex)
    {
        super(img_AllDinosaurs, byt_displayIndex); //call parent class (dinosaur class) method in order to set iamges and dinosau index
        
        //green dinosaur specific variables
        bytSpeed = 40; 
        bytCrouchSpeed = 5;
        shtChanceChangeDirection = 170; 
        shtChanceIdle = 17; 
        shtChanceCrouch = 22; 
        shtChanceStopCrouching = 30; 
        bytIdleCycles = 6;
        bytType = 2; 
        shtNumPoints = 30; 
    }
}
