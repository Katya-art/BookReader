package ua.kpi.comsys.bookreader.paginator.extension;

import java.util.ArrayList;
import java.util.List;

public class AllWordsPositions {
    public List<Integer> allWordsPositions(CharSequence input) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ' ' || input.charAt(i) == '\n') {
                indexes.add(i);
            }
        }
        return indexes;
    }
}