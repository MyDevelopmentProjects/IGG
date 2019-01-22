package ge.unknown.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * Created by user on 5/14/18.
 */
@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model,
                        @RequestParam(name = "page", defaultValue = "", required = false) String page,
                        @RequestParam(name = "lnk", defaultValue = "", required = false) String lnk) {
        if (!lnk.equals("")) {
            model.addAttribute("lnk", lnk);
        }
        switch (page) {
            case "casco":
            case "travel":
            case "tpl":
            case "documentSign":
            case "doctors":
            case "success":
            case "failure":
                return page;
        }
        return "index";
    }
}
