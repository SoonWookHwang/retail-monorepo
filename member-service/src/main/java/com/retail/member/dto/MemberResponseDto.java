package com.retail.member.dto;

import com.retail.member.entity.Address;
import com.retail.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class MemberResponseDto {

  private Long id;
  private String email;
  private String name;
  private String city;
  private String street;
  private String zipcode;

  public static MemberResponseDto from(Member member) {
    Address address = member.getAddress();
    return MemberResponseDto.builder()
        .id(member.getId())
        .email(member.getEmail())
        .name(member.getName())
        .city(address.getCity())
        .street(address.getStreet())
        .zipcode(address.getZipcode())
        .build();
  }

}
