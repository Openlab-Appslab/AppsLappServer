package org.appslapp.AppsLappServer.business.security.user;

import net.bytebuddy.utility.RandomString;
import org.appslapp.AppsLappServer.persistance.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JavaMailSender mailSender;

    public UserService(@Autowired UserRepository repository, @Autowired PasswordEncoder encoder, @Autowired JavaMailSender sender) {
        this.userRepository = repository;
        this.encoder = encoder;
        this.mailSender = sender;
    }

    public long update(User user) {
        return userRepository.save(user).getId();
    }

    public long enable(User user) {
        user.setEnabled(true);
        return userRepository.save(user).getId();
    }

    public Optional<User> findByCode(String code) {
        return userRepository.findByVerificationCode(code);
    }

    public long save(User user) {
        //Check if username or email already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent() || userRepository.findByEmail(user.getEmail()).isPresent())
            return -2;

        //Check for user validity
        if (!isPasswordValid(user.getPassword()))
            return -1;

        //Set default authority if none was set before
        if (user.getAuthority() == null)
            user.setAuthority("PUPIL");

        //Encrypts password
        user.setPassword(encoder.encode(user.getPassword()));

        //Disable account
        user.setEnabled(false);

        //Generate verification code
        user.setVerificationCode(RandomString.make(64));

        //Save user so id can be generated
        var id = userRepository.save(user).getId();

        //Send email
        try {
            sendVerificationEmail(user);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return id;
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public long resendEmail(String username) {
        var user = getUserByName(username);
        if (user.isEmpty())
            return -1;
        try {
            sendVerificationEmail(user.get());
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return user.get().getId();
    }

    private boolean isPasswordValid(String password) {
        var numberRegex = ".*[0-9]+.*";
        var lowerCaseLettersRegex = ".*[a-z]+.*";
        var upperCaseLettersRegex = ".*[A-Z]+.*";
        var specialCharactersRegex = ".*[^a-zA-Z0-9]+.*";

        return password.matches(numberRegex) && password.matches(lowerCaseLettersRegex) &&
                password.matches(specialCharactersRegex) && password.matches(upperCaseLettersRegex) &&
                password.matches(".{8,16}");
    }

    private void sendVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "appslappmanagement@gmail.com";
        String senderName = "AppsLapp";
        String subject = "Potvrdte registraciu";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "AppsLapp.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());

        //https://apps-lapp-server.herokuapp.com/ for deploy http://localhost:8080/ for testing
        String verifyURL = "https://apps-lapp-server.herokuapp.com/" + "/api/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }
}
