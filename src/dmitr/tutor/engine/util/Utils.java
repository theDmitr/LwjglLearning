package dmitr.tutor.engine.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Utils {

    public static String loadResource(String fileName) throws IOException {
        String result;

        try (InputStream is = Utils.class.getResourceAsStream(fileName)) {
            assert is != null;
            Scanner scanner = new Scanner(is, StandardCharsets.UTF_8);
            result = scanner.useDelimiter("\\A").next();
        }

        return result;
    }

    public static List<String> readAllLines(String fileName) throws Exception {
        List<String> result = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(Class.forName(Utils.class.getName())
                        .getResourceAsStream(fileName))))) {
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
        }
        return result;
    }

}
