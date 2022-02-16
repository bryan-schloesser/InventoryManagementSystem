package Core;

//Start of InHouse Class
public class InHouse extends Part{

    //local variables
    private int machineId;

    /**
     * Constructor
     * @param id id to set
     * @param price price to set
     * @param stock stock amount to set
     * @param min min to set
     * @param max max to set
     * @param machineId machine Id to set
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id, name, price, stock, min, max);
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
        setMachineId(machineId);
    }

    /**
     *
     * @param mId machine id to set
     */
    public void setMachineId(int mId){
        this.machineId = mId;
    }

    /**
     *
     * @return returns machine id
     */
    public int getMachineId(){
        return machineId;
    }

}
