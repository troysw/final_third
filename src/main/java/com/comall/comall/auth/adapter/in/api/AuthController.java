package com.comall.comall.auth.adapter.in.api;

import com.comall.comall.common.jwt.dto.TokenDto;
import com.comall.comall.common.jwt.JwtFilter;
import com.comall.comall.common.jwt.TokenProvider;
import com.comall.comall.auth.application.service.dto.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
  private final TokenProvider tokenProvider;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  @PostMapping("/login")
  public ResponseEntity<TokenDto> loginCustomer(@Valid @RequestBody LoginRequest loginRequest) {
      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

      // authenticate 메소드가 실행이 될 때 CustomUserDetailsService class의 loadUserByUsername 메소드가 실행
      Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
      // 해당 객체를 SecurityContextHolder에 저장하고
      SecurityContextHolder.getContext().setAuthentication(authentication);
      // authentication 객체를 createToken 메소드를 통해서 JWT Token을 생성
      String jwt = tokenProvider.createToken(authentication);

      HttpHeaders httpHeaders = new HttpHeaders();
      // response header에 jwt token에 넣어줌
      httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

      // tokenDto를 이용해 response body에도 넣어서 리턴
      return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
  }
