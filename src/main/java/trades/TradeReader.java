package trades;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TradeReader {

    private ObjectMapper objectMapper = new ObjectMapper();
    private static Set<String> alreadyReadFiles = new HashSet<>();

    public List<Trade> readTrades(String pathName) {
        List<File> newFiles = filterOutAlreadyReadFiles(pathName);

        return newFiles.stream()
                .map(file -> {
                    Trade[] tradesFromFile = null;
                    try {
                        tradesFromFile = objectMapper.readValue(file, Trade[].class);
                        alreadyReadFiles.add(file.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return tradesFromFile;
                })
                .filter(x -> x != null)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
    }

    private List<File> filterOutAlreadyReadFiles(String pathName) {
        File folder = new File(pathName);
        File[] listOfFilesInFolder = folder.listFiles();

        return Arrays.stream(listOfFilesInFolder)
                .filter(file -> !alreadyReadFiles.contains(file.getName()))
                .collect(Collectors.toList());
    }
}
