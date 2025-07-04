package br.com.nat.quadralivre.infra.expection;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class TratamentoDeErros {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException exception){

        var erros = exception.getFieldErrors().stream().map(DadosErroValidacao::new).toList();
        DadoErrosValidacao dados = new DadoErrosValidacao(erros);

        return ResponseEntity.badRequest().body(dados);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity tratarErroDeExecucao(RuntimeException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha na execução. " + exception.getLocalizedMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity tratarErroBadCredentials(BadCredentialsException expection) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas. " + expection.getLocalizedMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity tratarErroAuthentication() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity tratarErroAcessoNegado(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado: " + ex.getLocalizedMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity tratarErro500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " +ex.getLocalizedMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity tratarErroDadosDuplicados(DataIntegrityViolationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro: " + ex.getLocalizedMessage() );
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity tratarElementoNaoEncontrado(NoSuchElementException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + ex.getLocalizedMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity tratarUsuarioNaoEncontrado(UsernameNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erro: " + ex.getLocalizedMessage());
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity tratarErrosNoTokenJWT(JWTVerificationException exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Erro na verificação do token JWT. " + exception.getLocalizedMessage());
    }

    private record DadoErrosValidacao(List<DadosErroValidacao> erros){ }

    private record DadosErroValidacao(String campo, String mensagem){
        public DadosErroValidacao(FieldError erro){
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}
