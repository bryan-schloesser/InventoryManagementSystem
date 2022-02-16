package Controllers;

//Imports
import Core.Inventory;
import Core.Part;
import Core.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

//Beginning of Main Class
public class ModifyProductController implements Initializable {

    //Declarations
    Inventory inv;
    Product product;
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
    private TableView<Part> assocPartsTable;
    @FXML
    private TableView<Part> partSearchTable;
    @FXML
    private TextField searchTextField;
    private final ObservableList<Part> partsInventory = FXCollections.observableArrayList();
    private final ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    private final ObservableList<Part> assocPartList = FXCollections.observableArrayList();

    /**
     *
     * @param inv inv to bring in and manipulate
     * @param product product to modify
     */
    public ModifyProductController(Inventory inv, Product product) {
        this.inv = inv;
        this.product = product;
    }


    /**
     *
     * @param url required
     * @param rb required
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populateSearchTable();
        setData();
    }

    //populates search table
    private void populateSearchTable() {
        partsInventory.setAll(inv.getAllParts());

        TableColumn<Part, Double> costCol = formatPrice();
        partSearchTable.getColumns().addAll(costCol);

        partSearchTable.setItems(partsInventory);
        partSearchTable.refresh();
    }

    /**
     *
     * @param event clears text field on mouse click of textfield.
     */
    @FXML
    void resetText(MouseEvent event) {
        Object source = event.getSource();
        TextField fieldName = (TextField) source;
        fieldName.setText("");
        if (fieldName == searchTextField) {
            partSearchTable.setItems(partsInventory);
        }
    }

    /**
     *
     * @param event searches for matching parts
     */
    @FXML
    private void searchForParts(MouseEvent event) {
        if (!searchTextField.getText().trim().isEmpty()) {
            partSearchTable.setItems(inv.lookupPart(searchTextField.getText().trim()));
        }
    }

    /**
     *
     * @param event - deletes part on valid attempt
     */
    @FXML
    private void deletePart(MouseEvent event) {
        Part removePart = assocPartsTable.getSelectionModel().getSelectedItem();
        boolean deleted = false;
        if (removePart != null) {
            boolean remove = deletePartConfirm(removePart.getName());
            if (remove) {
                deleted = product.deleteAssociatedPart(removePart);
                assocPartList.remove(removePart);
                assocPartsTable.refresh();
            }
        } else {
            return;
        }
        if (deleted) {
            MessageBox.deleteConfirmMessage(1, removePart.getName());
        } else {
            MessageBox.deleteConfirmMessage(2, "");
        }

    }

    /**
     *
     * @param event attempts to error check adding of part then adds part if valid.
     */
    @FXML
    private void addPart(MouseEvent event) {
        Part addPart = partSearchTable.getSelectionModel().getSelectedItem();
        boolean repeatedItem = false;

        if (addPart != null) {
            int id = addPart.getId();
            for (Part part : assocPartList) {
                if (part.getId() == id) {
                    MessageBox.errorProduct(1, null);
                    repeatedItem = true;
                }
            }

            if (!repeatedItem) {
                assocPartList.add(addPart);
            }
            assocPartsTable.setItems(assocPartList);
        }
    }

    /**
     *
     * @param event returns to main screen on cancel button press
     */
    @FXML
    private void cancelModify(MouseEvent event) {
        boolean cancel = MessageBox.cancel();
        if (cancel) {
            mainScreen(event);
        }
    }

    /**
     * Removes Red coloring on previous errors
     */
    private void resetTextFields(){
        TextField[] fieldNames = {name, stock, price, min, max};
        for (TextField field : fieldNames) {
            field.setStyle("-fx-border-color:");
        }
    }

    /**
     *
     * @param event saves product after validation check
     */
    @FXML
    private void saveProduct(MouseEvent event) {

        //resets red border on previous errors
        resetTextFields();

        //loops through user input for errors
        TextField[] fieldNames = {name, stock, price, min, max};
        for (TextField field : fieldNames) {
            if (field != name){
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

        //saves product
        saveProduct();

        //goes back to main screen
        mainScreen(event);

    }

    //Saves Product
    private void saveProduct() {
        Product product = new Product(Integer.parseInt(id.getText().trim()), name.getText().trim(), Double.parseDouble(price.getText().trim()),
                Integer.parseInt(stock.getText().trim()), Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()));
        for (Part part : assocPartList) {
            product.addAssociatedPart(part);
        }

        inv.updateProduct(product);

    }

    //sets inventory values
    private void setData() {
        for (int i = 0; i < 9999; i++) {
            Part part = product.lookupAssociatedPart(i);
            if (part != null) {
                assocPartList.add(part);
            }
        }
        TableColumn<Part, Double> costCol = formatPrice();
        assocPartsTable.getColumns().addAll(costCol);
        assocPartsTable.setItems(assocPartList);
        this.name.setText(product.getName());
        this.id.setText((Integer.toString(product.getProductID())));
        this.stock.setText((Integer.toString(product.getInStock())));
        this.price.setText((Double.toString(product.getPrice())));
        this.min.setText((Integer.toString(product.getMin())));
        this.max.setText((Integer.toString(product.getMax())));

    }

    /**
     *
     * @param name part name to delete
     * @return returns based on user confirmation.
     */
    private boolean deletePartConfirm(String name) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Delete part");
        alert.setHeaderText(" You are about to delete part " + name);
        alert.setContentText("Click OK to Confirm.");
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     *
     * @param event loads main screen fxml and controller
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
     * @param fieldName field to check
     */
    private void checkType(TextField fieldName) {

        if (fieldName == price & !fieldName.getText().trim().matches("\\d+(\\.\\d+)?")) {
            MessageBox.errorProduct(2, fieldName);
            return;
        }
        if (fieldName != price & !fieldName.getText().trim().matches("[0-9]*")) {
            MessageBox.errorProduct(2, fieldName);
        }

    }

    /**
     *
     * @param <T> return type
     * @return - returns values as formatted prices.
     */
    private <T> TableColumn<T, Double> formatPrice() {
        TableColumn<T, Double> costCol = new TableColumn("Price");
        costCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        costCol.setCellFactory((TableColumn<T, Double> column) -> {
            return new TableCell<T, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    if (!empty) {
                        setText("$" + String.format("%10.2f", item));
                    }
                    else{
                        setText("");
                    }
                }
            };
        });
        return costCol;
    }


}
