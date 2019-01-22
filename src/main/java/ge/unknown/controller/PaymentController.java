package ge.unknown.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import ge.unknown.DTO.PolicySuccessDTO;
import ge.unknown.service.ApiService;
import ge.unknown.service.StorageService;
import ge.unknown.thread.ThreadPoolManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private ApiService api;

    @Autowired
    private StorageService storageService;

    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping(value = "/success",
            method = RequestMethod.POST,
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8"}
    )
    public void success(HttpServletResponse response, @RequestParam(name = "trans_id") String trans_id) {
        try {
            System.out.println("TRANSACTION ID RECEIVED ------ " + trans_id);
            if (trans_id != null && !trans_id.equals("")) {

                HashMap map = api.get_transaction_result(trans_id);
                System.out.println("map ------ " + map);

                if (map.containsKey("success") && map.get("success").equals("true")) {

//                    "RESULT": "FAILED",
//                    "RESULT_CODE": "102",

                    LinkedTreeMap obj = (LinkedTreeMap) map.get("result");

                    if (obj.containsKey("error") || obj.toString().contains("FAILED")) {

                        System.out.println("BANK PAYMENT FAILED WITH ------ " + obj);
                        response.sendRedirect("https://iggprod.com/?page=failure");

                    } else if (obj.toString().contains("000")) {


                        trans_id = trans_id.replaceAll("/", "_");
                        System.out.println("TRUNCATED ID ------ " + trans_id);
                        String path = storageService.getRootLocation().toAbsolutePath().toString() + File.separator + "payments" + File.separator + trans_id;
                        System.out.println("TRUNCATED PATH ------ " + path);
                        File f = new File(path);
                        if (f.exists()) {
                            System.out.println("FILE FOUND ON PATH ------ " + path);
                            StringBuilder json = new StringBuilder();
                            for (String line : Files.readAllLines(Paths.get(path))) {
                                json.append(line);
                            }
                            System.out.println("JSON ON PATH ------ " + json);
                            if (!json.toString().equals("")) {
                                PolicySuccessDTO dto = api.sendJson(json.toString());
                                if (dto != null && dto.getPolicy() != null) {

                                    try {
                                        GsonBuilder builder = new GsonBuilder();
                                        Gson gson = builder.serializeNulls().create();
                                        HashMap m = gson.fromJson(json.toString(), HashMap.class);

                                        if (m.containsKey("email") && m.get("email") != null && m.get("email").toString().contains("@") && m.get("email").toString().contains(".")) {
                                            ThreadPoolManager.execute(() -> {

                                                String email = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\"><head>\n" +
                                                        "    <!--[if gte mso 9]><xml>\n" +
                                                        "     <o:OfficeDocumentSettings>\n" +
                                                        "      <o:AllowPNG/>\n" +
                                                        "      <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                                                        "     </o:OfficeDocumentSettings>\n" +
                                                        "    </xml><![endif]-->\n" +
                                                        "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
                                                        "    <meta name=\"viewport\" content=\"width=device-width\">\n" +
                                                        "    <!--[if !mso]><!--><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->\n" +
                                                        "    <title></title>\n" +
                                                        "    <!--[if !mso]><!-- -->\n" +
                                                        "\t<link href=\"https://fonts.googleapis.com/css?family=Montserrat\" rel=\"stylesheet\" type=\"text/css\">\n" +
                                                        "\t<!--<![endif]-->\n" +
                                                        "    \n" +
                                                        "    <style type=\"text/css\" id=\"media-query\">\n" +
                                                        "      body {\n" +
                                                        "  margin: 0;\n" +
                                                        "  padding: 0; }\n" +
                                                        "\n" +
                                                        "table, tr, td {\n" +
                                                        "  vertical-align: top;\n" +
                                                        "  border-collapse: collapse; }\n" +
                                                        "\n" +
                                                        ".ie-browser table, .mso-container table {\n" +
                                                        "  table-layout: fixed; }\n" +
                                                        "\n" +
                                                        "* {\n" +
                                                        "  line-height: inherit; }\n" +
                                                        "\n" +
                                                        "a[x-apple-data-detectors=true] {\n" +
                                                        "  color: inherit !important;\n" +
                                                        "  text-decoration: none !important; }\n" +
                                                        "\n" +
                                                        "[owa] .img-container div, [owa] .img-container button {\n" +
                                                        "  display: block !important; }\n" +
                                                        "\n" +
                                                        "[owa] .fullwidth button {\n" +
                                                        "  width: 100% !important; }\n" +
                                                        "\n" +
                                                        "[owa] .block-grid .col {\n" +
                                                        "  display: table-cell;\n" +
                                                        "  float: none !important;\n" +
                                                        "  vertical-align: top; }\n" +
                                                        "\n" +
                                                        ".ie-browser .num12, .ie-browser .block-grid, [owa] .num12, [owa] .block-grid {\n" +
                                                        "  width: 600px !important; }\n" +
                                                        "\n" +
                                                        ".ExternalClass, .ExternalClass p, .ExternalClass span, .ExternalClass font, .ExternalClass td, .ExternalClass div {\n" +
                                                        "  line-height: 100%; }\n" +
                                                        "\n" +
                                                        ".ie-browser .mixed-two-up .num4, [owa] .mixed-two-up .num4 {\n" +
                                                        "  width: 200px !important; }\n" +
                                                        "\n" +
                                                        ".ie-browser .mixed-two-up .num8, [owa] .mixed-two-up .num8 {\n" +
                                                        "  width: 400px !important; }\n" +
                                                        "\n" +
                                                        ".ie-browser .block-grid.two-up .col, [owa] .block-grid.two-up .col {\n" +
                                                        "  width: 300px !important; }\n" +
                                                        "\n" +
                                                        ".ie-browser .block-grid.three-up .col, [owa] .block-grid.three-up .col {\n" +
                                                        "  width: 200px !important; }\n" +
                                                        "\n" +
                                                        ".ie-browser .block-grid.four-up .col, [owa] .block-grid.four-up .col {\n" +
                                                        "  width: 150px !important; }\n" +
                                                        "\n" +
                                                        ".ie-browser .block-grid.five-up .col, [owa] .block-grid.five-up .col {\n" +
                                                        "  width: 120px !important; }\n" +
                                                        "\n" +
                                                        ".ie-browser .block-grid.six-up .col, [owa] .block-grid.six-up .col {\n" +
                                                        "  width: 100px !important; }\n" +
                                                        "\n" +
                                                        ".ie-browser .block-grid.seven-up .col, [owa] .block-grid.seven-up .col {\n" +
                                                        "  width: 85px !important; }\n" +
                                                        "\n" +
                                                        ".ie-browser .block-grid.eight-up .col, [owa] .block-grid.eight-up .col {\n" +
                                                        "  width: 75px !important; }\n" +
                                                        "\n" +
                                                        ".ie-browser .block-grid.nine-up .col, [owa] .block-grid.nine-up .col {\n" +
                                                        "  width: 66px !important; }\n" +
                                                        "\n" +
                                                        ".ie-browser .block-grid.ten-up .col, [owa] .block-grid.ten-up .col {\n" +
                                                        "  width: 60px !important; }\n" +
                                                        "\n" +
                                                        ".ie-browser .block-grid.eleven-up .col, [owa] .block-grid.eleven-up .col {\n" +
                                                        "  width: 54px !important; }\n" +
                                                        "\n" +
                                                        ".ie-browser .block-grid.twelve-up .col, [owa] .block-grid.twelve-up .col {\n" +
                                                        "  width: 50px !important; }\n" +
                                                        "\n" +
                                                        "@media only screen and (min-width: 620px) {\n" +
                                                        "  .block-grid {\n" +
                                                        "    width: 600px !important; }\n" +
                                                        "  .block-grid .col {\n" +
                                                        "    vertical-align: top; }\n" +
                                                        "    .block-grid .col.num12 {\n" +
                                                        "      width: 600px !important; }\n" +
                                                        "  .block-grid.mixed-two-up .col.num4 {\n" +
                                                        "    width: 200px !important; }\n" +
                                                        "  .block-grid.mixed-two-up .col.num8 {\n" +
                                                        "    width: 400px !important; }\n" +
                                                        "  .block-grid.two-up .col {\n" +
                                                        "    width: 300px !important; }\n" +
                                                        "  .block-grid.three-up .col {\n" +
                                                        "    width: 200px !important; }\n" +
                                                        "  .block-grid.four-up .col {\n" +
                                                        "    width: 150px !important; }\n" +
                                                        "  .block-grid.five-up .col {\n" +
                                                        "    width: 120px !important; }\n" +
                                                        "  .block-grid.six-up .col {\n" +
                                                        "    width: 100px !important; }\n" +
                                                        "  .block-grid.seven-up .col {\n" +
                                                        "    width: 85px !important; }\n" +
                                                        "  .block-grid.eight-up .col {\n" +
                                                        "    width: 75px !important; }\n" +
                                                        "  .block-grid.nine-up .col {\n" +
                                                        "    width: 66px !important; }\n" +
                                                        "  .block-grid.ten-up .col {\n" +
                                                        "    width: 60px !important; }\n" +
                                                        "  .block-grid.eleven-up .col {\n" +
                                                        "    width: 54px !important; }\n" +
                                                        "  .block-grid.twelve-up .col {\n" +
                                                        "    width: 50px !important; } }\n" +
                                                        "\n" +
                                                        "@media (max-width: 620px) {\n" +
                                                        "  .block-grid, .col {\n" +
                                                        "    min-width: 320px !important;\n" +
                                                        "    max-width: 100% !important;\n" +
                                                        "    display: block !important; }\n" +
                                                        "  .block-grid {\n" +
                                                        "    width: calc(100% - 40px) !important; }\n" +
                                                        "  .col {\n" +
                                                        "    width: 100% !important; }\n" +
                                                        "    .col > div {\n" +
                                                        "      margin: 0 auto; }\n" +
                                                        "  img.fullwidth, img.fullwidthOnMobile {\n" +
                                                        "    max-width: 100% !important; }\n" +
                                                        "  .no-stack .col {\n" +
                                                        "    min-width: 0 !important;\n" +
                                                        "    display: table-cell !important; }\n" +
                                                        "  .no-stack.two-up .col {\n" +
                                                        "    width: 50% !important; }\n" +
                                                        "  .no-stack.mixed-two-up .col.num4 {\n" +
                                                        "    width: 33% !important; }\n" +
                                                        "  .no-stack.mixed-two-up .col.num8 {\n" +
                                                        "    width: 66% !important; }\n" +
                                                        "  .no-stack.three-up .col.num4 {\n" +
                                                        "    width: 33% !important; }\n" +
                                                        "  .no-stack.four-up .col.num3 {\n" +
                                                        "    width: 25% !important; }\n" +
                                                        "  .mobile_hide {\n" +
                                                        "    min-height: 0px;\n" +
                                                        "    max-height: 0px;\n" +
                                                        "    max-width: 0px;\n" +
                                                        "    display: none;\n" +
                                                        "    overflow: hidden;\n" +
                                                        "    font-size: 0px; } }\n" +
                                                        "\n" +
                                                        "    </style>\n" +
                                                        "</head>\n" +
                                                        "<body class=\"clean-body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #e2eace\">\n" +
                                                        "  <style type=\"text/css\" id=\"media-query-bodytag\">\n" +
                                                        "    @media (max-width: 520px) {\n" +
                                                        "      .block-grid {\n" +
                                                        "        min-width: 320px!important;\n" +
                                                        "        max-width: 100%!important;\n" +
                                                        "        width: 100%!important;\n" +
                                                        "        display: block!important;\n" +
                                                        "      }\n" +
                                                        "\n" +
                                                        "      .col {\n" +
                                                        "        min-width: 320px!important;\n" +
                                                        "        max-width: 100%!important;\n" +
                                                        "        width: 100%!important;\n" +
                                                        "        display: block!important;\n" +
                                                        "      }\n" +
                                                        "\n" +
                                                        "        .col > div {\n" +
                                                        "          margin: 0 auto;\n" +
                                                        "        }\n" +
                                                        "\n" +
                                                        "      img.fullwidth {\n" +
                                                        "        max-width: 100%!important;\n" +
                                                        "      }\n" +
                                                        "\t\t\timg.fullwidthOnMobile {\n" +
                                                        "        max-width: 100%!important;\n" +
                                                        "      }\n" +
                                                        "      .no-stack .col {\n" +
                                                        "\t\t\t\tmin-width: 0!important;\n" +
                                                        "\t\t\t\tdisplay: table-cell!important;\n" +
                                                        "\t\t\t}\n" +
                                                        "\t\t\t.no-stack.two-up .col {\n" +
                                                        "\t\t\t\twidth: 50%!important;\n" +
                                                        "\t\t\t}\n" +
                                                        "\t\t\t.no-stack.mixed-two-up .col.num4 {\n" +
                                                        "\t\t\t\twidth: 33%!important;\n" +
                                                        "\t\t\t}\n" +
                                                        "\t\t\t.no-stack.mixed-two-up .col.num8 {\n" +
                                                        "\t\t\t\twidth: 66%!important;\n" +
                                                        "\t\t\t}\n" +
                                                        "\t\t\t.no-stack.three-up .col.num4 {\n" +
                                                        "\t\t\t\twidth: 33%!important;\n" +
                                                        "\t\t\t}\n" +
                                                        "\t\t\t.no-stack.four-up .col.num3 {\n" +
                                                        "\t\t\t\twidth: 25%!important;\n" +
                                                        "\t\t\t}\n" +
                                                        "      .mobile_hide {\n" +
                                                        "        min-height: 0px!important;\n" +
                                                        "        max-height: 0px!important;\n" +
                                                        "        max-width: 0px!important;\n" +
                                                        "        display: none!important;\n" +
                                                        "        overflow: hidden!important;\n" +
                                                        "        font-size: 0px!important;\n" +
                                                        "      }\n" +
                                                        "    }\n" +
                                                        "  </style>\n" +
                                                        "  <!--[if IE]><div class=\"ie-browser\"><![endif]-->\n" +
                                                        "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\n" +
                                                        "  <table class=\"nl-container\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #e2eace;width: 100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                                                        "\t<tbody>\n" +
                                                        "\t<tr style=\"vertical-align: top\">\n" +
                                                        "\t\t<td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\n" +
                                                        "    <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #e2eace;\"><![endif]-->\n" +
                                                        "\n" +
                                                        "    <div style=\"background-color:transparent;\">\n" +
                                                        "      <div style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\" class=\"block-grid \">\n" +
                                                        "        <div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                                                        "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"background-color:transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 600px;\"><tr class=\"layout-full-width\" style=\"background-color:transparent;\"><![endif]-->\n" +
                                                        "\n" +
                                                        "              <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\" width:600px; padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:0px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                                                        "            <div class=\"col num12\" style=\"min-width: 320px;max-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                                                        "              <div style=\"background-color: transparent; width: 100% !important;\">\n" +
                                                        "              <!--[if (!mso)&(!IE)]><!--><div style=\"border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent; padding-top:5px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\"><!--<![endif]-->\n" +
                                                        "\n" +
                                                        "                  \n" +
                                                        "                    <div align=\"center\" class=\"img-container center  autowidth  fullwidth \" style=\"padding-right: 0px;  padding-left: 0px;\">\n" +
                                                        "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px;line-height:0px;\"><td style=\"padding-right: 0px; padding-left: 0px;\" align=\"center\"><![endif]-->\n" +
                                                        "<div style=\"line-height:25px;font-size:1px\">&nbsp;</div>  <img class=\"center  autowidth  fullwidth\" align=\"center\" border=\"0\" src=\"https://d1oco4z2z1fhwp.cloudfront.net/templates/default/20/rounder-up.png\" alt=\"Image\" title=\"Image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: block !important;border: 0;height: auto;float: none;width: 100%;max-width: 600px\" width=\"600\">\n" +
                                                        "<!--[if mso]></td></tr></table><![endif]-->\n" +
                                                        "</div>\n" +
                                                        "\n" +
                                                        "                  \n" +
                                                        "              <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                                                        "              </div>\n" +
                                                        "            </div>\n" +
                                                        "          <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                                                        "        </div>\n" +
                                                        "      </div>\n" +
                                                        "    </div>\n" +
                                                        "    <div style=\"background-color:transparent;\">\n" +
                                                        "      <div style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #FFFFFF;\" class=\"block-grid \">\n" +
                                                        "        <div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#FFFFFF;\">\n" +
                                                        "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"background-color:transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 600px;\"><tr class=\"layout-full-width\" style=\"background-color:#FFFFFF;\"><![endif]-->\n" +
                                                        "\n" +
                                                        "              <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\" width:600px; padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                                                        "            <div class=\"col num12\" style=\"min-width: 320px;max-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                                                        "              <div style=\"background-color: transparent; width: 100% !important;\">\n" +
                                                        "              <!--[if (!mso)&(!IE)]><!--><div style=\"border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\"><!--<![endif]-->\n" +
                                                        "\n" +
                                                        "                  \n" +
                                                        "                    <div align=\"center\" class=\"img-container center  autowidth  \" style=\"padding-right: 0px;  padding-left: 0px;\">\n" +
                                                        "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px;line-height:0px;\"><td style=\"padding-right: 0px; padding-left: 0px;\" align=\"center\"><![endif]-->\n" +
                                                        "  <img class=\"center  autowidth \" align=\"center\" border=\"0\" src=\"http://igg.ge/assets/img/logo-ka.png\" alt=\"Image\" title=\"Image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: block !important;border: 0;height: auto;float: none;width: 100%;max-width: 323px\" width=\"323\">\n" +
                                                        "<!--[if mso]></td></tr></table><![endif]-->\n" +
                                                        "</div>\n" +
                                                        "\n" +
                                                        "                  \n" +
                                                        "                  \n" +
                                                        "                    <div class=\"\">\n" +
                                                        "\t<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px;\"><![endif]-->\n" +
                                                        "\t<div style=\"color:#555555;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:150%; padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px;\">\t\n" +
                                                        "\t\t<div style=\"font-size:12px;line-height:18px;color:#555555;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 14px;line-height: 21px;text-align: center\">თქვენ წარმატებით შეიძინეთ პოლისი</p></div>\t\n" +
                                                        "\t</div>\n" +
                                                        "\t<!--[if mso]></td></tr></table><![endif]-->\n" +
                                                        "</div>\n" +
                                                        "                  \n" +
                                                        "              <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                                                        "              </div>\n" +
                                                        "            </div>\n" +
                                                        "          <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                                                        "        </div>\n" +
                                                        "      </div>\n" +
                                                        "    </div>\n" +
                                                        "    <div style=\"background-color:transparent;\">\n" +
                                                        "      <div style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #FFFFFF;\" class=\"block-grid \">\n" +
                                                        "        <div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#FFFFFF;\">\n" +
                                                        "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"background-color:transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 600px;\"><tr class=\"layout-full-width\" style=\"background-color:#FFFFFF;\"><![endif]-->\n" +
                                                        "\n" +
                                                        "              <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\" width:600px; padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:5px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                                                        "            <div class=\"col num12\" style=\"min-width: 320px;max-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                                                        "              <div style=\"background-color: transparent; width: 100% !important;\">\n" +
                                                        "              <!--[if (!mso)&(!IE)]><!--><div style=\"border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent; padding-top:0px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\"><!--<![endif]-->\n" +
                                                        "\n" +
                                                        "                  \n" +
                                                        "                    <div class=\"\">\n" +
                                                        "\t<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px;\"><![endif]-->\n" +
                                                        "\t<div style=\"color:#0D0D0D;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:120%; padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px;\">\t\n" +
                                                        "\t\t<div style=\"line-height:14px;font-size:12px;color:#0D0D0D;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;text-align:left;\"><p style=\"margin: 0;line-height: 14px;text-align: center;font-size: 12px\">საქართველოს სადაზღვევო ჯგუფი © 2017. <a style=\"color:#0068A5;text-decoration: underline;\" title=\"Powered By AppLoad\" href=\"https://appload.ge\" target=\"_blank\" rel=\"noopener\">Powered By AppLoad</a></p></div>\t\n" +
                                                        "\t</div>\n" +
                                                        "\t<!--[if mso]></td></tr></table><![endif]-->\n" +
                                                        "</div>\n" +
                                                        "                  \n" +
                                                        "                  \n" +
                                                        "                    <div class=\"\">\n" +
                                                        "\t<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px;\"><![endif]-->\n" +
                                                        "\t<div style=\"color:#555555;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:150%; padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px;\">\t\n" +
                                                        "\t\t<div style=\"font-size:12px;line-height:18px;color:#555555;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 14px;line-height: 21px;text-align: center\"><span style=\"color: rgb(168, 191, 111); font-size: 14px; line-height: 21px;\"><strong>მადლობას გიხდით ჩვენთან თანამშრომლობისთვის</strong></span></p></div>\t\n" +
                                                        "\t</div>\n" +
                                                        "\t<!--[if mso]></td></tr></table><![endif]-->\n" +
                                                        "</div>\n" +
                                                        "                  \n" +
                                                        "                  \n" +
                                                        "                    <div class=\"\">\n" +
                                                        "\t<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 20px; padding-bottom: 10px;\"><![endif]-->\n" +
                                                        "\t<div style=\"color:#0D0D0D;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:150%; padding-right: 10px; padding-left: 10px; padding-top: 20px; padding-bottom: 10px;\">\t\n" +
                                                        "\t\t<div style=\"font-size:12px;line-height:18px;color:#0D0D0D;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 14px;line-height: 21px;text-align: center\">პოლისის გადმოსაწერად დააჭირეთ ბმულს</p></div>\t\n" +
                                                        "\t</div>\n" +
                                                        "\t<!--[if mso]></td></tr></table><![endif]-->\n" +
                                                        "</div>\n" +
                                                        "                  \n" +
                                                        "                  \n" +
                                                        "                    \n" +
                                                        "<a href='http://212.72.150.51:8080/ims/content/getpolicypdfonline.jsp?num=" + dto.getPolicy() + "'><div align=\"center\" class=\"button-container center \" style=\"padding-right: 10px; padding-left: 10px; padding-top:25px; padding-bottom:10px;\">\n" +
                                                        "  <!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top:25px; padding-bottom:10px;\" align=\"center\"><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"\" style=\"height:46pt; v-text-anchor:middle; width:94pt;\" arcsize=\"7%\" strokecolor=\"#412F87\" fillcolor=\"#412F87\"><w:anchorlock/><v:textbox inset=\"0,0,0,0\"><center style=\"color:#ffffff; font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; font-size:16px;\"><![endif]-->\n" +
                                                        "    <div style=\"color: #ffffff; background-color: #412F87; border-radius: 4px; -webkit-border-radius: 4px; -moz-border-radius: 4px; max-width: 126px; width: 96px;width: auto; border-top: 0px solid transparent; border-right: 0px solid transparent; border-bottom: 0px solid transparent; border-left: 0px solid transparent; padding-top: 15px; padding-right: 15px; padding-bottom: 15px; padding-left: 15px; font-family: 'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; text-align: center; mso-border-alt: none;\">\n" +
                                                        "      <span style=\"font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;font-size:16px;line-height:32px;\">" +
                                                        "გადმოწერა" +
                                                        "</span>\n" +
                                                        "    </div>\n" +
                                                        "  <!--[if mso]></center></v:textbox></v:roundrect></td></tr></table><![endif]-->\n" +
                                                        "</div></a>\n" +
                                                        "\n" +
                                                        "                  \n" +
                                                        "                  \n" +
                                                        "                    \n" +
                                                        "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"divider \" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 100%;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                                                        "    <tbody>\n" +
                                                        "        <tr style=\"vertical-align: top\">\n" +
                                                        "            <td class=\"divider_inner\" style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;padding-right: 10px;padding-left: 10px;padding-top: 30px;padding-bottom: 10px;min-width: 100%;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                                                        "                <table class=\"divider_content\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 0px solid transparent;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                                                        "                    <tbody>\n" +
                                                        "                        <tr style=\"vertical-align: top\">\n" +
                                                        "                            <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                                                        "                                <span></span>\n" +
                                                        "                            </td>\n" +
                                                        "                        </tr>\n" +
                                                        "                    </tbody>\n" +
                                                        "                </table>\n" +
                                                        "            </td>\n" +
                                                        "        </tr>\n" +
                                                        "    </tbody>\n" +
                                                        "</table>\n" +
                                                        "                  \n" +
                                                        "              <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                                                        "              </div>\n" +
                                                        "            </div>\n" +
                                                        "          <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                                                        "        </div>\n" +
                                                        "      </div>\n" +
                                                        "    </div>\n" +
                                                        "    <div style=\"background-color:transparent;\">\n" +
                                                        "      <div style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #525252;\" class=\"block-grid three-up \">\n" +
                                                        "        <div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#525252;\">\n" +
                                                        "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"background-color:transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 600px;\"><tr class=\"layout-full-width\" style=\"background-color:#525252;\"><![endif]-->\n" +
                                                        "\n" +
                                                        "              <!--[if (mso)|(IE)]><td align=\"center\" width=\"200\" style=\" width:200px; padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:0px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                                                        "            <div class=\"col num4\" style=\"max-width: 320px;min-width: 200px;display: table-cell;vertical-align: top;\">\n" +
                                                        "              <div style=\"background-color: transparent; width: 100% !important;\">\n" +
                                                        "              <!--[if (!mso)&(!IE)]><!--><div style=\"border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\"><!--<![endif]-->\n" +
                                                        "\n" +
                                                        "                  \n" +
                                                        "                    \n" +
                                                        "<div align=\"center\" style=\"padding-right: 0px; padding-left: 0px; padding-bottom: 0px;\" class=\"\">\n" +
                                                        "  <div style=\"line-height:15px;font-size:1px\">&nbsp;</div>\n" +
                                                        "  \n" +
                                                        "</div>\n" +
                                                        "                  \n" +
                                                        "              <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                                                        "              </div>\n" +
                                                        "            </div>\n" +
                                                        "              <!--[if (mso)|(IE)]></td><td align=\"center\" width=\"200\" style=\" width:200px; padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                                                        "            <div class=\"col num4\" style=\"max-width: 320px;min-width: 200px;display: table-cell;vertical-align: top;\">\n" +
                                                        "              <div style=\"background-color: transparent; width: 100% !important;\">\n" +
                                                        "              <!--[if (!mso)&(!IE)]><!--><div style=\"border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\"><!--<![endif]-->\n" +
                                                        "\n" +
                                                        "                  \n" +
                                                        "                    <div class=\"\">\n" +
                                                        "\t<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top: 20px; padding-bottom: 0px;\"><![endif]-->\n" +
                                                        "\t<div style=\"color:#a8bf6f;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:120%; padding-right: 0px; padding-left: 0px; padding-top: 20px; padding-bottom: 0px;\">\t\n" +
                                                        "\t\t<div style=\"font-size:12px;line-height:14px;color:#a8bf6f;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 12px;line-height: 14px;text-align: center\"><span style=\"color: rgb(255, 255, 255); font-size: 12px; line-height: 14px;\"><span style=\"font-size: 12px; line-height: 14px; color: rgb(168, 191, 111);\">Tel.:</span> (+995 32) 2 244 111</span></p></div>\t\n" +
                                                        "\t</div>\n" +
                                                        "\t<!--[if mso]></td></tr></table><![endif]-->\n" +
                                                        "</div>\n" +
                                                        "                  \n" +
                                                        "              <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                                                        "              </div>\n" +
                                                        "            </div>\n" +
                                                        "              <!--[if (mso)|(IE)]></td><td align=\"center\" width=\"200\" style=\" width:200px; padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                                                        "            <div class=\"col num4\" style=\"max-width: 320px;min-width: 200px;display: table-cell;vertical-align: top;\">\n" +
                                                        "              <div style=\"background-color: transparent; width: 100% !important;\">\n" +
                                                        "              <!--[if (!mso)&(!IE)]><!--><div style=\"border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\"><!--<![endif]-->\n" +
                                                        "\n" +
                                                        "                  \n" +
                                                        "                    <div class=\"\">\n" +
                                                        "\t<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top: 20px; padding-bottom: 0px;\"><![endif]-->\n" +
                                                        "\t<div style=\"color:#a8bf6f;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:120%; padding-right: 0px; padding-left: 0px; padding-top: 20px; padding-bottom: 0px;\">\t\n" +
                                                        "\t\t<div style=\"font-size:12px;line-height:14px;color:#a8bf6f;font-family:'Montserrat', 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;text-align:left;\"><p style=\"margin: 0;font-size: 12px;line-height: 14px;text-align: center\">Email info<span style=\"color: rgb(255, 255, 255); font-size: 12px; line-height: 14px;\">@igg.ge</span></p></div>\t\n" +
                                                        "\t</div>\n" +
                                                        "\t<!--[if mso]></td></tr></table><![endif]-->\n" +
                                                        "</div>\n" +
                                                        "                  \n" +
                                                        "              <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                                                        "              </div>\n" +
                                                        "            </div>\n" +
                                                        "          <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                                                        "        </div>\n" +
                                                        "      </div>\n" +
                                                        "    </div>\n" +
                                                        "    <div style=\"background-color:transparent;\">\n" +
                                                        "      <div style=\"Margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\" class=\"block-grid \">\n" +
                                                        "        <div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                                                        "          <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"background-color:transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 600px;\"><tr class=\"layout-full-width\" style=\"background-color:transparent;\"><![endif]-->\n" +
                                                        "\n" +
                                                        "              <!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\" width:600px; padding-right: 0px; padding-left: 0px; padding-top:0px; padding-bottom:5px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                                                        "            <div class=\"col num12\" style=\"min-width: 320px;max-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                                                        "              <div style=\"background-color: transparent; width: 100% !important;\">\n" +
                                                        "              <!--[if (!mso)&(!IE)]><!--><div style=\"border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent; padding-top:0px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\"><!--<![endif]-->\n" +
                                                        "\n" +
                                                        "                  \n" +
                                                        "                    <div align=\"center\" class=\"img-container center  autowidth  fullwidth \" style=\"padding-right: 0px;  padding-left: 0px;\">\n" +
                                                        "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px;line-height:0px;\"><td style=\"padding-right: 0px; padding-left: 0px;\" align=\"center\"><![endif]-->\n" +
                                                        "  <img class=\"center  autowidth  fullwidth\" align=\"center\" border=\"0\" src=\"https://d1oco4z2z1fhwp.cloudfront.net/templates/default/20/rounder-dwn.png\" alt=\"Image\" title=\"Image\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: block !important;border: 0;height: auto;float: none;width: 100%;max-width: 600px\" width=\"600\">\n" +
                                                        "<!--[if mso]></td></tr></table><![endif]-->\n" +
                                                        "</div>\n" +
                                                        "\n" +
                                                        "                  \n" +
                                                        "                  \n" +
                                                        "                    \n" +
                                                        "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" class=\"divider \" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 100%;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                                                        "    <tbody>\n" +
                                                        "        <tr style=\"vertical-align: top\">\n" +
                                                        "            <td class=\"divider_inner\" style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;padding-right: 30px;padding-left: 30px;padding-top: 30px;padding-bottom: 30px;min-width: 100%;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                                                        "                <table class=\"divider_content\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 0px solid transparent;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                                                        "                    <tbody>\n" +
                                                        "                        <tr style=\"vertical-align: top\">\n" +
                                                        "                            <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                                                        "                                <span></span>\n" +
                                                        "                            </td>\n" +
                                                        "                        </tr>\n" +
                                                        "                    </tbody>\n" +
                                                        "                </table>\n" +
                                                        "            </td>\n" +
                                                        "        </tr>\n" +
                                                        "    </tbody>\n" +
                                                        "</table>\n" +
                                                        "                  \n" +
                                                        "              <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                                                        "              </div>\n" +
                                                        "            </div>\n" +
                                                        "          <!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
                                                        "        </div>\n" +
                                                        "      </div>\n" +
                                                        "    </div>\n" +
                                                        "   <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                                                        "\t\t</td>\n" +
                                                        "  </tr>\n" +
                                                        "  </tbody>\n" +
                                                        "  </table>\n" +
                                                        "  <!--[if (mso)|(IE)]></div><![endif]-->\n" +
                                                        "\n" +
                                                        "\n" +
                                                        "</body></html>";

                                                sendEmail("Policy", email, m.get("email").toString());
                                            });
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    System.out.println("POLICY DATA ------ " + dto.getPolicy());
                                    response.sendRedirect("https://iggprod.com/?page=success&lnk=http://212.72.150.51:8080/ims/content/getpolicypdfonline.jsp?num=" + dto.getPolicy());
                                } else {
                                    System.out.println("COULD NOT GET POLICY DATA FOR TRANSACTION ------ " + trans_id);
                                    response.sendRedirect("https://iggprod.com/?page=failure");
                                }
                            }
                        } else {
                            System.out.println("FILE COULD NOT BE FOUND ON PATH BUT PAYMENT WAS SUCCESSFULL ------ " + path);
                            response.sendRedirect("https://iggprod.com/?page=failure");
                        }
                    } else {
                        System.out.println("BANK PAYMENT FAILED WITH ------ " + obj);
                        response.sendRedirect("https://iggprod.com/?page=failure");
                    }

                } else {
                    System.out.println("INVALID ERROR ON BANK SIDE");
                    response.sendRedirect("https://iggprod.com/?page=failure");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendEmail(String subject, String comment, String to) {
        try {
            mailSender.send(mimeMessage -> {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                message.setFrom("no-reply@igg.ge");
                message.setTo(to);
                message.setSubject(subject);
                message.setText(comment, true);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/failure",
            method = RequestMethod.POST,
            consumes = {"application/x-www-form-urlencoded;charset=UTF-8"}
    )
    public void failure(HttpServletResponse response) {
        try {
            response.sendRedirect("https://iggprod.com/?page=failure");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
