package ge.unknown.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.unknown.DTO.PolicySuccessDTO;
import ge.unknown.DTO.TPL;
import ge.unknown.DTO.Transaction;
import ge.unknown.DTO.Travel;
import ge.unknown.service.ApiService;
import ge.unknown.service.StorageService;
import ge.unknown.utils.MGLIOUtils;
import ge.unknown.utils.MGLStringUtils;
import ge.unknown.utils.PaymentUtil;
import ge.unknown.utils.RequestResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/tpl")
public class TPLController {

    @Autowired
    private ApiService api;

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "/addPolicy", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Transaction> addPolicy(@RequestBody TPL tpl) {
        tpl.setGender("M");
        tpl.setStartdate("01/01/2018");
        tpl.setEnddate("01/01/2018");
        tpl.setOper("addpolicy");
        tpl.setProduct("tpl");
        tpl.setPackagename("GOLD");
        if (tpl.getDob() == null || tpl.getDob().equals("")) {
            tpl.setDob("01/01/2018");
        }

        double price = calculate(tpl);
        if (price > 0) {
            Transaction transaction = api.createTransaction(price);
            try {
                ObjectMapper mapper = new ObjectMapper();
                boolean res = PaymentUtil.writeStringToFile(transaction.getTransId(), mapper.writeValueAsString(tpl), storageService.getRootLocation().toAbsolutePath().toString());
                if (res) {
                    return ResponseEntity.ok(transaction);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    private double calculate(TPL tpl) {
        double price = 0;
        if (tpl.getLimit() != null && !tpl.getLimit().equals("") &&
                tpl.getPaymentschedule() != null && tpl.getPaymentschedule() > 0) {
            double _price = 0;

            if (tpl.getLimit().equals("5000")) {
                _price = 60;
            } else if (tpl.getLimit().equals("10000")) {
                _price = 100;
            } else if (tpl.getLimit().equals("20000")) {
                _price = 200;
            } else if (tpl.getLimit().equals("30000")) {
                _price = 300;
            }

            if (tpl.getPaymentschedule().equals(1)) {
                price = _price;
            } else if (tpl.getPaymentschedule().equals(2)) {
                price = _price / 4;
            } else if (tpl.getPaymentschedule().equals(12)) {
                price = _price / 12;
            }
        }
        return price;
    }

}
