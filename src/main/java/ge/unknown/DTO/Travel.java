package ge.unknown.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Travel {

     private String oper = "addpolicy";
     private String product =  "travel";
     private String startdate; //":"01/01/2018",
     private String idn; //": "01234567891",
     private String phone = "500000000"; //":"599111111",
     private String address = "უზნაძის 25, ბინა 16"; //": "უზნაძის 25, ბინა 16",
     private String dob; //":"01/01/1896",
     private String email = "no-email@email.com"; //": "test@mail.com",
     private String fname; //": "შალვა",
     private String lname; //":"ნათელაშვილი",
     private String gender; //": "M",
     private String passportnumber; //": "GT98798275",
     private String countrylist; //": "უგანდა, ავღანეთი, ერაყი",
     private String enddate; //": "01/01/2019",
     private String policydate; //": "01/02/2018",
     private String bagage; //": true,
     private String travellimit; //": true
     private String currency; //": currency

}
