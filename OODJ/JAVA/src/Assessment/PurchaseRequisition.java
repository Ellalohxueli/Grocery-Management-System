package Assessment;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PurchaseRequisition  {


    private int prId;
    String itemId;
    int quantity;
    Date dateRequired;
    private String requestStatus;
    private String name;
    private String supplierId;

    public PurchaseRequisition(int prId, String itemId, int quantity, Date dateRequired, String requestStatus, String name, String supplierId) {
    this.prId = prId;
    this.itemId = itemId;
    this.quantity = quantity;
    this.dateRequired = dateRequired;
    this.requestStatus = requestStatus;
    this.name = name; // Changed to 'name'
    this.supplierId = supplierId; // Changed to 'supplierId'
}


    public int getPrId() {
        return prId;
    }

public void saveToFile(String fileName) {
    try {
        FileWriter writer = new FileWriter(fileName, true);
        PrintWriter printWriter = new PrintWriter(writer);

        // Format the date as dd-MM-yyyy
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(dateRequired);

        // Save the PR details to the file
        printWriter.println(prId + " " + itemId + " " + quantity + " " + formattedDate + " " + requestStatus +
            " " + name + " " + supplierId);

        printWriter.close();
    } catch (IOException e) {
        System.out.println("Error saving PR to file.");
    }
}

    @Override
    public String toString() {
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    String formattedDate = dateFormat.format(dateRequired);

    return "PR ID: " + prId +
            ", Item Code: " + itemId +
            ", Quantity: " + quantity +
            ", Date Required: " + formattedDate +
            ", Request Status: " + requestStatus;
}
 
    
    public String getItemId() {
        return itemId;
    }
    

     public Date getDateRequired() {
        return dateRequired;
    }
     
     public int getQuantity() {
        return quantity;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public String getName() {
        return name;
    }

    public String getSupplierId() {
        return supplierId;
    }
    
    
    public void setItemId(String itemId) {
    this.itemId = itemId;
}
    public void setName(String name) {
    this.name = name;
}
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void setDateRequired(Date dateRequired) {
        this.dateRequired = dateRequired;
    }

    public void setSupplierId(String supplierId) {
    this.supplierId = supplierId;
}

}

