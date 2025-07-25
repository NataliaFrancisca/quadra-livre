package br.com.nat.quadralivre.domain.quadra.indisponibilidade;

import br.com.nat.quadralivre.domain.quadra.QuadraService;
import br.com.nat.quadralivre.domain.quadra.funcionamento.DiaSemana;
import br.com.nat.quadralivre.domain.quadra.funcionamento.validacoes.ValidadorAcaoAtingeOutraEntidade;
import br.com.nat.quadralivre.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class IndisponibilidadeService {

    @Autowired
    private QuadraService quadraService;

    @Autowired
    private IndisponibilidadeRepository indisponibilidadeRepository;

    @Autowired
    private ValidadorAcaoAtingeOutraEntidade validadorAcaoAtingeOutraEntidade;

    public IndisponibilidadeDadosDetalhados indicarQuadraComoIndisponivelEmDataSelecionada(Long quadraId, IndisponibilidadeRegistro indisponibilidadeRegistro, Usuario gestor){
        var data = indisponibilidadeRegistro.data();
        DiaSemana diaSemana = DiaSemana.fromEnglish(data.getDayOfWeek().name());

        var quadra = this.quadraService.buscarQuadraParaGestor(quadraId, gestor);
        this.validadorAcaoAtingeOutraEntidade.validar(quadraId, diaSemana);

        Indisponibilidade indisponibilidade = new Indisponibilidade(indisponibilidadeRegistro);
        indisponibilidade.setQuadra(quadra);

        return new IndisponibilidadeDadosDetalhados(this.indisponibilidadeRepository.save(indisponibilidade));
    }

    @Scheduled(cron = "0 0 0 * * MON")
    public void removerIndisponibilidadesDoBanco(){
        var dataAtual = LocalDate.now();
        var datasPassadas = this.indisponibilidadeRepository.findAllByDataBefore(dataAtual);
        this.indisponibilidadeRepository.deleteAll(datasPassadas);
    }
}
