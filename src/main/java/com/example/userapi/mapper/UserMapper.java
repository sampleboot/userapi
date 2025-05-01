package com.example.userapi.mapper;

import com.example.userapi.dto.UserDTO;
import com.example.userapi.dto.AddressDTO;
import com.example.userapi.entity.User;
import com.example.userapi.entity.Address;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUserId(user.getUserId());
        dto.setLastName(user.getLastName());
        dto.setFirstName(user.getFirstName());
        dto.setEmailAddress(user.getEmailAddress());
        dto.setSupervisorUserId(user.getSupervisorUserId());
        dto.setTitleText(user.getTitleText());
        dto.setCreateUserId(user.getCreateUserId());
        dto.setCreateDttm(user.getCreateDttm());
        dto.setUpdateUserId(user.getUpdateUserId());
        dto.setUpdateDttm(user.getUpdateDttm());

       if (user.getAddresses() != null) {
            dto.setAddresses(user.getAddresses().stream().map(UserMapper::toAddressDTO).collect(Collectors.toList()));
        }

        return dto;
    }

    public static AddressDTO toAddressDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setLocation(address.getLocation());
        dto.setMobilePhone(address.getMobilePhone());
        dto.setOtherPhone(address.getOtherPhone());
        dto.setStreetAddress(address.getStreetAddress());
        dto.setStreetAddress2(address.getStreetAddress2());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountryCode(address.getCountryCode());
        return dto;
    }
}
