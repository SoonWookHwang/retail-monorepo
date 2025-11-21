package com.retail.member.service;

import com.retail.common.exception.CustomException;
import com.retail.common.exception.ErrorCode;
import com.retail.member.dto.SignupRequest;
import com.retail.member.entity.Address;
import com.retail.member.entity.Member;
import com.retail.member.entity.Role;
import com.retail.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  public Member signup(SignupRequest request) {
    if (memberRepository.existsByEmail(request.getEmail())) {
      throw new CustomException(ErrorCode.INVALID_REQUEST);
    }

    Member member = Member.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .name(request.getName())
        .address(Address.builder()
            .city(request.getCity())
            .street(request.getStreet())
            .zipcode(request.getZipcode())
            .build())
        .role(Role.ROLE_USER)
        .build();

    return memberRepository.save(member);
  }
}
