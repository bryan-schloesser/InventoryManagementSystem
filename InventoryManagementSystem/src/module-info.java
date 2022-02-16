module InventoryManagementSystem {
    requires javafx.controls;
    requires javafx.fxml;

    opens Main;
    opens Core;
    opens Controllers;
    opens FXML_Layouts;
}