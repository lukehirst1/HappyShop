package ci553.happyshop.client.picker;

import ci553.happyshop.client.Main;

import java.io.IOException;

/**
 * Handles the different action responses that correspond with the picker client's methods.
 */
public class PickerController {
    public PickerModel pickerModel;
    protected String clicked = "src/main/resources/audio/ButtonClick.wav";

    public void doProgressing() throws IOException {
        pickerModel.doProgressing();
        Main.mainHolder.PlaySound(clicked);
    }
    public void doCollected() throws IOException {
        pickerModel.doCollected();
        Main.mainHolder.PlaySound(clicked);
    }
}
