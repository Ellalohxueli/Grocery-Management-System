package Assessment;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Sale {
   private String salesFileName;

    public Sale(String salesFileName) {
        this.salesFileName = salesFileName;
    }

    public void recordSale(int SalesId,String itemCode, int quantitySold, double itemPrice) {
        try (BufferedWriter salesWriter = new BufferedWriter(new FileWriter(salesFileName, true))) {
            double totalSales = quantitySold * itemPrice;

            salesWriter.write(SalesId+" "+itemCode + " " + quantitySold + " " + totalSales);
            salesWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

