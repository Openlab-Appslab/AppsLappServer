package org.appslapp.AppsLappServer.business.pojo.users.user;

import net.bytebuddy.utility.RandomString;
import org.appslapp.AppsLappServer.business.pojo.users.entity.EntityService;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.exceptions.UnsatisfyingPasswordException;
import org.appslapp.AppsLappServer.exceptions.UserNotFoundException;
import org.appslapp.AppsLappServer.exceptions.UsernameAlreadyExistsException;
import org.appslapp.AppsLappServer.persistance.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service
public class UserService implements EntityService<User> {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JavaMailSender mailSender;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder encoder,
                       JavaMailSender sender) {
        super();
        this.userRepository = repository;
        this.encoder = encoder;
        this.mailSender = sender;
    }

    public long update(User user) {
        return userRepository.save(user).getId();
    }

    public List<String> getStudents() {
        var map = new ArrayList<String>();
        for (var user : userRepository.findAllByAuthorityAndEnabled("PUPIL", true)) {
            map.add(user.getFirstName() + " " + user.getLastName());
        }
        return map;
    }

    public long enable(User user) {
        user.setEnabled(true);
        return userRepository.save(user).getId();
    }

    public String verifyUser(String code) {
        var user = userRepository.findByVerificationCode(code)
                .orElseThrow(() -> new UserNotFoundException(code));
        user.setEnabled(true);
        update(user);
        return user.getEmail();
    }

    public long save(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent() ||
                userRepository.findByEmail(user.getEmail()).isPresent())
            throw new UsernameAlreadyExistsException("Username: " + user.getUsername() + "exists");

        if (!isPasswordValid(user.getPassword()))
            throw new UnsatisfyingPasswordException("Password doesn't meet requirements");

        if (user.getAuthority() == null)
            user.setAuthority("PUPIL");

        user.setPassword(encoder.encode(user.getPassword()));

        user.setEnabled(false);

        user.setVerificationCode(RandomString.make(64));

        try {
            sendVerificationEmail(user);
        } catch (MessagingException | UnsupportedEncodingException e) {
            user.setEnabled(true); // debug only
        }

        return userRepository.save(user).getId();

    }

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public User getUserByName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }

    public long resendEmail(String username) {
        var user = getUserByName(username);

        try {
            sendVerificationEmail(user);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return user.getId();
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

        String verifyURL = "https://apps-lapp-server.herokuapp.com" + "/api/auth/verify/" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    public Labmaster createLabmaster(String username, String password) {
        var user = getUserByName(username);
        var labmaster = new Labmaster();
        labmaster.setUsername(user.getUsername());
        labmaster.setFirstName(user.getFirstName());
        labmaster.setLastName(user.getLastName());
        labmaster.setEmail(user.getEmail());
        labmaster.setPassword(encoder.encode(password));
        userRepository.delete(user);
        return labmaster;
    }
}
