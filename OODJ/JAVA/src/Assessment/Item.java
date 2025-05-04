package Assessment;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class Item implements write2file {
    public String itemId;
    public String itemName;
    public int quantity;
    public double itemPrice;
    public Set<String> supplierIds;
    
    public Item(String ID, String IN, double IP, int IQ, Set<String> SID)
    {
        itemId = ID;
        itemName = IN;
        itemPrice = IP;
        quantity = IQ;
        supplierIds = SID;
    }
    
    public void wri2file()
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("item.txt", true))) 
        {
            String SuppliertemIds = String.join(" ", supplierIds);
            String itemData =itemId + " "+itemName+" "+itemPrice+" "+quantity+" "+supplierIds;
            writer.write(itemData);
            writer.newLine();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
            
    } 
}
