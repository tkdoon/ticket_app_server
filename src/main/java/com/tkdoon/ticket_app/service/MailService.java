package com.tkdoon.ticket_app.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromAddress;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTicketNotification(
            String toEmail,
            String ownerName,
            String creatorName,
            String title,
            String description,
            LocalDate expiringDate
    ) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(toEmail);
            helper.setSubject("「" + creatorName + "」さんからチケットが届きました");
            helper.setText(buildHtml(ownerName, creatorName, title, description, expiringDate), true);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("チケット通知メール送信失敗 to={}", toEmail, e);
        }
    }

    private String buildHtml(
            String ownerName,
            String creatorName,
            String title,
            String description,
            LocalDate expiringDate
    ) {
        String formattedDate = expiringDate.format(DateTimeFormatter.ofPattern("yyyy年MM月dd日"));
        String descriptionHtml = (description != null && !description.isBlank())
                ? "<p style=\"color:#555;margin:0 0 16px 0;line-height:1.6;\">" + escapeHtml(description) + "</p>"
                : "";

        return """
                <!DOCTYPE html>
                <html lang="ja">
                <head><meta charset="UTF-8"></head>
                <body style="margin:0;padding:0;background-color:#f5f5f5;font-family:Arial,sans-serif;">
                  <table width="100%%" cellpadding="0" cellspacing="0" style="background-color:#f5f5f5;padding:40px 20px;">
                    <tr><td align="center">
                      <table width="600" cellpadding="0" cellspacing="0" style="background:#fff;border-radius:12px;overflow:hidden;box-shadow:0 2px 8px rgba(0,0,0,0.1);max-width:600px;">
                        <tr>
                          <td style="background:linear-gradient(135deg,#667eea,#764ba2);padding:40px;text-align:center;">
                            <p style="margin:0 0 8px 0;font-size:36px;">🎁</p>
                            <h1 style="color:#fff;margin:0;font-size:22px;font-weight:bold;">チケットが届きました</h1>
                          </td>
                        </tr>
                        <tr>
                          <td style="padding:36px 40px;">
                            <p style="color:#666;font-size:16px;margin:0 0 8px 0;">%s さん、こんにちは！</p>
                            <p style="color:#333;font-size:16px;margin:0 0 28px 0;"><strong>%s</strong> さんからギフトチケットが届きました。</p>
                            <div style="border:2px solid #667eea;border-radius:10px;padding:24px;">
                              <h2 style="color:#667eea;margin:0 0 12px 0;font-size:20px;">%s</h2>
                              %s
                              <p style="color:#999;font-size:13px;margin:0;">有効期限：%s</p>
                            </div>
                            <div style="text-align:center;margin-top:36px;">
                              <a href="%s" style="background:linear-gradient(135deg,#667eea,#764ba2);color:#fff;padding:14px 36px;border-radius:24px;text-decoration:none;font-size:16px;font-weight:bold;display:inline-block;">アプリを開く</a>
                            </div>
                          </td>
                        </tr>
                        <tr>
                          <td style="background:#f9f9f9;padding:20px;text-align:center;">
                            <p style="color:#bbb;font-size:12px;margin:0;">このメールはTicket Appから自動送信されています</p>
                          </td>
                        </tr>
                      </table>
                    </td></tr>
                  </table>
                </body>
                </html>
                """.formatted(
                escapeHtml(ownerName),
                escapeHtml(creatorName),
                escapeHtml(title),
                descriptionHtml,
                formattedDate,
                frontendUrl
        );
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }
}
