package es.paraninfo.psp.email;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Scanner;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class GestorEMail {

    private Properties propiedades;
    private Session sesion;

    // === Choose provider here ===
    // true  -> Gmail (smtp.gmail.com)
    // false -> Mail.ru (smtp.mail.ru)
    private static final boolean USE_GMAIL = true;

    // === SMTP configuration ===
    private void setPropiedadesServidorSMTP() {

        propiedades = new Properties();

        if (USE_GMAIL) {
            // Gmail STARTTLS (requires App Password)
            propiedades.put("mail.smtp.host", "smtp.gmail.com");
            propiedades.put("mail.smtp.port", "587");
            propiedades.put("mail.smtp.auth", "true");
            propiedades.put("mail.smtp.starttls.enable", "true");
        } else {
            // Mail.ru STARTTLS (requires App Password + user must be @mail.ru)
            propiedades.put("mail.smtp.host", "smtp.mail.ru");
            propiedades.put("mail.smtp.port", "587");
            propiedades.put("mail.smtp.auth", "true");
            propiedades.put("mail.smtp.starttls.enable", "true");
        }

        // Recommended timeouts (avoid hanging)
        propiedades.put("mail.smtp.connectiontimeout", "10000");
        propiedades.put("mail.smtp.timeout", "10000");
        propiedades.put("mail.smtp.writetimeout", "10000");

        // Debug output for screenshots
        propiedades.put("mail.debug", "true");

        // CRITICAL FIX: initialize the session and store it in the field
        this.sesion = Session.getInstance(propiedades);
        this.sesion.setDebug(true);
    }

    private Transport conectarServidorSMTP(String direccionEmail, String password)
            throws NoSuchProviderException, MessagingException {

        Transport t = sesion.getTransport("smtp");
        t.connect(propiedades.getProperty("mail.smtp.host"), direccionEmail, password);
        return t;
    }

    private Message crearNucleoMensaje(String emisor, String destinatario, String asunto)
            throws MessagingException, AddressException {

        MimeMessage mensaje = new MimeMessage(sesion);  // before it was Message
        mensaje.setFrom(new InternetAddress(emisor));
        mensaje.setRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
        mensaje.setSubject(asunto, StandardCharsets.UTF_8.name()); // compiles

        return mensaje;
    }

    private Message crearMensajeTexto(String emisor, String destinatario, String asunto, String textoMensaje)
            throws MessagingException, AddressException, IOException {

        Message mensaje = crearNucleoMensaje(emisor, destinatario, asunto);
        mensaje.setContent(textoMensaje, "text/plain; charset=UTF-8"); // compatible

        return mensaje;
    }

    private Message crearMensajeConAdjunto(String emisor, String destinatario, String asunto,
                                           String textoMensaje, String pathFichero)
            throws MessagingException, AddressException, IOException {

        Message mensaje = crearNucleoMensaje(emisor, destinatario, asunto);

        // Text body part
        MimeBodyPart bodyText = new MimeBodyPart();
        bodyText.setContent(textoMensaje, "text/plain; charset=UTF-8");

        // Attachment part
        File f = new File(pathFichero);
        if (!f.exists() || !f.isFile()) {
            throw new IOException("Attachment file not found: " + pathFichero);
        }

        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(f);

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyText);
        multipart.addBodyPart(attachmentPart);

        mensaje.setContent(multipart);
        return mensaje;
    }

    public void enviarMensajeTexto(String emisor, String destinatario, String asunto, String textoMensaje,
                                   String direccionEmail, String password)
            throws AddressException, MessagingException, IOException {

        setPropiedadesServidorSMTP();
        Message mensaje = crearMensajeTexto(emisor, destinatario, asunto, textoMensaje);

        Transport t = conectarServidorSMTP(direccionEmail, password);
        t.sendMessage(mensaje, mensaje.getAllRecipients());
        t.close();
    }

    public void enviarMensajeConAdjunto(String emisor, String destinatario, String asunto, String textoMensaje,
                                        String direccionEmail, String password, String pathFichero)
            throws AddressException, MessagingException, IOException {

        setPropiedadesServidorSMTP();
        Message mensaje = crearMensajeConAdjunto(emisor, destinatario, asunto, textoMensaje, pathFichero);

        Transport t = conectarServidorSMTP(direccionEmail, password);
        t.sendMessage(mensaje, mensaje.getAllRecipients());
        t.close();
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {

            System.out.print("Enter sender email: ");
            String emailEmisor = sc.nextLine().trim();

            System.out.print("Enter APP password: ");
            String passwordEmisor = sc.nextLine().trim();

            System.out.print("Enter recipient email: ");
            String destinatario = sc.nextLine().trim();

            if (emailEmisor.isEmpty() || passwordEmisor.isEmpty() || destinatario.isEmpty()) {
                System.out.println("ERROR: sender, password and recipient cannot be empty.");
                return;
            }

            // IMPORTANT: avoid mismatch user/provider
            if (!USE_GMAIL && !emailEmisor.toLowerCase().endsWith("@mail.ru")) {
                System.out.println("ERROR: If USE_GMAIL=false (Mail.ru), sender must be an @mail.ru account.");
                return;
            }

            GestorEMail gestorEMail = new GestorEMail();

            // 1) Send plain text (recommended first)
            gestorEMail.enviarMensajeTexto(
                    emailEmisor,
                    destinatario,
                    "Paraninfo JavaMail test",
                    "This email was sent from Java using SMTP + STARTTLS.",
                    emailEmisor,
                    passwordEmisor
            );

            System.out.println("Text email sent successfully.");

            // 2) Optional attachment (do NOT force it)
            System.out.print("Do you want to send an attachment too? (y/n): ");
            String opt = sc.nextLine().trim().toLowerCase();

            if (opt.equals("y")) {
                System.out.print("Enter attachment file path: ");
                String path = sc.nextLine().trim();

                gestorEMail.enviarMensajeConAdjunto(
                        emailEmisor,
                        destinatario,
                        "Paraninfo JavaMail attachment test",
                        "This email includes an attachment.",
                        emailEmisor,
                        passwordEmisor,
                        path
                );

                System.out.println("Email with attachment sent successfully.");
            }

            System.out.println("Done.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}//class
