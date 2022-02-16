package Core;

//Imports
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

//Start of Inventory Class
public class Inventory {

    //Local Variables
    private final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     *
     * @param newPart adds part to list
     */
    public void addPart(Part newPart){
        if(newPart != null){
            this.allParts.add(newPart);
        }
    }

    /**
     *
     * @param newProduct adds product to list
     */
    public void addProduct(Product newProduct){
        if (newProduct != null){
            this.allProducts.add(newProduct);
        }
    }

    /**
     *
     * @param partId part id to lookup
     * @return returns matching part
     */
    public Part lookupPart(int partId){
        for(int counter = 0; counter < allParts.size(); counter++){
            if (allParts.get(counter).getId() == partId){
                return allParts.get(counter);
            }
        }
        return null;
    }

    /**
     *
     * @param productId product id to lookup
     * @return returns matching product from list
     */
    public Product lookupProduct(int productId){
        for (int counter = 0; counter < allProducts.size(); counter++){
            if (allProducts.get(counter).getProductID() == productId){
                return allProducts.get(counter);
            }
        }
        return null;
    }

    /**
     *
     * @param searchedString string to lookup in list of parts
     * @return returns results from all parts if matches part name or id
     */
    public ObservableList<Part> lookupPart(String searchedString){
        ObservableList partsList = FXCollections.observableArrayList();
        if (!allParts.isEmpty()){
            for (Part part : getAllParts()){
                if (part.getName().contains(searchedString)){
                    partsList.add(part);
                }
                if (String.valueOf(part.getId()).contains(searchedString)){
                    partsList.add(part);
                }
            }
        }
        return partsList;
    }

    /**
     *
     * @param searchedString string to lookup in Product List
     * @return returns matching items from Product List
     */
    public ObservableList<Product> lookupProduct(String searchedString){
        ObservableList<Product> productList = FXCollections.observableArrayList();
        if (!allProducts.isEmpty()){
            for (Product product : getAllProducts()){
                if (product.getName().contains(searchedString)){
                    productList.add(product);
                }
                if ( String.valueOf(product.getProductID()).contains(searchedString)){
                    productList.add(product);
                }

            }
        }
        return productList;
    }

    /**
     *
     * @param selectedPart part to update
     */
    public void updatePart(Part selectedPart){
        for (int counter = 0; counter < allParts.size(); counter++){
            if (allParts.get(counter).getId() == selectedPart.getId()){
                allParts.set(counter, selectedPart);
            }
        }
    }

    /**
     *
     * @param selectedProduct product to update
     */
    public void updateProduct(Product selectedProduct){
        for (int counter = 0; counter < allProducts.size(); counter++){
            if (allProducts.get(counter).getProductID() == selectedProduct.getProductID()){
                allProducts.set(counter,selectedProduct);
            }
        }
    }

    /**
     *
     * @param selectedPart part to delete
     */
    public void deletePart(Part selectedPart){
            for(int counter = 0; counter < allParts.size(); counter++){
                if(allParts.get(counter).getId() == selectedPart.getId()){
                    allParts.remove(counter);
                    return;
                }
            }
    }

    /**
     *
     * @param selectedProduct product to delete
     */
    public void deleteProduct(Product selectedProduct){
        for (int counter = 0; counter < allProducts.size(); counter++){
            if (allProducts.get(counter).getProductID() == selectedProduct.getProductID()){
                allProducts.remove(counter);
                return;
            }
        }
    }

    /**
     *
     * @return returns all parts list
     */
    public ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     *
     * @return retursn all products list
     */
    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     *
     * @return returns size of list
     */
    public int partListSize() {
        return allParts.size();
    }

    /**
     *
     * @return returns size of list
     */
    public int productListSize() {
        return allProducts.size();
    }

}
