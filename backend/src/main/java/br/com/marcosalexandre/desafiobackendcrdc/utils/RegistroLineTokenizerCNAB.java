package br.com.marcosalexandre.desafiobackendcrdc.utils;

import java.util.Map;

import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;

public class RegistroLineTokenizerCNAB implements LineTokenizer {
    private final Map<String, Range[]> columnRanges;

    public RegistroLineTokenizerCNAB(Map<String, Range[]> columnRanges) {
        this.columnRanges = columnRanges;
    }

    @Override
    public FieldSet tokenize(String line) {

        String tipoRegistro = line.substring(0, 3);
        Range[] ranges = columnRanges.get(tipoRegistro);

        if (ranges == null) {
            throw new IllegalArgumentException("Intervalos de colunas não definidos para o tipo de registro: " + tipoRegistro);
        }

        DynamicLengthTokenizerCNAB dynamicLengthTokenizer = new DynamicLengthTokenizerCNAB();
        dynamicLengthTokenizer.setColumnRanges(ranges);
        return dynamicLengthTokenizer.tokenize(line);
    }

}
