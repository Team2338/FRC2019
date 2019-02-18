package team.gif.lib.oi;

import edu.wpi.first.wpilibj.buttons.Button;

/**
 * A {@link Button} that gets the opposite state of another button
 */
public class NotButton extends Button {

    private final Button button;

    /**
     * Trigger a command whenever a button is not being pressed.
     *
     * Returns true when the button is open.
     *
     * This can be very useful for setting up large button combinations with neutral conditions.
     *
     * @param button the button to invert
     */
    public NotButton(Button button) {
        this.button = button;
    }

    /**
     * Gets the opposite of the value of the button
     *
     * @return inverse of button
     */
    @Override
    public boolean get() {
        return !button.get();
    }
}