package org.appslapp.AppsLappServer.business.services;

import net.bytebuddy.utility.RandomString;
import org.appslapp.AppsLappServer.business.pojo.users.entity.EntityService;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.Labmaster;
import org.appslapp.AppsLappServer.business.pojo.users.user.User;
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

import static org.appslapp.AppsLappServer.business.Constants.EmailString.*;
import static org.appslapp.AppsLappServer.business.Constants.EmailString.SENDER_NAME;
import static org.appslapp.AppsLappServer.business.Constants.EmailString.NEW_ACCOUNT_SUBJECT;

@Service
public class UserService implements EntityService<User> {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JavaMailSender mailSender;

    private final LabmasterService labmasterRepository;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder encoder,
                       JavaMailSender sender, LabmasterService labmasterRepository) {
        super();
        this.userRepository = repository;
        this.encoder = encoder;
        this.mailSender = sender;
        this.labmasterRepository = labmasterRepository;
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

//    public long enable(User user) {
//        user.setEnabled(true);
//        return userRepository.save(user).getId();
//    }

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
            user.setEnabled(true);
        }

        return userRepository.save(user).getId();
    }

    public User getUserById(long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        return user.get();
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

    public boolean isPasswordValid(String password) {
        var numberRegex = ".*[0-9]+.*";
        var lowerCaseLettersRegex = ".*[a-z]+.*";
        var upperCaseLettersRegex = ".*[A-Z]+.*";
        var specialCharactersRegex = ".*[^a-zA-Z0-9]+.*";

        return password.matches(numberRegex) && password.matches(lowerCaseLettersRegex) &&
                password.matches(specialCharactersRegex) && password.matches(upperCaseLettersRegex) &&
                password.matches(".{8,16}");
    }

    public void sendVerificationEmail(User user) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        var content = NEW_ACCOUNT_CONTENT;

        helper.setFrom(APPSLAPP_GMAIL_COM, SENDER_NAME);
        helper.setTo(toAddress);
        helper.setSubject(NEW_ACCOUNT_SUBJECT);

        content = content.replace("[[name]]", user.getUsername());

        String verifyURL = "https://appslab-api.herokuapp.com" + "/api/auth/verify/" + user.getVerificationCode();

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
        labmasterRepository.save(labmaster);
        return labmaster;
    }

    public User getUserByFullName(String fullName) {
        var names = fullName.split(" ");
        var user = userRepository.findByFirstNameAndLastName(names[0], names[1]);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        return user.get();
    }

    public long resetPassword(String username, String password) {
        var user = getUserByName(username);
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
        return user.getId();
    }

    public long resetPasswordEmail(String username) throws MessagingException, UnsupportedEncodingException {
        var user = getUserByName(username);
        String toAddress = user.getEmail();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(APPSLAPP_GMAIL_COM, SENDER_NAME);
        helper.setTo(toAddress);
        helper.setSubject(PASSWORD_RESET_SUBJECT);

        var content = PASSWORD_RESET_CONTENT;

        content = content.replace("[[name]]", user.getUsername());

        String verifyURL = "https://appslappapp.vercel.app/reset-password/" + user.getUsername();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
        return 1L;
    }
}
