package Core;

//Imports
import javafx.collections.ObservableList;
import java.util.ArrayList;

//Start of Main Class
public class Product {
    private final ArrayList<Part> associatedParts = new ArrayList<Part>();
    private int productID;
    private String name;
    private double price;
    private int inStock;
    private int min;
    private int max;

    /**
     *
     * @param productID productID to set
     * @param name name to set
     * @param price price to set
     * @param inStock stock to set
     * @param min min to set
     * @param max max to set
     */
    public Product(int productID, String name, double price, int inStock, int min, int max){
        setProductID(productID);
        setName(name);
        setPrice(price);
        setInStock(inStock);
        setMin(min);
        setMax(max);
    }

    /**
     *
     * @param id sets productid
     */
    public void setProductID(int id){
        this.productID = id;
    }

    /**
     *
     * @param name name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     *
     * @param price price to set
     */
    public void setPrice(double price){
        this.price = price;
    }

    /**
     *
     * @param inStock stock to set
     */
    public void setInStock(int inStock){
        this.inStock = inStock;
    }

    /**
     *
     * @param min min to set
     */
    public void setMin(int min){
        this.min = min;
    }

    /**
     *
     * @param max max to set
     */
    public void setMax(int max){
        this.max = max;
    }

    /**
     *
     * @return product id to return
     */
    public int getProductID(){
        return productID;
    }

    /**
     *
     * @return name to return
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return price to return
     */
    public double getPrice(){
        return price;
    }

    /**
     *
     * @return stock to return
     */
    public int getInStock(){
        return inStock;
    }

    /**
     *
     * @return min to return
     */
    public int getMin(){
        return min;
    }

    /**
     *
     * @return max to return
     */
    public int getMax(){
        return max;
    }

    /**
     *
     * @param part part to add to product
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     *
     * @param selectedAssociatedPart part to delete from product
     * @return returns based on if part was deleted
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart){
        for (int counter = 0; counter < associatedParts.size(); counter++){
            if (associatedParts.get(counter).getId() == selectedAssociatedPart.getId()) {
                associatedParts.remove(counter);
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @return returns Associated Parts List
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return (ObservableList<Part>) associatedParts;
    }

    /**
     *
     * @param partToSearch part to search for
     * @return returns results of search
     */
    public Part lookupAssociatedPart(int partToSearch) {
        for (int i = 0; i < associatedParts.size(); i++) {
            if (associatedParts.get(i).getId() == partToSearch) {
                return associatedParts.get(i);
            }
        }
        return null;
    }

    /**
     *
     * @return returns list size of associated parts
     */
    public int getPartsListSize() {
        return associatedParts.size();
    }

}
