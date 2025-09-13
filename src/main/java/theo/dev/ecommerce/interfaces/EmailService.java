package theo.dev.ecommerce.interfaces;

import theo.dev.ecommerce.dto.EmailDetails;

// Interface
public interface EmailService {

    String sendSimpleMail(EmailDetails details);

    String sendMailWithAttachment(EmailDetails details);
}
