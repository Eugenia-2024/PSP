package es.paraninfo.psp.email;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

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

    private void setPropiedadesServidorSMTP() {
        propiedades = System.getProperties();
        propiedades.put("mail.smtp,auth", "true");
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.starttls.enable", "true");
        sesion.getInstance(propiedades, null);

    }
    private Transport conectarServidorSMTP(String direccionEmail, String password)
            throws NoSuchProviderException, MessagingException {
        Transport t = (Transport) sesion.getTransport("smtp");
        t.connect(propiedades.getProperty("mail.smtp.host"), direccionEmail, password);
        return t;
    }
    private Message crearNucleoMensaje(String emisor, String destinatario, String asunto)
            throws MessagingException, AddressException {
        Message mensaje = new MimeMessage(sesion);
        mensaje.setFrom(new InternetAddress(emisor));
        mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
        mensaje.setSubject(asunto);
        return mensaje;
    }
    private Message crearMensajeTexto(String emisor, String destinatario, String asunto,
                                      String textoMensaje)
            throws MessagingException, AddressException, IOException {
        Message mensaje = crearNucleoMensaje(emisor, destinatario, asunto);
        mensaje.setText(textoMensaje);
        mensaje.setSubject(asunto);
        return mensaje;
    }
    private Message crearMensajeConAdjunto(String emisor, String destinatario, String asunto,
                                           String textoMensaje, String pathFichero)
            throws MessagingException, AddressException, IOException {
        Message mensaje = crearNucleoMensaje (emisor, destinatario, asunto);
        jakarta.mail.BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setText(textoMensaje);
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);
        multipart.addBodyPart(mimeBodyPart);
        mensaje.setContent(multipart);
        return mensaje;
    }
    public void enviarMensajeTexto(String emisor, String destinatario, String asunto,
                                   String textoMensaje, String direccionEmail, String password)
            throws AddressException, MessagingException, IOException {
        setPropiedadesServidorSMTP();
        Message mensaje = crearMensajeTexto(emisor, destinatario, asunto, textoMensaje);
        Transport t = conectarServidorSMTP(direccionEmail, password);
        t.sendMessage(mensaje, mensaje.getAllRecipients());
        t.close();
        return;
    }
    public void enviarMensajeConAdjunto(String emisor, String destinatario, String asunto,
                                        String textoMensaje, String direccionEmail, String password, String pathFichero)
            throws AddressException, MessagingException, IOException{

        setPropiedadesServidorSMTP();
        Message mensaje = crearMensajeConAdjunto(emisor, destinatario, asunto,
                textoMensaje,pathFichero);
        Transport t = conectarServidorSMTP(direccionEmail, password);
        t.sendMessage(mensaje, mensaje.getAllRecipients());
        t.close();
        return;
    }

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Introduce dirección de correo: ");
            String emailEmisor = sc.nextLine();
            System.out.print("Introduce contraseña: ");
            String passwordEmisor = sc.nextLine();
            sc.close();

            GestorEMail gestorEMail = new GestorEMail();
            gestorEMail.enviarMensajeTexto(emailEmisor, "cesar.gayo@gmail.com", "Aviso de factura",
                    "El importe de la factura es 100€", emailEmisor, passwordEmisor);
            gestorEMail.enviarMensajeConAdjunto(emailEmisor, "cesar.gayo@gmail.com", "Aviso de factura",
                    "El importe de la factura es 100€", emailEmisor, passwordEmisor,"C:/Factura.pdf");
            System.out.println("Correo enviado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}//class
