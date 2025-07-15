package com.hftamayo.java.todo.services.impl;

import com.hftamayo.java.todo.dto.pagination.PageRequestDto;
import com.hftamayo.java.todo.dto.user.UserResponseDto;
import com.hftamayo.java.todo.entity.User;
import com.hftamayo.java.todo.entity.Roles;
import com.hftamayo.java.todo.entity.ERole;
import com.hftamayo.java.todo.exceptions.ResourceNotFoundException;
import com.hftamayo.java.todo.exceptions.DuplicateResourceException;
import com.hftamayo.java.todo.mapper.UserMapper;
import com.hftamayo.java.todo.repository.UserRepository;
import com.hftamayo.java.todo.repository.RolesRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RolesRepository rolesRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUsers_ShouldReturnListOfUsers() {
        List<User> usersList = List.of(
                createUser(1L, "John Doe"),
                createUser(2L, "Jane Doe")
        );
        List<UserResponseDto> responseDtos = List.of(
                createUserDto(1L, "John Doe"),
                createUserDto(2L, "Jane Doe")
        );

        when(userRepository.findAll()).thenReturn(usersList);
        when(userMapper.toUserResponseDto(usersList.get(0))).thenReturn(responseDtos.get(0));
        when(userMapper.toUserResponseDto(usersList.get(1))).thenReturn(responseDtos.get(1));

        List<UserResponseDto> result = userService.getUsers().getDataList();

        assertEquals(2, result.size());
        assertEquals(responseDtos, result);
        verify(userRepository).findAll();
        verify(userMapper, times(2)).toUserResponseDto(any(User.class));
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        Long userId = 1L;
        User user = createUser(userId, "John Doe");
        UserResponseDto responseDto = createUserDto(userId, "John Doe");

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(user));
        when(userMapper.userToDto(user)).thenReturn(responseDto);

        UserResponseDto result = userService.getUser(userId).getData();

        assertEquals(responseDto, result);
        verify(userRepository).findUserById(userId);
        verify(userMapper).toUserResponseDto(user);
    }

    @Test
    void getUserById_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        Long userId = 1L;

        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUser(userId));
        
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        verify(userRepository).findUserById(userId);
        verifyNoInteractions(userMapper);
    }

    @Test
    void saveUser_WhenUserDoesNotExist_ShouldReturnSavedUser() {
        User newUser = createUser(null, "John Doe");
        newUser.setEmail("john.doe@example.com");
        newUser.setPassword("password123");
        
        User savedUser = createUser(1L, "John Doe");
        savedUser.setEmail("john.doe@example.com");
        savedUser.setPassword("encodedPassword");
        
        UserResponseDto responseDto = createUserDto(1L, "John Doe");
        Roles defaultRole = new Roles();
        defaultRole.setRoleEnum(ERole.ROLE_USER);

        when(userRepository.findUserByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(rolesRepository.findByRoleEnum(ERole.ROLE_USER)).thenReturn(Optional.of(defaultRole));
        when(passwordEncoder.encode(newUser.getPassword().trim())).thenReturn("encodedPassword");
        when(userRepository.save(newUser)).thenReturn(savedUser);
        when(userMapper.userToDto(savedUser)).thenReturn(responseDto);

        UserResponseDto result = userService.saveUser(newUser).getData();

        assertEquals(responseDto, result);
        verify(userRepository).findUserByEmail(newUser.getEmail());
        verify(rolesRepository).findByRoleEnum(ERole.ROLE_USER);
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(newUser);
        verify(userMapper).toUserResponseDto(savedUser);
    }

    @Test
    void saveUser_WhenUserAlreadyExists_ShouldThrowResourceAlreadyExistsException() {
        User existingUser = createUser(1L, "John Doe");
        existingUser.setEmail("john.doe@example.com");

        when(userRepository.findUserByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class,
                () -> userService.saveUser(existingUser));
        
        assertEquals("Resource with email '" + existingUser.getEmail() + "' already exists", exception.getMessage());
        verify(userRepository).findUserByEmail(existingUser.getEmail());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(rolesRepository, passwordEncoder, userMapper);
    }

    @Test
    void updateUser_WhenUserExists_ShouldReturnUpdatedUser() {
        Long userId = 1L;
        User existingUser = createUser(userId, "John Doe");
        User updatedUser = createUser(userId, "John Smith");
        UserResponseDto responseDto = createUserDto(userId, "John Smith");

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toUserResponseDto(updatedUser)).thenReturn(responseDto);

        UserResponseDto result = userService.updateUser(userId, updatedUser).getData();

        assertEquals(responseDto, result);
        verify(userRepository).findUserById(userId);
        verify(userRepository).save(any(User.class));
        verify(userMapper).toUserResponseDto(updatedUser);
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        Long userId = 1L;
        User updatedUser = createUser(userId, "John Smith");

        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(userId, updatedUser));
        
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        verify(userRepository).findUserById(userId);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        Long userId = 1L;
        User existingUser = createUser(userId, "John Doe");

        when(userRepository.findUserById(userId)).thenReturn(Optional.of(existingUser));

        userService.deleteUser(userId);

        verify(userRepository).findUserById(userId);
        verify(userRepository).deleteUserById(userId);
    }

    @Test
    void deleteUser_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException() {
        Long userId = 1L;

        when(userRepository.findUserById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(userId));
        
        assertEquals("User with id " + userId + " not found", exception.getMessage());
        verify(userRepository).findUserById(userId);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getPaginatedUsers_WhenUsersExist_ShouldReturnPaginatedUsers() {
        List<User> usersList = List.of(
                createUser(1L, "John Doe"),
                createUser(2L, "Jane Doe")
        );
        List<UserResponseDto> responseDtos = List.of(
                createUserDto(1L, "John Doe"),
                createUserDto(2L, "Jane Doe")
        );
        Page<User> usersPage = new PageImpl<>(usersList, PageRequest.of(0, 2), 2);

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(usersPage);
        when(userMapper.toUserResponseDto(usersList.get(0))).thenReturn(responseDtos.get(0));
        when(userMapper.toUserResponseDto(usersList.get(1))).thenReturn(responseDtos.get(1));

        PageRequestDto pageRequestDto = new PageRequestDto(0, 2, null);

        var result = userService.getPaginatedUsers(pageRequestDto);

        assertEquals(2, result.getContent().size());
        assertEquals(responseDtos, result.getContent());
        assertNotNull(result.getPagination());
        assertEquals(1, result.getPagination().getCurrentPage());
        assertEquals(2, result.getPagination().getLimit());
        assertEquals(2, result.getPagination().getTotalCount());
        assertEquals(1, result.getPagination().getTotalPages());
        assertTrue(result.getPagination().isLastPage());
        verify(userRepository).findAll(any(PageRequest.class));
        verify(userMapper, times(2)).toUserResponseDto(any(User.class));
    }

    @Test
    void getPaginatedUsers_WhenMultiplePages_ShouldReturnFirstPageWithCorrectMetadata() {
        // Arrange
        List<User> firstPageUsers = List.of(
                createUser(1L, "John Doe"),
                createUser(2L, "Jane Doe")
        );
        List<UserResponseDto> firstPageDtos = List.of(
                createUserDto(1L, "John Doe"),
                createUserDto(2L, "Jane Doe")
        );

        // Creamos una página con 2 elementos, pero indicamos que hay 5 elementos en total
        // Esto significa que habrá 3 páginas en total (5 elementos / 2 por página = 3 páginas)
        Page<User> userPage = new PageImpl<>(firstPageUsers, PageRequest.of(0, 2), 5);

        // Configuramos los mocks
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);
        when(userMapper.toUserResponseDto(firstPageUsers.get(0))).thenReturn(firstPageDtos.get(0));
        when(userMapper.toUserResponseDto(firstPageUsers.get(1))).thenReturn(firstPageDtos.get(1));

        PageRequestDto pageRequestDto = new PageRequestDto(0, 2, null);

        // Act
        var result = userService.getPaginatedUsers(pageRequestDto);

        // Assert
        assertEquals(2, result.getContent().size());
        assertEquals(1, result.getPagination().getCurrentPage()); // Primera página (índice 0)
        assertEquals(2, result.getPagination().getLimit()); // 2 elementos por página
        assertEquals(5, result.getPagination().getTotalCount()); // 5 elementos en total
        assertEquals(3, result.getPagination().getTotalPages()); // 3 páginas en total
        assertFalse(result.getPagination().isLastPage()); // No es la última página
        assertEquals(firstPageDtos, result.getContent());

        verify(userRepository).findAll(any(PageRequest.class));
        verify(userMapper, times(2)).toUserResponseDto(any(User.class));
    }

    @Test
    void getPaginatedUsers_WhenOnLastPage_ShouldIndicateIsLastPage() {
        // Arrange
        List<User> lastPageUsers = List.of(
                createUser(1L, "John Doe")
        );
        List<UserResponseDto> lastPageDtos = List.of(
                createUserDto(1L, "John Doe")
        );

        // Creamos la última página (índice 2) con 1 elemento, de un total de 5 elementos
        Page<User> usersPage = new PageImpl<>(lastPageUsers, PageRequest.of(2, 2), 5);

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(usersPage);
        when(userMapper.toUserResponseDto(lastPageUsers.get(0))).thenReturn(lastPageDtos.get(0));

        PageRequestDto pageRequestDto = new PageRequestDto(2, 2, null);

        // Act
        var result = userService.getPaginatedUsers(pageRequestDto);

        // Assert
        assertEquals(1, result.getContent().size()); // Solo 1 elemento en la última página
        assertEquals(3, result.getPagination().getCurrentPage()); // Tercera página (índice 2)
        assertEquals(2, result.getPagination().getLimit());
        assertEquals(5, result.getPagination().getTotalCount());
        assertEquals(3, result.getPagination().getTotalPages());
        assertTrue(result.getPagination().isLastPage()); // Es la última página

        verify(userRepository).findAll(any(PageRequest.class));
        verify(userMapper).toUserResponseDto(any(User.class));
    }

    @Test
    void getPaginatedUser_WhenNoUsersExist_ShouldReturnEmptyPage() {
        List<User> usersList = List.of();
        Page<User> userPage = new PageImpl<>(usersList, PageRequest.of(0, 2), 0);

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);

        PageRequestDto pageRequestDto = new PageRequestDto(0, 2, null);

        var result = userService.getPaginatedUsers(pageRequestDto);

        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getPagination().getTotalCount());
        assertEquals(0, result.getPagination().getTotalPages());
        assertTrue(result.getPagination().isLastPage());
        verify(userRepository).findAll(any(PageRequest.class));
        verifyNoInteractions(userMapper);
    }

    //helper methods
    private User createUser(Long id, String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setStatus(true);
        return user;
    }

    private UserResponseDto createUserDto(Long id, String name) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(id);
        dto.setName(name);
        dto.setStatus(true);
        return dto;
    }
}