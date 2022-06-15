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
        String subject = "Registracia";
        String content = "<!DOCTYPE html>\n" +
                "\n" +
                "<html lang=\"en\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:v=\"urn:schemas-microsoft-com:vml\">\n" +
                "\n" +
                "<head>\n" +
                "\t<title></title>\n" +
                "\t<meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\" />\n" +
                "\t<meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\" />\n" +
                "\t<!--[if mso]><xml><o:OfficeDocumentSettings><o:PixelsPerInch>96</o:PixelsPerInch><o:AllowPNG/></o:OfficeDocumentSettings></xml><![endif]-->\n" +
                "\t<style>\n" +
                "\t\t* {\n" +
                "\t\t\tbox-sizing: border-box;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\tbody {\n" +
                "\t\t\tmargin: 0;\n" +
                "\t\t\tpadding: 0;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ta[x-apple-data-detectors] {\n" +
                "\t\t\tcolor: inherit !important;\n" +
                "\t\t\ttext-decoration: inherit !important;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t#MessageViewBody a {\n" +
                "\t\t\tcolor: inherit;\n" +
                "\t\t\ttext-decoration: none;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\tp {\n" +
                "\t\t\tline-height: inherit\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.desktop_hide,\n" +
                "\t\t.desktop_hide table {\n" +
                "\t\t\tmso-hide: all;\n" +
                "\t\t\tdisplay: none;\n" +
                "\t\t\tmax-height: 0px;\n" +
                "\t\t\toverflow: hidden;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t.menu_block.desktop_hide .menu-links span {\n" +
                "\t\t\tmso-hide: all;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t@media (max-width:700px) {\n" +
                "\t\t\t.desktop_hide table.icons-inner {\n" +
                "\t\t\t\tdisplay: inline-block !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.icons-inner {\n" +
                "\t\t\t\ttext-align: center;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.icons-inner td {\n" +
                "\t\t\t\tmargin: 0 auto;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.fullMobileWidth,\n" +
                "\t\t\t.row-content {\n" +
                "\t\t\t\twidth: 100% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.image_block img.big {\n" +
                "\t\t\t\twidth: auto !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.menu-checkbox[type=checkbox]~.menu-links {\n" +
                "\t\t\t\tdisplay: none !important;\n" +
                "\t\t\t\tpadding: 5px 0;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.menu-checkbox[type=checkbox]:checked~.menu-trigger .menu-open {\n" +
                "\t\t\t\tdisplay: none !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.menu-checkbox[type=checkbox]:checked~.menu-links,\n" +
                "\t\t\t.menu-checkbox[type=checkbox]~.menu-trigger {\n" +
                "\t\t\t\tdisplay: block !important;\n" +
                "\t\t\t\tmax-width: none !important;\n" +
                "\t\t\t\tmax-height: none !important;\n" +
                "\t\t\t\tfont-size: inherit !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.menu-checkbox[type=checkbox]~.menu-links>a,\n" +
                "\t\t\t.menu-checkbox[type=checkbox]~.menu-links>span.label {\n" +
                "\t\t\t\tdisplay: block !important;\n" +
                "\t\t\t\ttext-align: center;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.menu-checkbox[type=checkbox]:checked~.menu-trigger .menu-close {\n" +
                "\t\t\t\tdisplay: block !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.column .border,\n" +
                "\t\t\t.mobile_hide {\n" +
                "\t\t\t\tdisplay: none;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\ttable {\n" +
                "\t\t\t\ttable-layout: fixed !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.stack .column {\n" +
                "\t\t\t\twidth: 100%;\n" +
                "\t\t\t\tdisplay: block;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.mobile_hide {\n" +
                "\t\t\t\tmin-height: 0;\n" +
                "\t\t\t\tmax-height: 0;\n" +
                "\t\t\t\tmax-width: 0;\n" +
                "\t\t\t\toverflow: hidden;\n" +
                "\t\t\t\tfont-size: 0px;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.desktop_hide,\n" +
                "\t\t\t.desktop_hide table {\n" +
                "\t\t\t\tdisplay: table !important;\n" +
                "\t\t\t\tmax-height: none !important;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t#menui37qvf:checked~.menu-links {\n" +
                "\t\t\tbackground-color: #000000 !important;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t#menui37qvf:checked~.menu-links a,\n" +
                "\t\t#menui37qvf:checked~.menu-links span {\n" +
                "\t\t\tcolor: #ffffff !important;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "</head>\n" +
                "\n" +
                "<body style=\"background-color: #fff; margin: 0; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;\">\n" +
                "\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"nl-container\" role=\"presentation\"\n" +
                "\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #fff;\" width=\"100%\">\n" +
                "\t\t<tbody>\n" +
                "\t\t\t<tr>\n" +
                "\t\t\t\t<td>\n" +
                "\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-1\"\n" +
                "\t\t\t\t\t\trole=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\t\tclass=\"row-content stack\" role=\"presentation\"\n" +
                "\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\twidth=\"680\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"column column-1\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 5px; padding-bottom: 5px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"100%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"spacer_block\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"height:30px;line-height:30px;font-size:1px;\"> </div>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-2\"\n" +
                "\t\t\t\t\t\trole=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\t\tclass=\"row-content stack\" role=\"presentation\"\n" +
                "\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\twidth=\"680\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"column column-1\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"33.333333333333336%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"spacer_block\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"height:10px;line-height:5px;font-size:1px;\"> </div>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"column column-2\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"33.333333333333336%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tclass=\"image_block\" role=\"presentation\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"100%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"width:100%;padding-right:0px;padding-left:0px;padding-top:5px;padding-bottom:5px;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div align=\"center\" style=\"line-height:10px\"> \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<h1\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"margin: 0; color: #03191e; direction: ltr; font-family: 'Dosis', sans-serif !important; font-size: 30px; font-weight: normal; letter-spacing: normal; line-height: 120%; text-align: center; margin-top: 0; margin-bottom: 0;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<strong>WELCOME TO APPSLAPP!</strong>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</h1>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"column column-3\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"33.333333333333336%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"spacer_block\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"height:10px;line-height:5px;font-size:1px;\"> </div>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-3\"\n" +
                "\t\t\t\t\t\trole=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\t\tclass=\"row-content stack\" role=\"presentation\"\n" +
                "\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\twidth=\"680\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"column column-1\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 5px; padding-bottom: 5px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"100%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"spacer_block\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"height:10px;line-height:10px;font-size:1px;\"> </div>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-4\"\n" +
                "\t\t\t\t\t\trole=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\t\tclass=\"row-content stack\" role=\"presentation\"\n" +
                "\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\twidth=\"680\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"column column-1\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 5px; padding-bottom: 5px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"100%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tclass=\"image_block\" role=\"presentation\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"100%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"width:100%;padding-right:0px;padding-left:0px;padding-bottom:5px;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div align=\"center\" style=\"line-height:10px\"><img\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\talt=\"bear looking at password\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tclass=\"fullMobileWidth big\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tsrc=\"https://hawkemedia.com/wp-content/uploads/Email-Gif.gif\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"display: block; height: auto; border: 0; width: 400px; max-width: 100%; border-radius: 10px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\ttitle=\"bear looking at password\" width=\"612\" />\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-5\"\n" +
                "\t\t\t\t\t\trole=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\t\tclass=\"row-content stack\" role=\"presentation\"\n" +
                "\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\twidth=\"680\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"column column-1\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"16.666666666666668%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"spacer_block\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"height:10px;line-height:5px;font-size:1px;\"> </div>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"column column-2\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"66.66666666666667%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"text_block\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\trole=\"presentation\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"100%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"padding-bottom:10px;padding-left:20px;padding-right:10px;padding-top:10px;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div style=\"font-family: sans-serif\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"txtTinyMce-wrapper\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"font-size: 12px; mso-line-height-alt: 21.6px; color: #848484; line-height: 1.8; font-family: Arial, Helvetica Neue, Helvetica, sans-serif;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<p\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"margin: 0; font-size: 14px; text-align: center; mso-line-height-alt: 25.2px;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<span style=\"font-size:14px;\">Dear [[name]] thank you for joining Appslab comunity, please click on the button below to verify your accont. </span></p>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tclass=\"button_block\" role=\"presentation\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"100%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"padding-bottom:35px;padding-left:10px;padding-right:10px;padding-top:20px;text-align:center;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<div align=\"center\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"www.example.com\" style=\"height:44px;width:160px;v-text-anchor:middle;\" arcsize=\"10%\" strokeweight=\"0.75pt\" strokecolor=\"#03191E\" fillcolor=\"#03191e\"><w:anchorlock/><v:textbox inset=\"0px,0px,0px,0px\"><center style=\"color:#ffffff; font-family:Arial, sans-serif; font-size:16px\"><![endif]--><a\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\thref=\"[[URL]]\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"text-decoration:none;display:inline-block;color:#ffffff;background-color:#03191e;border-radius:4px;width:auto;border-top:1px solid #03191E;font-weight:undefined;border-right:1px solid #03191E;border-bottom:1px solid #03191E;border-left:1px solid #03191E;padding-top:5px;padding-bottom:5px;font-family:Arial, Helvetica Neue, Helvetica, sans-serif;text-align:center;mso-border-alt:none;word-break:keep-all;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\ttarget=\"_blank\"><span\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"padding-left:20px;padding-right:20px;font-size:16px;display:inline-block;letter-spacing:normal;\"><span\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"font-size: 16px; line-height: 2; word-break: break-word; mso-line-height-alt: 32px;\">Verify\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</span></span></a>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<!--[if mso]></center></v:textbox></v:roundrect><![endif]-->\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</div>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"column column-3\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"16.666666666666668%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"spacer_block\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"height:10px;line-height:5px;font-size:1px;\"> </div>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-6\"\n" +
                "\t\t\t\t\t\trole=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\t\tclass=\"row-content stack\" role=\"presentation\"\n" +
                "\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\twidth=\"680\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-7\"\n" +
                "\t\t\t\t\t\trole=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-8\"\n" +
                "\t\t\t\t\t\trole=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\t\tclass=\"row-content stack\" role=\"presentation\"\n" +
                "\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\twidth=\"680\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"column column-1\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"16.666666666666668%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"spacer_block\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"height:10px;line-height:5px;font-size:1px;\"> </div>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"column column-2\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"66.66666666666667%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"spacer_block\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"height:45px;line-height:5px;font-size:1px;\"> </div>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"column column-3\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"16.666666666666668%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<div class=\"spacer_block\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"height:10px;line-height:5px;font-size:1px;\"> </div>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-9\"\n" +
                "\t\t\t\t\t\trole=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t\t\t\t<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\t\tclass=\"row-content stack\" role=\"presentation\"\n" +
                "\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\twidth=\"680\">\n" +
                "\t\t\t\t\t\t\t\t\t\t<tbody>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t<td class=\"column column-1\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 5px; padding-bottom: 5px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"100%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tclass=\"icons_block\" role=\"presentation\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"100%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"vertical-align: middle; color: #9d9d9d; font-family: inherit; font-size: 15px; padding-bottom: 5px; padding-top: 5px; text-align: center;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<table cellpadding=\"0\" cellspacing=\"0\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\trole=\"presentation\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\twidth=\"100%\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<td\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\tstyle=\"vertical-align: middle; text-align: center;\">\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<!--[if vml]><table align=\"left\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"display:inline-block;padding-left:0px;padding-right:0px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;\"><![endif]-->\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t<!--[if !vml]><!-->\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t\t\t\t\t</table>\n" +
                "\t\t\t\t\t\t\t\t</td>\n" +
                "\t\t\t\t\t\t\t</tr>\n" +
                "\t\t\t\t\t\t</tbody>\n" +
                "\t\t\t\t\t</table>\n" +
                "\t\t\t\t</td>\n" +
                "\t\t\t</tr>\n" +
                "\t\t</tbody>\n" +
                "\t</table><!-- End -->\n" +
                "</body>\n" +
                "\n" +
                "</html>";

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
        String fromAddress = "appslappmanagement@gmail.com";
        String senderName = "AppsLapp";
        String subject = "Resetovanie hesla";
        String content = "<!DOCTYPE html>\n" +
                "\n" +
                "<html lang=\"en\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:v=\"urn:schemas-microsoft-com:vml\">\n" +
                "\n" +
                "<head>\n" +
                "<title></title>\n" +
                "<meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\" />\n" +
                "<meta content=\"width=device-width, initial-scale=1.0\" name=\"viewport\" />\n" +
                "<!--[if mso]><xml><o:OfficeDocumentSettings><o:PixelsPerInch>96</o:PixelsPerInch><o:AllowPNG/></o:OfficeDocumentSettings></xml><![endif]-->\n" +
                "<style>\n" +
                "* {\n" +
                "box-sizing: border-box;\n" +
                "}\n" +
                "\n" +
                "body {\n" +
                "margin: 0;\n" +
                "padding: 0;\n" +
                "}\n" +
                "\n" +
                "a[x-apple-data-detectors] {\n" +
                "color: inherit !important;\n" +
                "text-decoration: inherit !important;\n" +
                "}\n" +
                "\n" +
                "#MessageViewBody a {\n" +
                "color: inherit;\n" +
                "text-decoration: none;\n" +
                "}\n" +
                "\n" +
                "p {\n" +
                "line-height: inherit\n" +
                "}\n" +
                "\n" +
                ".desktop_hide,\n" +
                ".desktop_hide table {\n" +
                "mso-hide: all;\n" +
                "display: none;\n" +
                "max-height: 0px;\n" +
                "overflow: hidden;\n" +
                "}\n" +
                "\n" +
                ".menu_block.desktop_hide .menu-links span {\n" +
                "mso-hide: all;\n" +
                "}\n" +
                "\n" +
                "@media (max-width:700px) {\n" +
                ".desktop_hide table.icons-inner {\n" +
                "display: inline-block !important;\n" +
                "}\n" +
                "\n" +
                ".icons-inner {\n" +
                "text-align: center;\n" +
                "}\n" +
                "\n" +
                ".icons-inner td {\n" +
                "margin: 0 auto;\n" +
                "}\n" +
                "\n" +
                ".fullMobileWidth,\n" +
                ".row-content {\n" +
                "width: 100% !important;\n" +
                "}\n" +
                "\n" +
                ".image_block img.big {\n" +
                "width: auto !important;\n" +
                "}\n" +
                "\n" +
                ".menu-checkbox[type=checkbox]~.menu-links {\n" +
                "display: none !important;\n" +
                "padding: 5px 0;\n" +
                "}\n" +
                "\n" +
                ".menu-checkbox[type=checkbox]:checked~.menu-trigger .menu-open {\n" +
                "display: none !important;\n" +
                "}\n" +
                "\n" +
                ".menu-checkbox[type=checkbox]:checked~.menu-links,\n" +
                ".menu-checkbox[type=checkbox]~.menu-trigger {\n" +
                "display: block !important;\n" +
                "max-width: none !important;\n" +
                "max-height: none !important;\n" +
                "font-size: inherit !important;\n" +
                "}\n" +
                "\n" +
                ".menu-checkbox[type=checkbox]~.menu-links>a,\n" +
                ".menu-checkbox[type=checkbox]~.menu-links>span.label {\n" +
                "display: block !important;\n" +
                "text-align: center;\n" +
                "}\n" +
                "\n" +
                ".menu-checkbox[type=checkbox]:checked~.menu-trigger .menu-close {\n" +
                "display: block !important;\n" +
                "}\n" +
                "\n" +
                ".column .border,\n" +
                ".mobile_hide {\n" +
                "display: none;\n" +
                "}\n" +
                "\n" +
                "table {\n" +
                "table-layout: fixed !important;\n" +
                "}\n" +
                "\n" +
                ".stack .column {\n" +
                "width: 100%;\n" +
                "display: block;\n" +
                "}\n" +
                "\n" +
                ".mobile_hide {\n" +
                "min-height: 0;\n" +
                "max-height: 0;\n" +
                "max-width: 0;\n" +
                "overflow: hidden;\n" +
                "font-size: 0px;\n" +
                "}\n" +
                "\n" +
                ".desktop_hide,\n" +
                ".desktop_hide table {\n" +
                "display: table !important;\n" +
                "max-height: none !important;\n" +
                "}\n" +
                "}\n" +
                "\n" +
                "#menui37qvf:checked~.menu-links {\n" +
                "background-color: #000000 !important;\n" +
                "}\n" +
                "\n" +
                "#menui37qvf:checked~.menu-links a,\n" +
                "#menui37qvf:checked~.menu-links span {\n" +
                "color: #ffffff !important;\n" +
                "}\n" +
                "</style>\n" +
                "</head>\n" +
                "\n" +
                "<body style=\"background-color: #fff; margin: 0; padding: 0; -webkit-text-size-adjust: none; text-size-adjust: none;\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"nl-container\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #fff;\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-1\"\n" +
                "role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "class=\"row-content stack\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "width=\"680\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td class=\"column column-1\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 5px; padding-bottom: 5px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "width=\"100%\">\n" +
                "<div class=\"spacer_block\"\n" +
                "style=\"height:30px;line-height:30px;font-size:1px;\"> </div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-2\"\n" +
                "role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "class=\"row-content stack\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "width=\"680\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td class=\"column column-1\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "width=\"33.333333333333336%\">\n" +
                "<div class=\"spacer_block\"\n" +
                "style=\"height:10px;line-height:5px;font-size:1px;\"> </div>\n" +
                "</td>\n" +
                "<td class=\"column column-2\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "width=\"33.333333333333336%\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "class=\"image_block\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n" +
                "width=\"100%\">\n" +
                "<tr>\n" +
                "<td\n" +
                "style=\"width:100%;padding-right:0px;padding-left:0px;padding-top:5px;padding-bottom:5px;\">\n" +
                "<div align=\"center\" style=\"line-height:10px\"><h1\n" +
                "style=\"margin: 0; color: #03191e; direction: ltr; font-family: 'Dosis', sans-serif !important; font-size: 40px; font-weight: normal; letter-spacing: normal; line-height: 120%; text-align: center; margin-top: 0; margin-bottom: 0;\">\n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "</td>\n" +
                "<td class=\"column column-3\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "width=\"33.333333333333336%\">\n" +
                "<div class=\"spacer_block\"\n" +
                "style=\"height:10px;line-height:5px;font-size:1px;\"> </div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-3\"\n" +
                "role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "class=\"row-content stack\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "width=\"680\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td class=\"column column-1\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 5px; padding-bottom: 5px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "width=\"100%\">\n" +
                "<div class=\"spacer_block\"\n" +
                "style=\"height:10px;line-height:10px;font-size:1px;\"> </div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-4\"\n" +
                "role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "class=\"row-content stack\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "width=\"680\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td class=\"column column-1\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 5px; padding-bottom: 5px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "width=\"100%\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "class=\"image_block\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n" +
                "width=\"100%\">\n" +
                "<tr>\n" +
                "<td\n" +
                "style=\"width:100%;padding-right:0px;padding-left:0px;padding-bottom:5px;\">\n" +
                "<div align=\"center\" style=\"line-height:10px\"><img\n" +
                "alt=\"bear looking at password\"\n" +
                "class=\"fullMobileWidth big\"\n" +
                "src=\"https://media.discordapp.net/attachments/983375202794741791/984374509295505448/LogoEffect.png\"\n" +
                "style=\"display: block; height: auto; border: 0;  max-width: 100%;\"\n" +
                "title=\"bear looking at password\" width=\"612\" />" +
                "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-5\"\n" +
                "role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "class=\"row-content stack\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "width=\"680\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td class=\"column column-1\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "width=\"16.666666666666668%\">\n" +
                "<div class=\"spacer_block\"\n" +
                "style=\"height:10px;line-height:5px;font-size:1px;\"> </div>\n" +
                "</td>\n" +
                "<td class=\"column column-2\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "width=\"66.66666666666667%\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "class=\"heading_block\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n" +
                "width=\"100%\">\n" +
                "<tr>\n" +
                "<td style=\"text-align:center;width:100%;padding-top:5px;\">\n" +
                "<h1\n" +
                "style=\"margin: 0; color: #03191e; direction: ltr; font-family: 'Dosis', sans-serif !important; font-size: 40px; font-weight: normal; letter-spacing: normal; line-height: 120%; text-align: center; margin-top: 0; margin-bottom: 0;\">\n" +
                "<strong>Forgot</strong></h1>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "class=\"heading_block\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n" +
                "width=\"100%\">\n" +
                "<tr>\n" +
                "<td\n" +
                "style=\"padding-bottom:10px;text-align:center;width:100%;\">\n" +
                "<h1\n" +
                "style=\"margin: 0; color: #03191e; direction: ltr; font-family: 'Dosis', sans-serif !important; font-size: 40px; font-weight: normal; letter-spacing: normal; line-height: 120%; text-align: center; margin-top: 0; margin-bottom: 0;\">\n" +
                "<strong>Your Password?</strong></h1>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"text_block\"\n" +
                "role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; word-break: break-word;\"\n" +
                "width=\"100%\">\n" +
                "<tr>\n" +
                "<td\n" +
                "style=\"padding-bottom:10px;padding-left:20px;padding-right:10px;padding-top:10px;\">\n" +
                "<div style=\"font-family: sans-serif\">\n" +
                "<div class=\"txtTinyMce-wrapper\"\n" +
                "style=\"font-size: 12px; mso-line-height-alt: 21.6px; color: #848484; line-height: 1.8; font-family: Arial, Helvetica Neue, Helvetica, sans-serif;\">\n" +
                "<p\n" +
                "style=\"margin: 0; font-size: 14px; text-align: center; mso-line-height-alt: 25.2px;\">\n" +
                "<span style=\"font-size:14px;\">Dear [[name]] Reset your password HERE:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "class=\"button_block\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n" +
                "width=\"100%\">\n" +
                "<tr>\n" +
                "<td\n" +
                "style=\"padding-bottom:35px;padding-left:10px;padding-right:10px;padding-top:20px;text-align:center;\">\n" +
                "<div align=\"center\">\n" +
                "<!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"www.example.com\" style=\"height:44px;width:160px;v-text-anchor:middle;\" arcsize=\"10%\" strokeweight=\"0.75pt\" strokecolor=\"#03191E\" fillcolor=\"#03191e\"><w:anchorlock/><v:textbox inset=\"0px,0px,0px,0px\"><center style=\"color:#ffffff; font-family:Arial, sans-serif; font-size:16px\"><![endif]--><a\n" +
                "href=\"[[URL]]\"\n" +
                "style=\"text-decoration:none;display:inline-block;color:#ffffff;background-color:#03191e;border-radius:4px;width:auto;border-top:1px solid #03191E;font-weight:undefined;border-right:1px solid #03191E;border-bottom:1px solid #03191E;border-left:1px solid #03191E;padding-top:5px;padding-bottom:5px;font-family:Arial, Helvetica Neue, Helvetica, sans-serif;text-align:center;mso-border-alt:none;word-break:keep-all;\"\n" +
                "target=\"_blank\"><span\n" +
                "style=\"padding-left:20px;padding-right:20px;font-size:16px;display:inline-block;letter-spacing:normal;\"><span\n" +
                "style=\"font-size: 16px; line-height: 2; word-break: break-word; mso-line-height-alt: 32px;\">Reset\n" +
                "Password</span></span></a>\n" +
                "<!--[if mso]></center></v:textbox></v:roundrect><![endif]-->\n" +
                "</div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "</td>\n" +
                "<td class=\"column column-3\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "width=\"16.666666666666668%\">\n" +
                "<div class=\"spacer_block\"\n" +
                "style=\"height:10px;line-height:5px;font-size:1px;\"> </div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-6\"\n" +
                "role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "class=\"row-content stack\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "width=\"680\">\n" +
                "\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-7\"\n" +
                "role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>\n" +
                "\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-8\"\n" +
                "role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "class=\"row-content stack\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "width=\"680\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td class=\"column column-1\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "width=\"16.666666666666668%\">\n" +
                "<div class=\"spacer_block\"\n" +
                "style=\"height:10px;line-height:5px;font-size:1px;\"> </div>\n" +
                "</td>\n" +
                "<td class=\"column column-2\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "width=\"66.66666666666667%\">\n" +
                "<div class=\"spacer_block\"\n" +
                "style=\"height:45px;line-height:5px;font-size:1px;\"> </div>\n" +
                "</td>\n" +
                "<td class=\"column column-3\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "width=\"16.666666666666668%\">\n" +
                "<div class=\"spacer_block\"\n" +
                "style=\"height:10px;line-height:5px;font-size:1px;\"> </div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"row row-9\"\n" +
                "role=\"presentation\" style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td>\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "class=\"row-content stack\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; color: #000000; width: 680px;\"\n" +
                "width=\"680\">\n" +
                "<tbody>\n" +
                "<tr>\n" +
                "<td class=\"column column-1\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt; font-weight: 400; text-align: left; vertical-align: top; padding-top: 5px; padding-bottom: 5px; border-top: 0px; border-right: 0px; border-bottom: 0px; border-left: 0px;\"\n" +
                "width=\"100%\">\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                "class=\"icons_block\" role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n" +
                "width=\"100%\">\n" +
                "<tr>\n" +
                "<td\n" +
                "style=\"vertical-align: middle; color: #9d9d9d; font-family: inherit; font-size: 15px; padding-bottom: 5px; padding-top: 5px; text-align: center;\">\n" +
                "<table cellpadding=\"0\" cellspacing=\"0\"\n" +
                "role=\"presentation\"\n" +
                "style=\"mso-table-lspace: 0pt; mso-table-rspace: 0pt;\"\n" +
                "width=\"100%\">\n" +
                "<tr>\n" +
                "<td\n" +
                "style=\"vertical-align: middle; text-align: center;\">\n" +
                "<!--[if vml]><table align=\"left\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"display:inline-block;padding-left:0px;padding-right:0px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;\"><![endif]-->\n" +
                "<!--[if !vml]><!-->\n" +
                "\n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table><!-- End -->\n" +
                "</body>\n" +
                "\n" +
                "</html>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());

        String verifyURL = "https://appslappapp.vercel.app/reset-password/" + user.getUsername();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
        return 1L;
    }
}
