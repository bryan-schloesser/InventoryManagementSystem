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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

//Beginning of Main Class
public class AddProductController implements Initializable {

    //Declarations
    Inventory inv;
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
    private TextField searchTextField;
    @FXML
    private TableView<Part> partSearchTable;
    @FXML
    private TableView<Part> assocPartsTable;
    private final ObservableList<Part> partsInventory = FXCollections.observableArrayList();
    private final ObservableList<Part> partsInventorySearch = FXCollections.observableArrayList();
    private final ObservableList<Part> assocPartList = FXCollections.observableArrayList();

    /**
     *
     * @param inv inv to bring in for manipulation
     */
    public AddProductController(Inventory inv) {
        this.inv = inv;
    }

    /**
     *
     * @param url required
     * @param rb required
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        generateProductID();
        populateSearchTable();
    }


    /**
     *
     * @param num num to check if duplicate
     * @return returns true
     */
    private boolean duplicateIDCheck(Integer num) {
        Part isSame = inv.lookupPart(num);
        return isSame != null;
    }


    //Generates product ID
    private void generateProductID() {
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
                generateProductID();
            }
        }
    }

    /**
     *
     * @param num num to verify validity of
     * @return returns if match is found or not
     */
    private boolean generateNum(Integer num) {
        Part match = inv.lookupPart (num);
        return match != null;
    }


    //Populates Table
    private void populateSearchTable() {
        partsInventory.setAll(inv.getAllParts());

        TableColumn<Part, Double> costCol = formatPrice();
        partSearchTable.getColumns().addAll(costCol);

        partSearchTable.setItems(partsInventory);
        partSearchTable.refresh();
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
     * @param event error checks part addition and adds part if valid
     */
    @FXML
    private void addPart(MouseEvent event) {
        Part addPart = partSearchTable.getSelectionModel().getSelectedItem();
        boolean repeatedItem = false;

        if (addPart != null) {
            int id = addPart.getId();
            for (int i = 0; i < assocPartList.size(); i++) {
                if (assocPartList.get(i).getId() == id) {
                    MessageBox.errorProduct(1, null);
                    repeatedItem = true;
                }
            }

            if (!repeatedItem) {
                assocPartList.add(addPart);

            }

            TableColumn<Part, Double> costCol = formatPrice();
            assocPartsTable.getColumns().addAll(costCol);

            assocPartsTable.setItems(assocPartList);
        }
    }

    /**
     *
     * @param event deletes part based on check results
     */
    @FXML
    private void deletePart(MouseEvent event
    ) {
        Part removePart = assocPartsTable.getSelectionModel().getSelectedItem();
        boolean deleted = false;
        if (removePart != null) {
            boolean remove = MessageBox.deletePartConfirm(removePart.getName());
            if (remove) {
                assocPartList.remove(removePart);
                assocPartsTable.refresh();
            }
        } else {
            return;
        }
        MessageBox.deleteConfirmMessage(2, "");

    }


    /**
     *
     * @param event returns to main screen on cancel button press
     */
    @FXML
    private void cancelAddProduct(MouseEvent event
    ) {
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
     * @param event adds product to inventory after validation
     */
    @FXML
    private void saveAddProduct(MouseEvent event) {
        resetTextFields();
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

    /**
     *
     * @param fieldName specifies field to change border of to red on error
     */
    private void fieldError(TextField fieldName) {
        if (fieldName != null) {
            fieldName.setStyle("-fx-border-color: red");
        }
    }

    //Saves product to inventory
    private void saveProduct() {
        Product product = new Product(Integer.parseInt(id.getText().trim()), name.getText().trim(), Double.parseDouble(price.getText().trim()),
                Integer.parseInt(stock.getText().trim()), Integer.parseInt(min.getText().trim()), Integer.parseInt(max.getText().trim()));
        for (Part part : assocPartList) {
            product.addAssociatedPart(part);
        }

        inv.addProduct(product);

    }

    /**
     *
     * @param event clears values in field on mouse click
     */
    @FXML
    void resetText(MouseEvent event) {
        searchTextField.setText("");
        if (!partsInventory.isEmpty()) {
            partSearchTable.setItems(partsInventory);
        }

    }

    /**
     *
     * @param event changes to main screen.
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
     * @param fieldName checks field for validity
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
     * @param <T> Creates column and returns formatted prices.
     * @return
     */
    private <T> TableColumn<T, Double> formatPrice() {
        TableColumn<T, Double> costCol = new TableColumn("Price");
        costCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        // Format as currency
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
