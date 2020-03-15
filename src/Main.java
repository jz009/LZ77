import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("src/test.txt"));
        String line = "";
        StringBuilder s = new StringBuilder();
        while ((line = br.readLine()) != null)
        {
            s.append(line);
        }
        LZ77 encoder = new LZ77(s.toString());
        encoder.runTest();
    }
}
