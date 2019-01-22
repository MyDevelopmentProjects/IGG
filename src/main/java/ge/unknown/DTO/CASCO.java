package ge.unknown.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CASCO {
    private String oper; //;             // ":"addpolicy",
    private String product; //;             // ":"casco",
    private String startdate; //;             // ":"01/01/2018",
    private String idn; //;             // ":"01234567891",
    private String phone; //;             // ":"599111111",
    private String address; //;             // ":"უზნაძის 25, ბინა 16",
    private String dob;             // ":"01/01/1896",
    private String email;             // ":"test@mail.com",
    private String fname;             // ":"შალვა",
    private String lname;             // ":"ნათელაშვილი",
    private String gender;             // ":"M",
    private String vehicletype;             // ":"სატვირთო",
    private String price;             // ":15000,
    private String year;             // ":2010,
    private String driverside;             // ":1,
    private String driverage;             // ":45,
    private String driverexperience;             // ":10,
    private String numberofdrivers;             // ":2,
    private String franshiza;             // ":"200 აშშ დოლარი",
    private Boolean risk;             // ":true,
    private Boolean extraservice;             // ":true,
    private String tpllimit;             // ":"20000",
    private String malimit;             // ":"40000",
    private String paymentschedule;             // ":12
}
