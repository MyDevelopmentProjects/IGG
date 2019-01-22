package ge.unknown.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import ge.unknown.DTO.CASCO;
import ge.unknown.DTO.InsuranceKoef;
import ge.unknown.DTO.PolicySuccessDTO;
import ge.unknown.DTO.TPL;
import ge.unknown.service.ApiService;
import ge.unknown.thread.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import static ge.unknown.utils.constants.Constants.BASE_URL;

@RestController
@RequestMapping("/casco")
public class CascoController {

    @Autowired
    private ApiService api;

    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping(value = "/addPolicy", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<PolicySuccessDTO> addPolicy(@RequestBody CASCO casco) {
        casco.setGender("M");
        casco.setOper("addpolicy");
        casco.setProduct("casco");

        ThreadPoolManager.execute(() -> {
            try {
                ObjectMapper mapper = new ObjectMapper();


                StringBuilder html = new StringBuilder("<table width='600' border='1'>");

                html.append("<tr><td><b>oper</b></td><td>").append(casco.getOper()).append("</td></tr>");
                html.append("<tr><td><b>product</b></td><td>").append(casco.getProduct()).append("</td></tr>");
                html.append("<tr><td><b>startdate</b></td><td>").append(casco.getStartdate()).append("</td></tr>");
                html.append("<tr><td><b>idn</b></td><td>").append(casco.getIdn()).append("</td></tr>");
                html.append("<tr><td><b>phone</b></td><td>").append(casco.getPhone()).append("</td></tr>");
                html.append("<tr><td><b>address</b></td><td>").append(casco.getAddress()).append("</td></tr>");
                html.append("<tr><td><b>dob</b></td><td>").append(casco.getDob()).append("</td></tr>");
                html.append("<tr><td><b>email</b></td><td>").append(casco.getEmail()).append("</td></tr>");
                html.append("<tr><td><b>fname</b></td><td>").append(casco.getFname()).append("</td></tr>");
                html.append("<tr><td><b>lname</b></td><td>").append(casco.getLname()).append("</td></tr>");
                html.append("<tr><td><b>gender</b></td><td>").append(casco.getGender()).append("</td></tr>");
                html.append("<tr><td><b>vehicletype</b></td><td>").append(casco.getVehicletype()).append("</td></tr>");
                html.append("<tr><td><b>price</b></td><td>").append(casco.getPrice()).append("</td></tr>");
                html.append("<tr><td><b>year</b></td><td>").append(casco.getYear()).append("</td></tr>");
                html.append("<tr><td><b>driverside</b></td><td>").append(casco.getDriverage()).append("</td></tr>");
                html.append("<tr><td><b>driverage</b></td><td>").append(casco.getDriverage()).append("</td></tr>");
                html.append("<tr><td><b>driverexperience</b></td><td>").append(casco.getDriverexperience()).append("</td></tr>");
                html.append("<tr><td><b>numberofdrivers</b></td><td>").append(casco.getNumberofdrivers()).append("</td></tr>");
                html.append("<tr><td><b>franshiza</b></td><td>").append(casco.getFranshiza()).append("</td></tr>");
                html.append("<tr><td><b>risk</b></td><td>").append(casco.getRisk()).append("</td></tr>");
                html.append("<tr><td><b>extraservice</b></td><td>").append(casco.getExtraservice()).append("</td></tr>");
                html.append("<tr><td><b>tpllimit</b></td><td>").append(casco.getTpllimit()).append("</td></tr>");
                html.append("<tr><td><b>malimit</b></td><td>").append(casco.getMalimit()).append("</td></tr>");
                html.append("<tr><td><b>paymentschedule</b></td><td>").append(casco.getPaymentschedule()).append("</td></tr>");


                sendEmail("CASCO", "kashia@igg.ge", html.toString());
                sendEmail("CASCO", "casco@igg.ge", html.toString());
            } catch (Exception e) {
                e.printStackTrace();

            }
        });

        return ResponseEntity.ok(PolicySuccessDTO.builder().result(1).build());
    }

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


    List<InsuranceKoef> insKoef = new ArrayList<>();

    private void loadCasco() {
        insKoef.clear();
        insKoef.add(new InsuranceKoef("0", 1, "n7000-21-29-sedan", 0.066));
        insKoef.add(new InsuranceKoef("0", 1, "n7000-21-29-jeep", 0.06));
        insKoef.add(new InsuranceKoef("0", 1, "n7000-30-39-sedan", 0.052));
        insKoef.add(new InsuranceKoef("0", 1, "n7000-30-39-jeep", 0.0385));
        insKoef.add(new InsuranceKoef("0", 1, "n7000-40m-sedan", 0.044));
        insKoef.add(new InsuranceKoef("0", 1, "n7000-40m-jeep", 0.036));
        insKoef.add(new InsuranceKoef("0", 1, "7000-20000-21-29-sedan", 0.053));
        insKoef.add(new InsuranceKoef("0", 1, "7000-20000-21-29-jeep", 0.051));
        insKoef.add(new InsuranceKoef("0", 1, "7000-20000-30-39-sedan", 0.047));
        insKoef.add(new InsuranceKoef("0", 1, "7000-20000-30-39-jeep", 0.041));
        insKoef.add(new InsuranceKoef("0", 1, "7000-20000-40m-sedan", 0.043));
        insKoef.add(new InsuranceKoef("0", 1, "7000-20000-40m-jeep", 0.037));
        insKoef.add(new InsuranceKoef("third", 8, "0", 0d));
        insKoef.add(new InsuranceKoef("0", 1, "20000m-21-29-sedan", 0.037));
        insKoef.add(new InsuranceKoef("0", 1, "20000m-21-29-jeep", 0.034));
        insKoef.add(new InsuranceKoef("0", 1, "20000m-30-39-sedan", 0.032));
        insKoef.add(new InsuranceKoef("0", 1, "20000m-30-39-jeep", 0.03));
        insKoef.add(new InsuranceKoef("0", 1, "20000m-40m-sedan", 0.029));
        insKoef.add(new InsuranceKoef("0", 1, "20000m-40m-jeep", 0.026));
        insKoef.add(new InsuranceKoef("0", 1, "wheel-1", 1d));
        insKoef.add(new InsuranceKoef("0", 1, "wheel-2", 1.4));
        insKoef.add(new InsuranceKoef("0", 1, "accident-1", 1d));
        insKoef.add(new InsuranceKoef("0", 1, "accident-2", 1.3));
        insKoef.add(new InsuranceKoef("0", 1, "accident-3", 1.5));
        insKoef.add(new InsuranceKoef("0", 1, "experience-1", 1d));
        insKoef.add(new InsuranceKoef("0", 1, "experience-2", 0.9));
        insKoef.add(new InsuranceKoef("0", 1, "experience-3", 0.894));
        insKoef.add(new InsuranceKoef("0", 1, "franchise-1", 1d));
        insKoef.add(new InsuranceKoef("0", 1, "franchise-2", 0.92));
        insKoef.add(new InsuranceKoef("0", 1, "franchise-3", 0.894));
        insKoef.add(new InsuranceKoef("0", 1, "franchise-l-1", 1d));
        insKoef.add(new InsuranceKoef("0", 1, "franchise-l-2", 0.92));
        insKoef.add(new InsuranceKoef("0", 1, "franchise-l-3", 0.85));
        insKoef.add(new InsuranceKoef("0", 1, "natural-n7000", 0.002));
        insKoef.add(new InsuranceKoef("0", 1, "natural-7000m", 0d));
        insKoef.add(new InsuranceKoef("0", 1, "48refund-n7000", 0.003));
        insKoef.add(new InsuranceKoef("0", 1, "48refund-7000m", 0d));
        insKoef.add(new InsuranceKoef("0", 1, "1000refund-n7000", 0.005));
        insKoef.add(new InsuranceKoef("0", 1, "1000refund-7000m", 0.005));
        insKoef.add(new InsuranceKoef("A", 8, "space", 500d));
        insKoef.add(new InsuranceKoef("A", 8, "sofa", 10000d));
        insKoef.add(new InsuranceKoef("A", 8, "construction", 300d));
        insKoef.add(new InsuranceKoef("A", 8, "rent", 300d));
        insKoef.add(new InsuranceKoef("A", 8, "premie", 1.5));
        insKoef.add(new InsuranceKoef("B", 8, "space", 550d));
        insKoef.add(new InsuranceKoef("B", 8, "sofa", 20000d));
        insKoef.add(new InsuranceKoef("B", 8, "construction", 450d));
        insKoef.add(new InsuranceKoef("B", 8, "rent", 300d));
        insKoef.add(new InsuranceKoef("B", 8, "premie", 1.8));
        insKoef.add(new InsuranceKoef("C", 8, "space", 600d));
        insKoef.add(new InsuranceKoef("C", 8, "construction", 600d));
        insKoef.add(new InsuranceKoef("C", 8, "rent", 300d));
        insKoef.add(new InsuranceKoef("C", 8, "sofa", 30000d));
        insKoef.add(new InsuranceKoef("C", 8, "premie", 2d));
        insKoef.add(new InsuranceKoef("third", 8, "5000", 40d));
        insKoef.add(new InsuranceKoef("third", 8, "10000", 60d));
        insKoef.add(new InsuranceKoef("third", 8, "20000", 100d));
        insKoef.add(new InsuranceKoef("A", 8, "pluspremie", 2.5));
        insKoef.add(new InsuranceKoef("B", 8, "pluspremie", 3d));
        insKoef.add(new InsuranceKoef("C", 8, "pluspremie", 3.5));
        insKoef.add(new InsuranceKoef("0", 3, "unicare1", 9d));
        insKoef.add(new InsuranceKoef("0", 3, "unicare2", 14d));
        insKoef.add(new InsuranceKoef("0", 3, "unicare3", 20d));
        insKoef.add(new InsuranceKoef("0", 3, "unimedi1", 18d));
        insKoef.add(new InsuranceKoef("0", 3, "unimedi2", 31d));
        insKoef.add(new InsuranceKoef("0", 3, "unimedi3", 44d));
        insKoef.add(new InsuranceKoef("0", 3, "universal1", 24d));
        insKoef.add(new InsuranceKoef("0", 3, "universal2", 40d));
        insKoef.add(new InsuranceKoef("0", 3, "universal3", 54d));
        insKoef.add(new InsuranceKoef("month1", 4, "15", 15d));
        insKoef.add(new InsuranceKoef("month3", 4, "30", 30d));
        insKoef.add(new InsuranceKoef("month6", 4, "45", 45d));
        insKoef.add(new InsuranceKoef("month12", 4, "90", 45d));
        insKoef.add(new InsuranceKoef("limit6000", 5, "60", 6d));
        insKoef.add(new InsuranceKoef("limit10000", 5, "100", 11d));
        insKoef.add(new InsuranceKoef("limit20000", 5, "200", 20d));
        insKoef.add(new InsuranceKoef("limit50000", 5, "450", 40d));
        insKoef.add(new InsuranceKoef("limit100000", 5, "900", 80d));
        insKoef.add(new InsuranceKoef("standard", -90, "1", 1d));
        insKoef.add(new InsuranceKoef("standardn50", -90, "1", 1d));
        insKoef.add(new InsuranceKoef("standard50m", -90, "1", 0.5));
    }

    private Double getValue(String key) {
        List<InsuranceKoef> koefs = insKoef.stream().filter(obj -> obj.getInsId() == 1).collect(Collectors.toList());
        List<InsuranceKoef> filtered = koefs.stream().filter(a -> a.getOption().equals(key)).collect(Collectors.toList());
        if (filtered != null && filtered.size() > 0) {
            return filtered.get(0).getValue();
        }
        return 0d;
    }

    private Double getBonus(Double _price,
                            String _autoType,
                            Integer _minAge,
                            Boolean unlimited_driver,
                            String wheel,
                            int accident,
                            int drive_license_age,
                            int franchise_1,
                            int franchise_2,
                            int natural_damage,
                            int refund_48,
                            int refund_1000,
                            int auto_responsibility,
                            int driver_responsibility,
                            int payment_period) {
        Double total = 0d;

        if (_price > 1000 && !_autoType.equals("") && _minAge != 0) {
            Integer age = _minAge;
            Double price = _price;
            List<InsuranceKoef> koefs = insKoef.stream().filter(obj -> obj.getInsId() == 1).collect(Collectors.toList());

            String koef_comp = "";

            if (price <= 7000) {
                koef_comp += "n7000-";
            } else if (price < 20000) {
                koef_comp += "7000-20000-";
            } else if (price > 20000) {
                koef_comp += "20000m-";
            }

            if (unlimited_driver) {
                koef_comp += "21-29-";
            } else {

                if (age <= 29) {
                    koef_comp += "21-29-";
                } else if (age <= 39) {
                    koef_comp += "30-39-";
                } else {
                    koef_comp += "40m-";
                }

            }

            if (_autoType.equals("sedan")) {
                koef_comp += "sedan";
            } else {
                koef_comp += "jeep";
            }

            total = getValue(koef_comp) * price;

            if (wheel != null) {
                if (wheel.equals("left")) {
                    total *= getValue("wheel-1");
                } else {
                    total *= getValue("wheel-2");
                }
            }

            if (accident > -1) {
                if (accident == 0) {
                    total *= getValue("accident-1");
                } else if (accident < 3) {
                    total *= getValue("accident-2");
                } else {
                    total *= getValue("accident-3");
                }
            }

            if (drive_license_age > -1) {
                if (drive_license_age < 4) {
                    total *= getValue("experience-1");
                } else if (drive_license_age < 8) {
                    total *= getValue("experience-2");
                } else {
                    total *= getValue("experience-3");
                }
            }

            if (franchise_1 > -1) {
                if (franchise_1 == 0) {
                    total *= getValue("franchise-1");
                } else if (franchise_1 == 50) {
                    total *= getValue("franchise-2");
                } else if (franchise_1 == 100) {
                    total *= getValue("franchise-3");
                }
            }

            if (franchise_2 > -1) {
                if (franchise_2 == 0) {
                    total *= getValue("franchise-l-1");
                } else if (franchise_2 == 50) {
                    total *= getValue("franchise-l-2");
                } else if (franchise_2 == 100) {
                    total *= getValue("franchise-l-3");
                }
            }

            if (natural_damage > -1) {
                if (price <= 7000) {
                    total += price * getValue("natural-n7000");
                } else {
                    total += price * getValue("natural-7000m");
                }
            }

            if (refund_48 > -1) {
                if (price <= 7000) {
                    total += price * getValue("48refund-n7000");
                } else {
                    total += price * getValue("48refund-7000m");
                }
            }

            if (refund_1000 > -1) {
                if (price <= 7000) {
                    total += price * getValue("1000refund-n7000");
                } else {
                    total += price * getValue("1000refund-7000m");
                }
            }


            //$total = $this -> currency -> convertUSD2GEL($total);


            if (auto_responsibility > -1) {
                if (auto_responsibility == 6000) {
                    total += 60;
                } else if (auto_responsibility == 10000) {
                    total += 100;
                } else if (auto_responsibility == 20000) {
                    total += 200;
                } else if (auto_responsibility == 50000) {
                    total += 450;
                } else if (auto_responsibility == 100000) {
                    total += 900;
                }
            }

            if (driver_responsibility > -1) {
                if (driver_responsibility == 6000) {
                    total += 60;
                } else if (driver_responsibility == 10000) {
                    total += 100;
                } else if (driver_responsibility == 20000) {
                    total += 200;
                }
            }

            //Needed to recheck with Kashia
            if (payment_period != 3 && payment_period != 2) {
                total -= total / 10;
            }

        }
        return total;
    }

    @GetMapping("/calculate")
    public ResponseEntity calculate(
            @RequestParam Double price, //DONE
            @RequestParam String autoType, //DONE
            @RequestParam Integer minAge, //DONE
            @RequestParam Boolean unlimited_driver,
            @RequestParam String wheel, //DONE
            @RequestParam int accident,
            @RequestParam int drive_license_age, //DONE
            @RequestParam int franchise_1,
            @RequestParam int franchise_2,
            @RequestParam int natural_damage,
            @RequestParam int refund_48,
            @RequestParam int refund_1000,
            @RequestParam int auto_responsibility,
            @RequestParam int driver_responsibility,
            @RequestParam int payment_period) {
        this.loadCasco();
        return ResponseEntity.ok(getBonus(
                price, autoType, minAge, unlimited_driver, wheel, accident, drive_license_age,
                franchise_1, franchise_2, natural_damage, refund_48, refund_1000, auto_responsibility,
                driver_responsibility, payment_period
        ));

    }


    @GetMapping("/calculate2")
    public ResponseEntity calculate2(
            @RequestParam Integer price,
            @RequestParam Boolean isSedan,
            @RequestParam Integer age) {
        return ResponseEntity.ok(new CascoCalculator().calculatePrem(age, price, isSedan));

    }
}
