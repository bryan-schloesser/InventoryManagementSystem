package Controllers;

//Imports
import Core.Inventory;
import Core.Part;
import Core.Product;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


//Start of Main Class
public class MainController implements Initializable{

    // creates instance of inventory
    Inventory inv;

    //declarations
    @FXML
    private TextField partSearchBox;
    @FXML
    private TextField productSearchBox;
    @FXML
    private TableView<Part> partsTable;
    @FXML
    private TableView<Product> prodTable;
    private final ObservableList<Part> partInv = FXCollections.observableArrayList();
    private final ObservableList<Product> prodInv = FXCollections.observableArrayList();
    private final ObservableList<Part> partInvSearch = FXCollections.observableArrayList();
    private final ObservableList<Product> prodInvSearch = FXCollections.observableArrayList();

    //pulls in inventory
    public MainController(Inventory inv){
        this.inv = inv;
    }

    /**
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        initPartsTable();
        initProdTable();
    }

    //Generates parts table
    private void initPartsTable() {
        partInv.setAll(inv.getAllParts());
        TableColumn<Part, Double> costCol = formatPrice();
        partsTable.getColumns().addAll(costCol);
        partsTable.setItems(partInv);
        partsTable.refresh();
    }

    //generates products table
    private void initProdTable(){
        prodInv.setAll(inv.getAllProducts());
        TableColumn<Product, Double> costCol = formatPrice();
        prodTable.getColumns().addAll(costCol);
        prodTable.setItems(prodInv);
        prodTable.refresh();
    }

    /**
     * Formats table for cost
     * @param <T> Returns Table Column
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
                    } else {
                        setText("");
                    }
                }
            };
        });
        return costCol;
    }

    /**
     *
     * @param event searches for matching parts
     */
    @FXML
    private void searchForPart(MouseEvent event) {
        if (!partSearchBox.getText().trim().isEmpty()) {
            partsTable.setItems(inv.lookupPart(partSearchBox.getText().trim()));
        }
    }

    /**
     *
     * @param event changes to AddPart window
     */
    @FXML
    private void addPart(MouseEvent event
    ) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Layouts/AddPart.fxml"));
            AddPartController controller = new AddPartController(inv);

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
     * @param event loads modify part fxml and controller on button click.
     */
    @FXML
    private void modifyPart(MouseEvent event
    ) {
        try {
            Part selected = partsTable.getSelectionModel().getSelectedItem();
            if (partInv.isEmpty()) {
                errorMessage(1);
                return;
            }
            if (selected == null) {
                errorMessage(2);
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Layouts/ModifyPart.fxml"));
                ModifyPartController controller = new ModifyPartController(inv, selected);

                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();

            }
        } catch (IOException ignored) {

        }
    }

    /**
     *
     * @param event starts part delete process on delete button click
     */
    @FXML
    private void deletePart(MouseEvent event
    ) {
        Part removePart = partsTable.getSelectionModel().getSelectedItem();
        if (partInv.isEmpty()) {
            errorMessage(1);
            return;
        }
        if (removePart == null) {
            errorMessage(2);
        } else {
            boolean confirm = confirmPartDelete(removePart.getName());
            if (!confirm) {
                return;
            }
            inv.deletePart(removePart);
            partInv.remove(removePart);
            partsTable.refresh();

        }
    }

    /**
     *
     * @param name Brings in name for deletion message.
     * @return returns boolean based on confirmation
     */
    private boolean confirmPartDelete(String name) {
        Alert confirmDelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDelAlert.setTitle("Deleting Part..");
        confirmDelAlert.setHeaderText("You are about to delete " + name + ". Are you sure you want to proceed?");
        confirmDelAlert.setContentText("Click ok to confirm");

        Optional<ButtonType> result = confirmDelAlert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    /**
     *
     * @param event searches for product based on user input
     */
    @FXML
    private void searchForProduct(MouseEvent event
    ) {
        if (!productSearchBox.getText().trim().isEmpty()) {
            prodInvSearch.clear();

            for (Product product : inv.getAllProducts()) {
                if (product.getName().contains(productSearchBox.getText().trim())) {
                    prodInvSearch.add(product);
                }
                if (String.valueOf(product.getProductID()).contains(productSearchBox.getText().trim())) {
                    prodInvSearch.add(product);
                }
            }
            prodTable.setItems(prodInvSearch);
        }
    }

    /**
     *
     * @param event loads AddProduct FXML and Controller on Add Product Button Press
     */
    @FXML
    private void addProduct(MouseEvent event
    ) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Layouts/AddProduct.fxml"));
            AddProductController controller = new AddProductController(inv);

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
     * @param event error checks before loading Modify Product FXML and Controller
     */
    @FXML
    private void modifyProduct(MouseEvent event
    ) {
        try {
            Product productSelected = prodTable.getSelectionModel().getSelectedItem();
            if (prodInv.isEmpty()) {
                errorMessage(1);
                return;
            }
            if (productSelected == null) {
                errorMessage(2);

            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Layouts/ModifyProduct.fxml"));
                ModifyProductController controller = new ModifyProductController(inv, productSelected);

                loader.setController(controller);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            }
        } catch (IOException ignored) {

        }
    }

    /**
     *
     * @param event deletes Product on Mouse Click
     */
    @FXML
    private void deleteProduct(MouseEvent event
    ) {
        boolean deleted = false;
        Product removeProduct = prodTable.getSelectionModel().getSelectedItem();
        if (prodInv.isEmpty()) {
            errorMessage(1);
            return;
        }
        if (removeProduct == null) {
            errorMessage(2);
            return;
        }
        if (removeProduct.getPartsListSize() == 0) {
            boolean confirm = confirmProdDelete(removeProduct.getName());
            if (!confirm) {
                return;
            }
        } else {
            partAssocErr(removeProduct.getName());
            return;
        }
        inv.deleteProduct(removeProduct);
        prodInv.remove(removeProduct);
        prodTable.setItems(prodInv);
        prodTable.refresh();
    }

    /**
     *
     * @param name Brings in name for deletion message.
     * @return returns boolean based on confirmation
     */
    private boolean confirmProdDelete(String name){
        Alert confirmDel = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDel.setTitle("Deleting Product..");
        confirmDel.setHeaderText("You are about to delete " + name + ". Are you sure you want to proceed?");

        Optional<ButtonType> result = confirmDel.showAndWait();
        return result.get() == ButtonType.OK;

    }

    /**
     *
     * @param prodName product name for error message notification.
     */
    private void partAssocErr(String prodName){
        Alert partsAssignedErr = new Alert(Alert.AlertType.INFORMATION);
        partsAssignedErr.setTitle("Part Association Error");
        partsAssignedErr.setHeaderText(null);
        partsAssignedErr.setContentText(prodName + " cannot be deleted. There are still parts associated to it.");
        partsAssignedErr.showAndWait();
    }

    /**
     *
     * @param event on mouse click data gets cleared
     */
    @FXML
    void clearText(MouseEvent event
    ) {
        Object source = event.getSource();
        javafx.scene.control.TextField field = (javafx.scene.control.TextField) source;
        field.setText("");

        if (partSearchBox == field) {
            if (partInv.size() != 0) {
                partsTable.setItems(partInv);
            }
        }
        if (productSearchBox == field) {
            if (prodInv.size() != 0) {
                prodTable.setItems(prodInv);
            }
        }
    }

    /**
     *
     * @param errorNum brings in integer number to determine what specific error message to display
     */
    private void errorMessage(int errorNum) {
        if (errorNum == 1) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("No Inventory");
            error.setContentText("The Inventory is empty.");
            error.showAndWait();
        }
        if (errorNum == 2) {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("Invalid Selection");
            error.setContentText("Please Select an Item.");
            error.showAndWait();
        }

    }

    /**
     *
     * @param event Exits Program
     */
    @FXML
    private void exitProgram(ActionEvent event){
        Platform.exit();
    }

    /**
     *
     * @param event Exits Program on Click
     */
    @FXML
    private void exitProgramButton(MouseEvent event){
        Platform.exit();
    }

}

