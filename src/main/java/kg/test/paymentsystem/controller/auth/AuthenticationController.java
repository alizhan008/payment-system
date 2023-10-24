package kg.test.paymentsystem.controller.auth;

import kg.test.paymentsystem.dtos.auth.AuthenticationRequestDto;
import kg.test.paymentsystem.dtos.auth.AuthenticationResponseDto;
import kg.test.paymentsystem.service.auth.AuthenticationService;
import kg.test.paymentsystem.dtos.auth.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody RegisterRequestDto request){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequestDto request){
        return ResponseEntity.ok(service.authenticate(request));
    }

}
