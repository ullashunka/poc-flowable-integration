package asia.circles.service.metrics;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArrayProcessingService {
    private final ArrayProcessingMetrics metrics;

    public ArrayProcessingService(ArrayProcessingMetrics metrics) {
        this.metrics = metrics;
    }

    public List<Object> calculateColumnWidths(Object[][] arrayOfArray) {
        long startTime = System.currentTimeMillis();
        metrics.setBatchSize(arrayOfArray.length);
        
        int stringConversions = 0;
        List<Object> result = new ArrayList<>();
        
        try {
            // Get the number of columns from the first row
            final int numColumns = arrayOfArray[1] != null ? arrayOfArray[1].length : 0;
            int totalElements = 0;
            
            // Loop through each column
            for (int i = 0; i < numColumns; i++) {
                int maxWidth = 0;
                
                // Loop through each row to find the maximum width for this column
                for (int j = 0; j < arrayOfArray.length; j++) {
                    Object[] currentRow = arrayOfArray[j];
                    totalElements++;
                    
                    if (currentRow != null && i < currentRow.length && currentRow[i] != null) {
                        stringConversions++;
                        int currentCellLength = currentRow[i].toString().length();
                        
                        if (currentCellLength > maxWidth) {
                            maxWidth = currentCellLength;
                        }
                    }
                }
                
                // Add the result for this column
                result.add(new ColumnWidth(maxWidth));
            }
            
            metrics.recordArrayProcessed();
            metrics.recordElementsProcessed(totalElements);
            metrics.recordStringConversions(stringConversions);
            
        } catch (Exception e) {
            metrics.recordError();
            throw e;
        } finally {
            long processingTime = System.currentTimeMillis() - startTime;
            metrics.setLastProcessingTime(processingTime);
        }
        
        return result;
    }
    
    // Simple class to represent column width
    public static class ColumnWidth {
        private final int wch;
        
        public ColumnWidth(int wch) {
            this.wch = wch;
        }
        
        public int getWch() {
            return wch;
        }
    }
}