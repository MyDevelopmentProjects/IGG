package ge.unknown.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.unknown.service.StorageService;
import ge.unknown.thread.ThreadPoolManager;
import ge.unknown.utils.MGLIOUtils;
import ge.unknown.utils.MGLStringUtils;
import ge.unknown.utils.RequestResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public RequestResponseEntity<List<String>> uploadMultipleFileHandler(@RequestParam("file") MultipartFile[] files,
                                                                         @RequestParam("type") String object,
                                                                         @RequestParam("fullname") String fullname,
                                                                         @RequestParam("phone") String phone,
                                                                         @RequestParam("email") String email) {
        RequestResponseEntity<List<String>> response  = new RequestResponseEntity<>(null);
        if (files.length != 6 || MGLStringUtils.IsNullOrBlank(object)){
            response.setMessage("ყველა ფაილი აუცილებელია");
            response.setSuccess(false);
            return response;
        }
        /*if (!MGLIOUtils.limitFileSize(files)) {
            response.setMessage("არასწორიო");
            response.setSuccess(false);
            return response;
        }*/
        if (!MGLIOUtils.isValidObjectPath(object)) {
            response.setMessage("არასწორი ფაილის ფორმატი");
            response.setSuccess(false);
            return response;
        }

        List<String> fileList = new ArrayList<>();
        for(MultipartFile file : files){
            String extension = "";
            int index = file.getOriginalFilename().lastIndexOf('.');
            if (index >= 0){
                extension = file.getOriginalFilename().substring(index + 1);
            }
            if (!MGLIOUtils.isValidExtension(extension.toLowerCase())){
                response.setMessage("არასწორი ფაილის ფორმატი");
                response.setSuccess(false);
                return response;
            }
            fileList.add(storageService.storeEncoded(file));
        }

        response.setContent(fileList);

        StringBuilder sb = new StringBuilder();
        sb.append("Mail From: ").append(email).append("<br/>");
        sb.append("Phone: ").append(phone).append("<br/>");
        sb.append("Fullname: ").append(fullname).append("<br/>");

        ThreadPoolManager.execute(() -> {
            sendEmail(email, sb.toString(), fileList);
        });

        response.setSuccess(true);
        return response;
    }

    private void sendEmail(String subject, String comment, List<String> files) {
        try {
            mailSender.send(mimeMessage -> {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setFrom("no-reply@igg.ge");
                message.setTo("malatsidze@igg.ge"); // send to user email
                message.setCc("burjanadze@igg.ge");
                message.setCc("kashia@igg.ge");
                message.setSubject(subject);
                message.setText(comment, true);

                for(String file : files){
                    try {
                        message.addAttachment(file, new File(storageService.load(file).toAbsolutePath().toString()));
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
