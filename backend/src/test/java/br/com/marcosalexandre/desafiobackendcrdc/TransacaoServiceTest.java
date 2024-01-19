package br.com.marcosalexandre.desafiobackendcrdc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.marcosalexandre.desafiobackendcrdc.entity.TipoTransacao;
import br.com.marcosalexandre.desafiobackendcrdc.entity.Transacao;
import br.com.marcosalexandre.desafiobackendcrdc.entity.TransacaoReport;
import br.com.marcosalexandre.desafiobackendcrdc.repository.TransacaoRepository;
import br.com.marcosalexandre.desafiobackendcrdc.service.TransacaoService;

@ExtendWith(MockitoExtension.class)
public class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService transacaoService;

    @Mock
    private TransacaoRepository transacaoRepository;

    @Test
    public void testGetTotaisTransacoesByNomeDaLoja() {
        final String lojaA = "Loja A", lojaB = "Loja B";

        // Arrange
        var transacao1 = new Transacao(
                1L,
                lojaA,
                "123456789", "",
                TipoTransacao.CREDITO.getTipo(),
                BigDecimal.valueOf(100.00),
                12345678L,
                90123456L,
                "");

        var transacao2 = new Transacao(
                2L,
                lojaB,
                "111222333", "",
                TipoTransacao.DEBITO.getTipo(),
                BigDecimal.valueOf(75.00),
                11112222L,
                33334444L,
                "");

        var transacao3 = new Transacao(
                3L,
                lojaA,
                "123456789", "",
                TipoTransacao.TRANSFERENCIA.getTipo(),
                BigDecimal.valueOf(50.00),
                11112222L,
                33334444L,
                "");

        List<Transacao> mockTransacoes = List.of(transacao1, transacao2, transacao3);

        when(transacaoRepository.findAllByOrderByRazaoSocialAscIdDesc()).thenReturn(mockTransacoes);

        // Act
        List<TransacaoReport> reports = transacaoService.getTotaisTransacoesByRazaoSocial();

        // Assert
        assertEquals(2, reports.size(), "O número de relatórios não está correto.");

        reports.forEach(report -> {
            if (report.razaoSocial().equals(lojaA)) {
                assertTransacaoReport(report, lojaA, 2, BigDecimal.valueOf(150.00), transacao1, transacao3);
            } else if (report.razaoSocial().equals(lojaB)) {
                assertTransacaoReport(report, lojaB, 1, BigDecimal.valueOf(75.00), transacao2);
            }
        });
    }

    private void assertTransacaoReport(TransacaoReport report, String expectedRazaoSocial, int expectedSize, BigDecimal expectedTotal, Transacao... expectedTransacoes) {
        assertEquals(expectedRazaoSocial, report.razaoSocial(), "Razão social incorreta.");
        assertEquals(expectedSize, report.transacoes().size(), "Número incorreto de transações.");
        assertEquals(expectedTotal, report.total(), "Total incorreto.");
        List<Transacao> transacoesEsperadas = Arrays.asList(expectedTransacoes);
        List<Transacao> transacoesReais = report.transacoes();

        for (Transacao transacaoEsperada : transacoesEsperadas) {
            assertTrue(transacoesReais.contains(transacaoEsperada), "Transação ausente: " + transacaoEsperada);
        }

        for (Transacao transacaoReal : transacoesReais) {
            assertTrue(transacoesEsperadas.contains(transacaoReal), "Transação inesperada: " + transacaoReal);
        }
    }
}
