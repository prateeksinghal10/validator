package validation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hodor on 06-01-2019.
 */
public class ConstraintViolations {

    private boolean valid;

    private List<String> violationsMessages =new ArrayList<>();

    public ConstraintViolations(boolean valid, String... violationsMessages) {
        this.valid = valid;
        this.violationsMessages.addAll(Arrays.asList(violationsMessages));
    }

    public boolean hasVoilations() {
        return valid;
    }

    /*public void setValid(boolean valid) {
        this.valid = valid;
    }*/

    public List<String> getViolationsMessages() {
        return violationsMessages;
    }

    /*public void setViolationsMessages(String... violationsMessages) {
        this.violationsMessages.addAll(Arrays.asList(violationsMessages));
    }*/
}
