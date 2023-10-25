package kg.test.paymentsystem.service.auth;

import kg.test.paymentsystem.dtos.auth.AuthenticationRequestDto;
import kg.test.paymentsystem.dtos.auth.AuthenticationResponseDto;
import kg.test.paymentsystem.dtos.auth.RegisterRequestDto;
import kg.test.paymentsystem.entity.user.RoleEntity;
import kg.test.paymentsystem.entity.user.UserEntity;
import kg.test.paymentsystem.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public void register(RegisterRequestDto request) throws RuntimeException{
        var usr = repository.findByEmail(request.getEmail());

        if (usr.isPresent()){
            throw new RuntimeException("Пользователь с таким емайл существует!");
        }
        if (request.getEmail().isBlank() || request.getPassword().isBlank()){
            throw new RuntimeException("Емайл или пароль не должны быть пустыми!");
        }

        var user = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleEntity.USER)
                .build();
        repository.save(user);
    }

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponseDto.builder()
                .token(jwtToken)
                .build();
    }

}
