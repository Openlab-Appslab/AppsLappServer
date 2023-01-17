package org.appslapp.AppsLappServer.business.services;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;
import org.appslapp.AppsLappServer.exceptions.UnsatisfyingPasswordException;
import org.appslapp.AppsLappServer.exceptions.UserNotFoundException;
import org.appslapp.AppsLappServer.persistance.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

@ExtendWith(MockitoExtension.class)

class UserServiceTest {
    @Mock
    private JavaMailSender mailSender;

    @Mock
    private LabmasterService labmasterRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService example;

    @Test
    public void testGetStudents() {
        User user1 = createUser1();
        User user2 = createUser2();

        when(userRepository.findAllByAuthorityAndEnabled("PUPIL", true))
                .thenReturn(Arrays.asList(
                        user1,
                        user2
                ));

        List<String> students = example.getStudents();

        assertEquals(Arrays.asList("John Doe", "Jane Doe"), students);
    }

    @Test
    public void testVerifyUser() {
        User user = createUser1();

        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByVerificationCode("abc123")).thenReturn(Optional.of(user));

        String email = example.verifyUser("abc123");

        assertEquals("john.doe@example.com", email);
        assertTrue(user.isEnabled());
        verify(userRepository).save(user);
    }

    @Test
    public void testSave_whenPasswordIsInvalid() {
        User user = createUser1();
        user.setUsername("johndoe");
        user.setEmail("john.doe@example.com");
        user.setPassword("pass");
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());

        assertThrows(UnsatisfyingPasswordException.class, () -> example.save(user));
    }

    @Test
    public void testSave_whenSuccessful() throws MessagingException, UnsupportedEncodingException {
        var mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(mimeMessage);
        User user = createUser1();
        user.setUsername("johndoe");
        user.setEmail("john.doe@example.com");
        user.setPassword("Heslo123_");
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("john.doe@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);
        when(encoder.encode("Heslo123_")).thenReturn("encodedPassword");

        long id = example.save(user);

        assertEquals(1, id);
        assertFalse(user.isEnabled());
        assertNotNull(user.getVerificationCode());
        verify(mailSender).send(mimeMessage);
    }

    @Test
    public void testGetUserByFullName_whenUserFound() {
        User user = createUser2();
        when(userRepository.findByFirstNameAndLastName("Jane", "Doe")).thenReturn(Optional.of(user));

        User returnedUser = example.getUserByFullName("Jane Doe");

        assertEquals(user, returnedUser);
    }

    @Test
    public void testGetUserByFullName_whenUserNotFound() {
        when(userRepository.findByFirstNameAndLastName("John", "Doe")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> example.getUserByFullName("John Doe"));
    }

    @Test
    public void testResetPassword() {
        User user = createUser1();
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
        when(encoder.encode("newpassword")).thenReturn("encodedpassword");

        long id = example.resetPassword("johndoe", "newpassword");

        assertEquals(1, id);
        assertEquals("encodedpassword", user.getPassword());
        verify(userRepository).save(user);
    }

    @Test
    public void testResetPasswordEmail() throws MessagingException, UnsupportedEncodingException {
        var mimeMessage = new MimeMessage((Session) null);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(mimeMessage);
        User user = createUser1();
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        long id = example.resetPasswordEmail("johndoe");

        assertEquals(1, id);
        verify(mailSender).send(any(MimeMessage.class));
    }

    @Test
    public void testCreateLabmaster() {
        User user = createUser1();
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));
        when(encoder.encode("password")).thenReturn("encodedpassword");
        doNothing().when(userRepository).delete(user);
        Labmaster labmaster = new Labmaster();
        labmaster.setUsername(user.getUsername());
        labmaster.setFirstName(user.getFirstName());
        labmaster.setLastName(user.getLastName());
        labmaster.setEmail(user.getEmail());
        labmaster.setPassword(encoder.encode(user.getPassword()));

        // create when case for labmaster service with any labmaster
        when(labmasterRepository.save(any(Labmaster.class))).thenReturn(1L);

        Labmaster returnedLabmaster = example.createLabmaster("johndoe", "password");

        assertEquals(labmaster.getEmail(), returnedLabmaster.getEmail());
        assertEquals(labmaster.getFirstName(), returnedLabmaster.getFirstName());
        assertEquals(labmaster.getLastName(), returnedLabmaster.getLastName());
        assertEquals(labmaster.getUsername(), returnedLabmaster.getUsername());
        assertEquals(labmaster.getPassword(), returnedLabmaster.getPassword());
        verify(userRepository).delete(user);
        verify(labmasterRepository).save(any(Labmaster.class));
    }

    @Test
    public void testGetUserById_whenUserFound() {
        User user = createUser1();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User returnedUser = example.getUserById(1);

        assertEquals(user, returnedUser);
    }

    @Test
    public void testGetUserById_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> example.getUserById(1));
    }


    private User createUser1() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setVerificationCode("abc123");
        user.setAuthority("PUPIL");
        user.setEnabled(false);
        user.setId(1L);
        user.setEmail("john.doe@example.com");
        user.setUsername("johndoe");
        user.setPassword("password");
        return user;
    }

    private User createUser2() {
        User user = new User();
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEnabled(true);
        return user;
    }
}