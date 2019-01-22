package ge.unknown.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TPL {
    String packagename;
    String oper = "addpolicy";
    String product = "tpl";
    String startdate;                                        //"01/01/2018",
    String enddate;                                        //"01/01/2018",
    String idn;                                        // "01234567891"
    String phone;                                        // "599111111"
    String address;                                        // "უზნაძის 25, ბინა 16"
    String dob;                                        // "01/01/1896",
    String email;                                        // "test@mail.com",
    String fname;                                        // "შალვა",
    String lname;                                        // "ნათელაშვილი",
    String gender = "M";                                        // "M",
    String limit;                                        // "15000",
    String vin;                                        // "ABG9182475689723967"
    String platenumber;                                        // "AT678UI"
    String vehiclepassportnumber;                                        // "AA7657656567",
    Integer paymentschedule;                                        // 12
}

