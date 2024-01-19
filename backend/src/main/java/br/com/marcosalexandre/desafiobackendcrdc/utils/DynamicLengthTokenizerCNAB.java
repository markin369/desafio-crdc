package br.com.marcosalexandre.desafiobackendcrdc.utils;

import org.springframework.batch.item.file.transform.AbstractLineTokenizer;
import org.springframework.batch.item.file.transform.Range;

import java.util.ArrayList;
import java.util.List;

public class DynamicLengthTokenizerCNAB extends AbstractLineTokenizer {

    private Range[] columnRanges;

    public void setColumnRanges(Range[] columnRanges) {
        this.columnRanges = columnRanges;
    }

    @Override
    protected List<String> doTokenize(String line) {
        List<String> tokens = new ArrayList<>();
        int start = 0;

        for (Range range : columnRanges) {
            int end = range.getMax();
            if (line.length() >= end) {
                tokens.add(line.substring(start, end));
            } else if (line.length() > start) {
                tokens.add(line.substring(start));
            } else {
                tokens.add("");
            }

            start = end;
        }

        return tokens;
    }
}
