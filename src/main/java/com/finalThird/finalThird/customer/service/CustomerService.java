package com.finalThird.finalThird.customer.service;

import com.finalThird.finalThird.auth.domain.Authority;
import com.finalThird.finalThird.common.config.security.SecurityUtil;
import com.finalThird.finalThird.common.exception.customer.CustomerAlreadyExistException;
import com.finalThird.finalThird.common.exception.customer.CustomerNotFoundException;
import com.finalThird.finalThird.common.security.Security;
import com.finalThird.finalThird.customer.controller.dto.CustomerRequest;
import com.finalThird.finalThird.customer.controller.dto.CustomerResponse;
import com.finalThird.finalThird.customer.domain.Customer;
import com.finalThird.finalThird.customer.external.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CustomerRepository customerRepository;
  private final Security security;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public CustomerResponse.JoinResponse signup(CustomerRequest.JoinRequest userDto) {
    //검증
    //있는 e-mail이면 익 처리
    if (customerRepository.findOneWithAuthoritiesByCustomerEmail(userDto.getCustomerEmail()).isPresent()) {
      throw new CustomerAlreadyExistException();
    }

    // 가입되어 있지 않은 회원이면,
    // 권한 정보 만들고
    Authority authority = Authority.builder()
        .authorityName("ROLE_USER")
        .build();

    // 유저 정보를 만들어서 save
    Customer user = createCustomer(userDto, authority);

    Customer save = customerRepository.save(user);

    return toResponse(save);
  }

  private static CustomerResponse.JoinResponse toResponse(Customer save) {
    CustomerResponse.JoinResponse res = new CustomerResponse.JoinResponse();

    res.setCustomerEmail(save.getCustomerEmail());
    res.setCustomerName(save.getCustomerEmail());
    res.setCustomerNickName(save.getCustomerEmail());
    res.setCustomerPhone(save.getCustomerEmail());
    return res;
  }

  private Customer createCustomer(CustomerRequest.JoinRequest userDto, Authority authority) {
    Customer user = Customer.builder()
        .customerEmail(userDto.getCustomerEmail())
        .password(passwordEncoder.encode(userDto.getPassword()))
        .customerName(userDto.getCustomerName())
        .nickName(userDto.getCustomerNickName())
        .customerPhone(userDto.getCustomerPhone())
        .authorities(Collections.singleton(authority))
        .activated(true)
        .build();
    return user;
  }

  // 유저,권한 정보를 관리자가 가져오는 메소드
  @Transactional(readOnly = true)
  public CustomerResponse.JoinResponse getUserWithAuthorities(String username) {
    Customer customer = customerRepository
        .findOneWithAuthoritiesByCustomerEmail(username)
        .orElseThrow(CustomerNotFoundException::new);

    return toResponse(customer);
  }

  // 현재 securityContext에 저장된 username의 정보만 가져오는 메소드
  @Transactional(readOnly = true)
  public Optional<Customer> getMyUserWithAuthorities() {
    return SecurityUtil.getCurrentUsername()
        .flatMap(customerRepository::findOneWithAuthoritiesByCustomerEmail);
  }

  public Customer getCustomer(String userEmail) {
    return customerRepository.findByCustomerEmail(userEmail).orElseThrow(CustomerNotFoundException::new);
  }

  public CustomerResponse.Information getMe() {
    Customer me = security.getMe();

    return dataTransferToResponse(me);
  }

  private static CustomerResponse.Information dataTransferToResponse(Customer me) {
    CustomerResponse.Information res = new CustomerResponse.Information();

    res.setCustomerEmail(me.getCustomerEmail());
    res.setCustomerName(me.getCustomerName());
    res.setCustomerNickName(me.getNickName());
    res.setCustomerPhone(me.getCustomerPhone());

    return res;
  }
}
