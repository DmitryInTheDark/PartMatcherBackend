package ru.app.partmatcher.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.app.partmatcher.dto.AuthResponseDto;
import ru.app.partmatcher.dto.AuthRequestDto;
import ru.app.partmatcher.dto.UserDto;
import ru.app.partmatcher.dto.UserRegistrationDto;
import ru.app.partmatcher.entity.Role;
import ru.app.partmatcher.entity.User;
import ru.app.partmatcher.exception.BusinessException;
import ru.app.partmatcher.mapper.UserMapper;
import ru.app.partmatcher.repository.UserRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public AuthResponseDto register(UserRegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new BusinessException("Пользователь с таким email уже зарегистрирован");
        }

        User user = User.builder()
                .name(registrationDto.getName())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .roles(Set.of(Role.USER))
                .build();
        userRepository.save(user);

        return buildResponse(user);
    }

    public AuthResponseDto authenticate(AuthRequestDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw new BusinessException("Неверные учетные данные");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Пользователь не найден"));

        return buildResponse(user);
    }

    private AuthResponseDto buildResponse(User user) {
        String token = jwtService.generateToken(user.getEmail());
        UserDto userDto = UserMapper.toDto(user);

        return AuthResponseDto.builder()
                .accessToken(token)
                .user(userDto)
                .build();
    }
}
