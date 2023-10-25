package kg.test.paymentsystem.controller.auth;

import kg.test.paymentsystem.dtos.auth.AuthenticationRequestDto;
import kg.test.paymentsystem.dtos.auth.AuthenticationResponseDto;
import kg.test.paymentsystem.service.auth.AuthenticationService;
import kg.test.paymentsystem.dtos.auth.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kg.test.paymentsystem.common.Constants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(API+AUTH)
public class AuthenticationController {

    private final AuthenticationService service;
    /**
     * Метод для регистрации нового пользователя.
     *
     * @param request Запрос на регистрацию, содержащий данные нового пользователя.
     * @return HTTP-ответ с сообщением об успешной регистрации.
     */
    @PostMapping(REGISTER)
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody RegisterRequestDto request){
        service.register(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Метод для аутентификации пользователя.
     *
     * @param request Запрос на аутентификацию, содержащий данные пользователя для входа.
     * @return HTTP-ответ с токеном аутентификации и сообщением об успешной аутентификации.
     */
    @PostMapping(AUTHENTICATE)
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequestDto request){
        return ResponseEntity.ok(service.authenticate(request));
    }

}
