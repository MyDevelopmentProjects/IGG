package ge.unknown.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerCreationReq {
    private String oper;                   //"oper": "addpartner/editpartner",
    private String idn;                  //"idn": "XXXXXXXXXXX", პირადი ნომერი
    private String fname;                    //"fname": "",
    private String lname;                    //"lname": "",
    private String phone;                    //"phone": "",
    private String address;                   //"address": "",
    private String gender;                   //"gender": "M/F",
    private String dob;                   //"dob": "DD/MM/YYYY",
    private String email;                    //"email": "mail@mail.com"
}
