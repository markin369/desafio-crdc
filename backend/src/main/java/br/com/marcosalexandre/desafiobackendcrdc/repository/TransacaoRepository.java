package br.com.marcosalexandre.desafiobackendcrdc.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.marcosalexandre.desafiobackendcrdc.entity.Transacao;

public interface TransacaoRepository extends CrudRepository<Transacao, Long> {
  List<Transacao> findAllByOrderByRazaoSocialAscIdDesc();
}
