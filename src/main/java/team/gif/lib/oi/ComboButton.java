package team.gif.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

/**
 * A {@link Button} that gets its state from the combination of two other buttons
 */
public class ComboButton extends Button {

    private final Button buttonOne;
    private final Button buttonTwo;

    /**
     * Create a combination button for triggering commands.
     *
     * Returns true when both buttons return true.
     *
     * @param one the first button
     * @param two the second button
     */
    public ComboButton(Button one, Button two) {
        buttonOne = one;
        buttonTwo = two;
    }

    /**
     * Gets the value of the button combination
     *
     * @return AND product of both buttons
     */
    @Override
    public boolean get() {
        return buttonOne.get() && buttonTwo.get();
    }
}