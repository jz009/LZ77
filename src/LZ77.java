import java.util.ArrayList;
@SuppressWarnings("unchecked")
public class LZ77 {
    private String input;
    private int divide;
    private ArrayList<Triple> list;
    private int sbMax;

    public LZ77(String in) {
        input = in;
        divide = 0;
        sbMax = 2048;
        list = new ArrayList<Triple>();
    }

    public void compress() {
        char[] str = input.toCharArray();
        int i = 0;
        while(divide < str.length) {
            list.add(findBestMatch(divide, str));
            divide += list.get(i).length + 1;
            i++;
        }
    }

    public String decompress() {
        ArrayList chars = new ArrayList();
        int pos = 0;
        for (int i = 0; i < list.size(); i++) {
            Triple cur = list.get(i);
            if (cur.offset == 0) {
                chars.add(cur.symbol);
                pos++;
            }
            else {
                for (int j = 0; j < cur.length; j++) {
                    chars.add(chars.get(pos - cur.offset + j));
                }
                pos += cur.length + 1;
                chars.add(cur.symbol);
            }
        }
        StringBuilder s = new StringBuilder();
        for (Object c : chars) {
            s.append(c);
        }
        return s.toString();
    }

    private Triple findBestMatch(int divide,char[] stream) {
        int longMatch = 0;
        int matchIndex = -1;
        int start;
        if (divide - sbMax > - 1) {
            start = divide - sbMax;
        }
        else start = 0;
        for (int i = start; i < divide; i++) {
            int tempLong = 0;
            int tempIndex = -1;
            int j = 0;
            while(divide + j < stream.length && j < 8 && stream[i + j] == stream[divide + j]) {
                tempLong = j + 1;
                tempIndex = i;
                j++;
            }
            if (tempLong > longMatch) {
                longMatch = tempLong;
                matchIndex = tempIndex;
            }
        }
        if (matchIndex == -1) {
            return new Triple(0, 0, stream[divide + longMatch]);
        }
        else if (divide + longMatch == stream.length) {
            return new Triple(divide - matchIndex, longMatch, '\u0000');
        }
        else
            return new Triple(divide - matchIndex, longMatch, stream[divide + longMatch]);
    }

    public void runTest() {
        this.compress();
        String output = this.decompress();
        int compressedBits = list.size() * 16;
        int origChars = input.length();
        System.out.println(input);
        System.out.println(output);
        System.out.println("Bits per character: " + compressedBits / origChars);
    }

    private class Triple {
        int offset;
        int length;
        char symbol;

        public Triple(int o, int l, char c) {
            offset = o;
            length = l;
            symbol = c;
        }
    }
}
