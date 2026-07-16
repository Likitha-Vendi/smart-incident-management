package com.smartims.service.impl;

import com.smartims.enums.OtpPurpose;
import com.smartims.service.AuditLogService;
import com.smartims.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender mailSender;

    private final AuditLogService auditLogService;


    @Value("${spring.mail.username}")
    private String mailUsername;



    @Override
    public void sendOtpEmail(
            String email,
            String otp,
            OtpPurpose purpose
    ) {


        String subject =
                purpose == OtpPurpose.REGISTER
                        ? "Verify Your ServicePlus Account"
                        : "Reset Your ServicePlus Password";


        String body = """
                Hello,

                Your OTP is: %s

                This OTP is valid for 3 minutes.

                Do not share this OTP.

                Regards,
                ServicePlus Team
                """.formatted(otp);



        sendMail(
                email,
                subject,
                body
        );


        auditLogService.logSystem(
                "OTP_EMAIL_SENT",
                "OTP email sent successfully",
                null,
                "EMAIL"
        );

    }




    @Override
    public void sendContactAcknowledgement(
            String email,
            String name
    ) {


        String body = """
                Hello %s,

                Thank you for contacting ServicePlus.

                Our team will respond shortly.

                Regards,
                ServicePlus Team
                """.formatted(name);



        sendMail(
                email,
                "We Received Your Message",
                body
        );

    }





    @Override
    public void sendNewUserCredentialsEmail(
            String email,
            String fullName,
            String username,
            String rawPassword,
            String loginUrl
    ) {


        String body = """
                Hello %s,

                Your account has been created.

                Username: %s
                Password: %s

                Login URL:
                %s


                Regards,
                ServicePlus Team
                """.formatted(
                fullName,
                username,
                rawPassword,
                loginUrl
        );



        sendMail(
                email,
                "Your ServicePlus Account Details",
                body
        );

    }





    private void sendMail(
            String to,
            String subject,
            String body
    ) {


        try {


            SimpleMailMessage message =
                    new SimpleMailMessage();


            message.setFrom(mailUsername);

            message.setTo(to);

            message.setSubject(subject);

            message.setText(body);



            System.out.println("==========================");
            System.out.println("MAIL TEST");
            System.out.println("FROM : " + mailUsername);
            System.out.println("TO   : " + to);
            System.out.println("==========================");


            mailSender.send(message);



            System.out.println("==========================");
            System.out.println("MAIL SENT SUCCESSFULLY");
            System.out.println("==========================");



        }
        catch(Exception e) {


            System.out.println("==========================");
            System.out.println("MAIL FAILED");
            System.out.println(e.getMessage());
            System.out.println("==========================");


            throw e;

        }

    }

}