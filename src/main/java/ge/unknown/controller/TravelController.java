package ge.unknown.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import ge.unknown.DTO.PolicySuccessDTO;
import ge.unknown.DTO.Transaction;
import ge.unknown.DTO.Travel;
import ge.unknown.service.ApiService;
import ge.unknown.service.StorageService;
import ge.unknown.thread.ThreadPoolManager;
import ge.unknown.utils.PaymentUtil;
import lombok.experimental.var;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static ge.unknown.utils.constants.Constants.BASE_URL;

@RestController
@RequestMapping("/travel")
public class TravelController {

    @Autowired
    private ApiService api;

    @Autowired
    private StorageService storageService;



    @RequestMapping(value = "/addPolicy", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Transaction> addPolicy(@RequestBody Travel travel) {
        travel.setOper("addpolicy");
        travel.setProduct("travel");
        travel.setAddress("");
        travel.setPhone("");
        travel.setTravellimit("50000");
        travel.setCountrylist(travel.getCountrylist().replaceAll("undefined", ""));
        travel.setPolicydate(new SimpleDateFormat("dd/mm/yyyy").format(new Date()));

        if (travel.getBagage() != null && travel.getBagage().equals("true")) {
            travel.setBagage("true");
        } else {
            travel.setBagage("false");
        }



        double price = calcualte(travel);
        if (price > 0) {
            Transaction transaction = api.createTransaction(price);
            try {
                ObjectMapper mapper = new ObjectMapper();
                boolean res = PaymentUtil.writeStringToFile(transaction.getTransId(), mapper.writeValueAsString(travel), storageService.getRootLocation().toAbsolutePath().toString());
                if (res) {
                    return ResponseEntity.ok(transaction);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.badRequest().build();
    }



    private double calcualte(Travel travel) {

        if (travel.getDob() == null || travel.getCountrylist() == null || travel.getStartdate() == null || travel.getEnddate() == null) {
            return 0d;
        }

        double startPrice = 1;

        try {

            DateFormat fd = new SimpleDateFormat("dd/mm/yyyy");

            int numberOfDays = Math.abs(Days.daysBetween(new LocalDate(fd.parse(travel.getStartdate())), new LocalDate(fd.parse(travel.getEnddate()))).getDays());
            numberOfDays += 1;
            int dob = Math.abs(Years.yearsBetween(new LocalDate(), new LocalDate(fd.parse(travel.getDob()))).getYears());


            if (travel.getCountrylist().contains("USA") || travel.getCountrylist().contains("Canada")) {
                startPrice = 2;
            }

            if (numberOfDays > 0) {
                startPrice *= numberOfDays;
            }

            if (dob < 15) {
                startPrice /= 2;
            } else if (dob >= 65 && dob <= 75) {
                startPrice *= 2;
            } else if (dob > 75) {
                return 0;
            }

            if (travel.getBagage() != null && travel.getBagage().equals("true")) {
                startPrice += 10;
            }
        } catch (Exception e) {

        }

        return startPrice;
    }

}
