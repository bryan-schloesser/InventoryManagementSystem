package Controllers;

//Imports
import Core.InHouse;
import Core.Inventory;
import Core.Outsourced;
import Core.Part;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

//Beginning of Main Class
public class ModifyPartController implements Initializable {

    //Declarations
    Inventory inv;
    Part part;
    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outSourcedRadio;
    @FXML
    private Label companyLabel;
    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextField stock;
    @FXML
    private TextField min;
    @FXML
    private TextField max;
    @FXML
    private TextField company;
    @FXML
    private Button modifyPartSaveButton;

    /**
     *
     * @param inv inventory to manipulate
     * @param part part to modify
     */
    public ModifyPartController(Inventory inv, Part part) {
        this.inv = inv;
        this.part = part;
    }

    /**
     *
     * @param url required
     * @param rb required
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setData();
    }

    //Sets Data based on InHouse or Outsourced
    private void setData() {

        if (part instanceof InHouse) {

            InHouse inHousePart = (InHouse) part;
            inHouseRadio.setSelected(true);
            companyLabel.setText("Machine ID");
            this.name.setText(inHousePart.getName());
            this.id.setText((Integer.toString(inHousePart.getId())));
            this.stock.setText((Integer.toString(inHousePart.getStock())));
            this.price.setText((Double.toString(inHousePart.getPrice())));
            this.min.setText((Integer.toString(inHousePart.getMin())));
            this.max.setText((Integer.toString(inHousePart.getMax())));
            this.company.setText((Integer.toString(inHousePart.getMachineId())));

        }

        if (part instanceof Outsourced) {

            Outsourced outsourcedPart = (Outsourced) part;
            outSourcedRadio.setSelected(true);
            companyLabel.setText("Company");
            this.name.setText(outsourcedPart.getName());
            this.id.setText((Integer.toString(outsourcedPart.getId())));
            this.stock.setText((Integer.toString(outsourcedPart.getStock())));
            this.price.setText((Double.toString(outsourcedPart.getPrice())));
            this.min.setText((Integer.toString(outsourcedPart.getMin())));
            this.max.setText((Integer.toString(outsourcedPart.getMax())));
            this.company.setText(outsourcedPart.getCompanyName());
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
     * @param event switches form for InHouse part
     */
    @FXML
    private void selectInHouse(MouseEvent event
    ) {
        companyLabel.setText("Machine ID");
        company.setText("");

    }

    /**
     *
     * @param event switches form for Outsourced part
     */
    @FXML
    private void selectOutSourced(MouseEvent event
    ) {
        companyLabel.setText("Company");
        company.setText("");
    }

    /**
     *
     * @param event changes window to main screen on cancellation
     */
    @FXML
    private void cancelModifyPart(MouseEvent event
    ) {
        boolean cancel = cancel();
        if (cancel) {
            mainScreen(event);
        }
    }

    //removes red border from previous errors
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
    private void saveModifyPart(MouseEvent event) {

        //resets borders of previous errors
        resetTextFields();

        //loops through all textfields for validation
        TextField[] fieldNames = {name, stock, price, min, max, company};
        for (TextField field : fieldNames) {
            //validates type
            if (field !=name && field !=company){
                checkType(field);
            }
            //checks for empty or null values
            if (field.getText().trim().isEmpty()) {
                MessageBox.errorPart(5, field);
                return;
            } else {
                field.getText();
            }
            //checks for negative price
            if (field == price && Double.parseDouble(field.getText().trim()) < 0) {
                MessageBox.errorPart(1, field);
                return;
            }
            //checks for a non integer machine ID
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
            updateItemInHouse();
        }else if (outSourcedRadio.isSelected()){
            updateItemOutsourced();
        }
        //goes back to main screen
        mainScreen(event);

    }

    //Updates InHouse Item
    private void updateItemInHouse() {
        inv.updatePart(new InHouse(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(stock.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), Integer.parseInt(company.getText().trim())));
    }

    //Updates Outsourced Item
    private void updateItemOutsourced() {
        inv.updatePart(new Outsourced(Integer.parseInt(id.getText().trim()), name.getText().trim(),
                Double.parseDouble(price.getText().trim()), Integer.parseInt(stock.getText().trim()),
                Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()), company.getText().trim()));
    }

    /**
     *
     * @param event loads main screen fxml and controller for use
     */
    private void mainScreen(MouseEvent event) {
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
     * @param fieldName field to check
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
     *
     * @return returns true/false based on user confirmation of cancellation
     */
    private boolean cancel() {
        Alert cancelConfirm = new Alert(Alert.AlertType.CONFIRMATION);
        cancelConfirm.setTitle("Cancel");
        cancelConfirm.setHeaderText("Are you sure you would like to Cancel?");
        cancelConfirm.setContentText("Click ok to confirm");

        Optional<ButtonType> result = cancelConfirm.showAndWait();
        return result.get() == ButtonType.OK;
    }
}
