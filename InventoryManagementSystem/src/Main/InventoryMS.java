package Main;

//Imports
import Core.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class InventoryMS extends Application {

    /**
     *
     * @param inv passes instance of inventory to add Existing data too
     */
    void addExistingInv(Inventory inv) {

        //Add Parts
        Part chain = new InHouse(123, "Chain", 10.93, 65, 2, 80, 101);
        Part grips = new InHouse(213, "Grips", 14.92, 8, 3, 76, 103);
        Part pedals = new InHouse(243, "Pedals", 34.83, 7, 6, 99, 102);
        inv.addPart(chain);
        inv.addPart(pedals);
        inv.addPart(grips);
        inv.addPart(new InHouse(433, "Rims", 64.21, 11, 15, 88, 104));
        inv.addPart(new InHouse(451, "Tires", 5.24, 5, 5, 77, 105));
        Part decals = new Outsourced(26, "Decals", 3.34, 13, 8, 44, "Trek");
        Part seat = new Outsourced(62, "Seat", 34.69, 19, 7, 33, "Surly");
        Part breakWires = new Outsourced(76, "Brake Wires", 22.39, 13, 6, 100, "Huffy");
        inv.addPart(decals);
        inv.addPart(seat);
        inv.addPart(breakWires);
        inv.addPart(new Outsourced(12, "Handlebar", 233.99, 22, 1, 50, "Specialized"));
        inv.addPart(new Outsourced(345, "Frame", 754.99, 26, 1, 50, "Trek"));

        //Add Products
        Product roadster = new Product(1020, "Roadster H-123", 3119.99, 13, 23, 50);
        roadster.addAssociatedPart(chain);
        roadster.addAssociatedPart(decals);
        inv.addProduct(roadster);
        Product hybrid_b2324 = new Product(3010, "Hybrid B2324", 1239.99, 29, 23, 76);
        hybrid_b2324.addAssociatedPart(grips);
        hybrid_b2324.addAssociatedPart(seat);
        inv.addProduct(hybrid_b2324);
        Product mountain_blazer_ex313 = new Product(1024, "Mountain Blazer EX313", 1923.99, 30, 23, 76);
        mountain_blazer_ex313.addAssociatedPart(pedals);
        mountain_blazer_ex313.addAssociatedPart(breakWires);
        inv.addProduct(mountain_blazer_ex313);
        Product easyRide = new Product(1240, "EasyRide", 2239.99, 12, 1, 76);
        inv.addProduct(easyRide);
        inv.addProduct(new Product(5230, "Road King", 1129.99, 6, 1, 76));

    }

    public static void main(String[] args){
        launch(args);
    }

    /**
     *
     * @param stage stage for initial load
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception{
        Inventory inv = new Inventory();
        addExistingInv(inv);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML_Layouts/MainScreen.fxml"));
        Controllers.MainController mainController = new Controllers.MainController(inv);
        loader.setController(mainController);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


}