package org.appslapp.AppsLappServer.business.pojo.users.user;

import org.appslapp.AppsLappServer.persistance.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService underTest;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder encoder;
    @Mock private JavaMailSender sender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new UserService(userRepository, encoder, sender);
    }

    @Test
    void shouldSave() {
        var user = new User();
        user.setUsername("Kubino");
        user.setPassword("Heslo123.");
        user.setEmail("kapitulcinjakub1@gmail.com");

        underTest.save(user);

        User capture = ArgumentCaptor.forClass(User.class).capture();
        verify(userRepository).save(capture);
        assertThat(user).isEqualTo(capture);
    }

    @Test
    void wrongEmailSave() {

    }

    @Test
    void wrongPasswordSave() {

    }

    @Test
    void alreadyExistsSave() {

    }
}