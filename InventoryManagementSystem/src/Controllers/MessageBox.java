package Controllers;

//Imports
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import java.util.Optional;


//Start of Message Box Class
public class MessageBox {

    /**
     *
     * @param errorNum error number to match case
     * @param fieldName field to change to red on error
     */
    public static void errorPart(int errorNum, TextField fieldName) {
        fieldError(fieldName);
        Alert partErrors = new Alert(Alert.AlertType.ERROR);
        partErrors.setTitle("Error When Adding Part");
        partErrors.setHeaderText("Cannot Add Part");
        switch (errorNum) {
            case 1: {
                partErrors.setContentText("value cannot be negative or zero");
                break;
            }
            case 2: {
                partErrors.setContentText("Please choose inventory number greater than minimum.");
                break;
            }
            case 3: {
                partErrors.setContentText("Please choose inventory number less than maximum.");
                break;
            }
            case 4: {
                partErrors.setContentText("Minimum cannot be higher than Maximum.");
                break;
            }
            case 5: {
                partErrors.setContentText("Field is not valid. Please check values and try again.");
                break;
            }
            case 6: {
                partErrors.setContentText("Machine ID has to be an integer.");
            }
            case 9: {
                partErrors.setContentText("Invalid format. Please check formatting and submit again.");
                break;
            }
            default: {
                partErrors.setContentText("Something went wrong. Please Try again");
                break;
            }
        }
        partErrors.showAndWait();
    }

    /**
     *
     * @param errorNum error to match case
     * @param fieldName field name to change border to red on error
     */
    public static void errorProduct(int errorNum, TextField fieldName) {
        fieldError(fieldName);

        Alert productError = new Alert(Alert.AlertType.ERROR);
        productError.setTitle("Error adding product");
        productError.setHeaderText("Cannot add product");
        switch (errorNum) {
            case 1: {
                productError.setContentText("value cannot be negative or zero");
                break;
            }
            case 2: {
                productError.setContentText("Please choose inventory number greater than minimum.");
                break;
            }
            default: {
                productError.setContentText("Something went wrong. Please Try again");
                break;
            }
        }
        productError.showAndWait();
    }

    /**
     *
     * @param fieldName fieldName to change border to red
     */
    private static void fieldError(TextField fieldName) {
            fieldName.setStyle("-fx-border-color: red");
    }

    /**
     *
     * @param name name of part to delete
     * @return returns once user clicks ok
     */
    public static boolean deletePartConfirm(String name) {
        Alert delPart = new Alert(Alert.AlertType.CONFIRMATION);
        delPart.setTitle("Delete part");
        delPart.setHeaderText("You are about to delete the part " + name);
        delPart.setContentText("Click OK to confirm");
        Optional<ButtonType> result = delPart.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     *
     * @return returns user input of cancellation
     */
    public static boolean cancel() {
        Alert cancelConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        cancelConfirm.setTitle("Cancel");
        cancelConfirm.setHeaderText("Are you sure you want to cancel?");
        cancelConfirm.setContentText("Click ok to confirm");
        Optional<ButtonType> result = cancelConfirm.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     *
     * @param errorNum number to match case of error
     * @param name name of object to delete
     */
    public static void deleteConfirmMessage(int errorNum, String name) {
        Alert deleteConfirmAlert = new Alert(Alert.AlertType.INFORMATION);
        if (errorNum != 2) {
            deleteConfirmAlert.setTitle("Confirmed");
            deleteConfirmAlert.setHeaderText(null);
            deleteConfirmAlert.setContentText(name + " has been deleted");
            deleteConfirmAlert.showAndWait();
        } else {
            deleteConfirmAlert.setTitle("Deletion Error");
            deleteConfirmAlert.setHeaderText(null);
            deleteConfirmAlert.setContentText("There was an error when attempting deletion request.");
        }
    }
}
