package ci553.happyshop.client.picker;

import java.io.IOException;

/**
 * Handles the different action responses that correspond with the picker client's methods.
 */
public class PickerController {
    public PickerModel pickerModel;

    public void doProgressing() throws IOException {
        pickerModel.doProgressing();
    }
    public void doCollected() throws IOException {
        pickerModel.doCollected();
    }
}
