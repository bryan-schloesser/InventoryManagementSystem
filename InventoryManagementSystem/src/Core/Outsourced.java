package Core;

//Start of Main Class
public class Outsourced extends Part{

    //local variables
    private String companyName;

    /**
     *
     * @param id id to set
     * @param name name to set
     * @param price price to set
     * @param stock stock to set
     * @param min min to set
     * @param max max to set
     * @param companyName company name to set
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName){
        super(id, name, price, stock, min, max);
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
        setCompanyName(companyName);

    }

    /**
     *
     * @param companyName company name to set
     */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }

    /**
     *
     * @return company name to return
     */
    public String getCompanyName(){
        return companyName;
    }
}
