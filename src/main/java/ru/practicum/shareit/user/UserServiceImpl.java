package ru.practicum.shareit.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto postuser(UserDto userDto) {
        return UserMapper.mapUserToDto(userRepository.save(UserMapper.mapDtoToUser(userDto)));
    }

    @Transactional
    @Override
    public UserDto patchUser(UserDto userDto, Long userId) {
        User user = UserMapper.mapDtoToUser(userDto);
        User userFromMemory = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет"));
        if (user.getName() != null) {
            userFromMemory.setName(user.getName());
        }
        if (user.getEmail() != null) {
            userFromMemory.setEmail(user.getEmail());
        }
        return UserMapper.mapUserToDto(userRepository.save(userFromMemory));
    }

    @Override
    public UserDto getUser(long userId) {
        return UserMapper.mapUserToDto(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет")));
    }

    @Transactional
    @Override
    public void deleteUser(long userId) {
        userRepository.delete(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с таким id нет")));

    }
}
