package me.hardstyl3r.neuralnet;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {
    public static List<double[]> trainInputs = new ArrayList<>();
    public static List<double[]> trainOutputs = new ArrayList<>();
    public static List<double[]> testInputs = new ArrayList<>();
    public static List<double[]> testOutputs = new ArrayList<>();

    public static double[][] getTrainX() {
        return trainInputs.toArray(new double[0][]);
    }

    public static double[][] getTrainY() {
        return trainOutputs.toArray(new double[0][]);
    }

    public static double[][] getTestX() {
        return testInputs.toArray(new double[0][]);
    }

    public static double[][] getTestY() {
        return testOutputs.toArray(new double[0][]);
    }

    public static void loadAllFromDirectory(String directoryPath) throws Exception {
        File dir = new File(directoryPath);
        File[] files = dir.listFiles((file, name) -> name.endsWith(".xlsx"));

        if (files == null || files.length == 0) throw new RuntimeException("No .xlsx files found in: " + directoryPath);

        for (File file : files) {
            System.out.println("Reading: " + file.getName());
            loadFromFile(file, file.getName().contains("_stat_"));  // trening
        }
    }

    private static void loadFromFile(File file, boolean isTraining) throws Exception {
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            int xCol = findColumn(sheet, "data__coordinates__x");
            int yCol = findColumn(sheet, "data__coordinates__y");
            int refXCol = findColumn(sheet, "reference__x");
            int refYCol = findColumn(sheet, "reference__y");

            List<double[]> raw = new ArrayList<>();
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                try {
                    double x = row.getCell(xCol).getNumericCellValue() / 10000.0;
                    double y = row.getCell(yCol).getNumericCellValue() / 10000.0;
                    double refX = row.getCell(refXCol).getNumericCellValue() / 10000.0;
                    double refY = row.getCell(refYCol).getNumericCellValue() / 10000.0;
                    raw.add(new double[]{x, y, refX, refY});
                } catch (Exception ignored) {
                }
            }

            int window = 5;
            for (int i = 0; i <= raw.size() - window; i++) {
                double[] input = new double[window * 4];
                for (int j = 0; j < window; j++) {
                    System.arraycopy(raw.get(i + j), 0, input, j * 4, 4);
                }

                double[] last = raw.get(i + window - 1);
                double[] output = new double[]{last[2], last[3]};  // reference_x, reference_y

                if (isTraining) {
                    trainInputs.add(input);
                    trainOutputs.add(output);
                } else {
                    testInputs.add(input);
                    testOutputs.add(output);
                }
            }
        }
    }

    private static int findColumn(Sheet sheet, String headerName) {
        Row header = sheet.getRow(0);
        for (Cell cell : header) {
            if (cell.getStringCellValue().equals(headerName)) {
                return cell.getColumnIndex();
            }
        }
        throw new RuntimeException("Column not found: " + headerName);
    }
}
