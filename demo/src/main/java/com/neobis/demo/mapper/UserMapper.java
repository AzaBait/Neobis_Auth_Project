package com.neobis.demo.mapper;

import com.neobis.demo.dto.RegisterDto;
import com.neobis.demo.dto.UserDto;
import com.neobis.demo.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserDto entityToDto(User user);
    User dtoToEntity(UserDto userDto);
    List<UserDto> entitiesToDtos(List<User> users);
    User registerDtoToEntity(RegisterDto registerDto);
}
