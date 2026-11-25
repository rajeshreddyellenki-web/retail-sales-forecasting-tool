import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Reads a CSV (month,product,units) into a time series per product. */
public class SalesDataLoader {

    /** Returns product name -> units sold per month, in file order. */
    public static Map<String, double[]> load(Path csv) throws IOException {
        Map<String, List<Double>> grouped = new LinkedHashMap<>();
        List<String> lines = Files.readAllLines(csv);

        for (int i = 1; i < lines.size(); i++) { // skip header
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split(",");
            if (parts.length < 3) {
                System.err.println("Skipping bad line " + (i + 1) + ": " + line);
                continue;
            }
            try {
                double units = Double.parseDouble(parts[2].trim());
                grouped.computeIfAbsent(parts[1].trim(), k -> new ArrayList<>()).add(units);
            } catch (NumberFormatException e) {
                System.err.println("Skipping bad number on line " + (i + 1) + ": " + line);
            }
        }

        Map<String, double[]> result = new LinkedHashMap<>();
        grouped.forEach((product, values) ->
                result.put(product, values.stream().mapToDouble(Double::doubleValue).toArray()));
        return result;
    }
}
