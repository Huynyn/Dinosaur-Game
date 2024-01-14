
/**
 * Child of dinosaur class.  Strongest dinosaur, being fast (slower than yellow dinosaur) and most unpredictable
 *
 * @author (Huy Nguyen)
 * @version (2023-06-07)
 */
import javafx.scene.image.Image;

public class BlueDinosaur extends Dinosaur
{
    public BlueDinosaur(Image[] img_AllDinosaurs, byte byt_displayIndex)
    {
        super(img_AllDinosaurs, byt_displayIndex); //call parent class (dinosaur class) method in order to set iamges and dinosau index
        
        //green dinosaur specific variables
        bytSpeed = 33; 
        bytCrouchSpeed = 15;
        shtChanceChangeDirection = 150; 
        shtChanceIdle = 3; 
        shtChanceCrouch = 2; 
        shtChanceStopCrouching = 400; 
        bytIdleCycles = 2;
        bytType = 3; 
        shtNumPoints = 40; 
    }
}
