package Assessment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class Supplier implements write2file {
    private String supplierId;
    private String supplierName;
    private Set<String> itemIds;
    
    public Supplier(String SI, String SN, Set<String> SIds)
    {
        supplierId = SI;
        supplierName = SN;
        itemIds = SIds;
    }
    
    //method to write supplier data to a file 
    public void wri2file()
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("supplier.txt", true))) 
        {
            String itemIdsStr = String.join(" ", itemIds);
            String supplierData = supplierId + " "+supplierName+" "+itemIdsStr;
            writer.write(supplierData);
            writer.newLine();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public String getSupplierId()
    {
        return supplierId;
    }
    
    public Set<String> getItemIds()
    {
        return itemIds;
    }
    
    public String getItemSupplierCode(String itemId) {
    try (BufferedReader reader = new BufferedReader(new FileReader("supplier.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            if (parts.length >= 3 && parts[2].equals(itemId)) {
                return parts[0];
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    return null; // or any other value to indicate that the item ID is not provided by any supplier
}
}
