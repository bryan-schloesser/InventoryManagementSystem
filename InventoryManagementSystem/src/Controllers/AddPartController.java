package Controllers;

//Imports
import Core.InHouse;
import Core.Inventory;
import Core.Outsourced;
import Core.Part;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

//Beginning of Class
public class AddPartController implements Initializable {

    //creates instance of Inventory
    Inventory inv;

    //Declarations
    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outSourcedRadio;
    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField stock;
    @FXML
    private TextField price;
    @FXML
    private TextField max;
    @FXML
    private TextField min;

    //company or machine id
    @FXML
    private Label companyLabel;
    @FXML
    private TextField company;

    /**
     *
     * @param inv the inventory to set
     */
    public AddPartController(Inventory inv) {
        this.inv = inv;
    }

    /**
     * Changes form to InHouse on Load
     */
    private void setToInHouse() {
        companyLabel.setText("Machine ID");
        inHouseRadio.setSelected(true);
    }

    /**
     *
     * @param num number generated for ID
     * @return returns true/false based on match being null or not
     */
    private boolean duplicateIDCheck(Integer num) {
        Part isSame = inv.lookupPart(num);
        return isSame != null;
    }

    //Generates PartID
    private void generatePartID() {
        Random randomInt = new Random();
        Integer ranNum = randomInt.nextInt(9999);
        boolean isSame;
        isSame = duplicateIDCheck(ranNum);

        if (inv.partListSize() == 0) {
            id.setText(ranNum.toString());
        } else {
            if (!isSame) {
                id.setText(ranNum.toString());
            } else {
                generatePartID();
            }
        }
    }

    /**
     *
     * @param event switches label from company to machine id
     */
    @FXML
    private void selectInHouse(MouseEvent event) {
        companyLabel.setText("Machine ID");
    }

    /**
     *
     * @param event - switches label from machine id to company
     */
    @FXML
    private void selectOutSourced(MouseEvent event) {
        companyLabel.setText("Company");
    }

    /**
     *
     * @param event - Returns to main screen on cancel button press
     */
    @FXML
    private void cancelAddPart(MouseEvent event) {
        boolean cancel = MessageBox.cancel();
        if (cancel) {
            mainScreen(event);
        }
    }

    /**
     *
     * @param fieldName field to validate
     */
    private void checkType(TextField fieldName) {

        if (fieldName == price && !fieldName.getText().trim().matches("\\d+(\\.\\d+)?")) {
            MessageBox.errorPart(5, fieldName);
            return;
        }
        if (fieldName != price && !fieldName.getText().trim().matches("[0-9]*")) {
            MessageBox.errorPart(9, fieldName);
        }

    }

    /**
     * Removes Red coloring on previous errors
     */
    private void resetTextFields(){
        TextField[] fieldNames = {name, stock, price, min, max, company};
        for (TextField field : fieldNames) {
            field.setStyle("-fx-border-color:");
        }
    }

    /**
     *
     * @param event - gathers part parameters and adds them to inventory
     */
    @FXML
    private void saveAddPart(MouseEvent event) {

        //resets border for previous errors
        resetTextFields();

        //loops through textFields for validation
        TextField[] fieldNames = {name, stock, price, min, max, company};
        for (TextField field : fieldNames) {
            if (field != name && field != company){
                checkType(field);
            }

            if (field.getText().trim().isEmpty()) {
                MessageBox.errorPart(5, field);
                return;
            } else {
                field.getText();
            }
            if (field == price && Double.parseDouble(field.getText().trim()) < 0) {
                MessageBox.errorPart(1, field);
                return;
            }
            if (field == company && inHouseRadio.isSelected() && !field.getText().trim().matches("[0-9]*")){
                MessageBox.errorPart(6,field);
                return;
            }

        }
            if (Integer.parseInt(stock.getText().trim()) < Integer.parseInt(min.getText().trim())) {
                MessageBox.errorPart(2, stock);
                return;
            }
            if (Integer.parseInt(stock.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                MessageBox.errorPart(3, stock);
                return;
            }
            if (Integer.parseInt(min.getText().trim()) > Integer.parseInt(max.getText().trim())) {
                MessageBox.errorPart(4, min);
                return;
            }
            //Determines what type of part
            if (inHouseRadio.isSelected()){
                addInHousePart();
            }else if (outSourcedRadio.isSelected()){
                addOutsourcedPart();
            }
            //goes back to main screen
            mainScreen(event);

        }

    /**
     * adds Part to inventory as InHouse Part
     */
    private void addInHousePart() {
        inv.addPart(new InHouse(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(stock.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), (Integer.parseInt(company.getText().trim()))));

    }

    /**
     * adds Part to inventory as Outsourced Part
     */
    private void addOutsourcedPart() {
        inv.addPart(new Outsourced(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(stock.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), company.getText().trim()));

    }

    /**
     *
     * @param event - sets main screen fxml and controller
     */
    private void mainScreen(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Layouts/MainScreen.fxml"));
            MainController controller = new MainController(inv);

            loader.setController(controller);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ignored) {

        }
    }

    /**
     *
     * @param event clears text on mouse click of text field.
     */
    @FXML
    private void resetText(MouseEvent event) {
        Object source = event.getSource();
        TextField fieldName = (TextField) source;
        fieldName.setText("");
    }

    /**
     *
     * @param url url required
     * @param rb resourceBundle required
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generatePartID();
        setToInHouse();
    }

}
