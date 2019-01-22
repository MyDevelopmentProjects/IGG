package ge.unknown.controller;

import ge.unknown.DTO.PolicySuccessDTO;
import ge.unknown.data.AllDoctorsSingleton;
import ge.unknown.data.AllProductsSingleton;
import ge.unknown.data.Doctor;
import ge.unknown.data.Product;
import ge.unknown.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ge.unknown.utils.constants.Constants.Products.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ApiService service;

    List<Product> availableProducts = new ArrayList<>();

    private void sendEmail(String subject, String to, String comment) {
        try {
            mailSender.send(mimeMessage -> {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setFrom("no-reply@igg.ge");
                message.setTo(to); // send to user email
                message.setSubject(subject);
                message.setText(comment, true);
            });

        } catch (Exception e) {
        }
    }

    @GetMapping("/syncProducts")
    public void syncProducts() {
        service.syncProducts();
        availableProducts.clear();
        AllProductsSingleton.getInstance().getProducts().forEach(obj -> {
            if (obj.getProductdid().equals(BAGGADZE_INS) ||
                    obj.getProductdid().equals(TPL1) ||
                    obj.getProductdid().equals(MPA1) ||
                    obj.getProductdid().equals(TRANSPORTER_INS) ||
                    obj.getProductdid().equals(TRAVEL_INS) ||
                    obj.getProductdid().equals(AUTO_INS)) {
                availableProducts.add(obj);
            }
        });
    }

    @GetMapping("/getschedule")
    public String getschedule(String dt, String doctorId) {
        return service.getschedule(dt, doctorId);
    }

    @GetMapping("/syncDoctors")
    public void syncDoctors() {
        service.syncDoctors();
    }

    @GetMapping("/listAllProducts")
    public List<Product> listAllProducts() {
        return availableProducts;
    }

    @GetMapping("/listAllDoctors")
    public List<Doctor> listAllDoctors() {
        return AllDoctorsSingleton.getInstance().getDoctors();
    }

    @GetMapping("/findDoctors")
    public List<Doctor> findDoctors(@RequestParam(name = "city") String city) {
        return AllDoctorsSingleton.getInstance().findDoctors(city);
    }

    @RequestMapping(value = "/addVisit", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<PolicySuccessDTO> addVisit(@RequestBody Map map) {
        map.put("oper", "visit");
        return ResponseEntity.ok(service.send(map));
    }

}
