package ge.unknown.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorVisitCreationReq {
    private String oper;//       "oper": "visit",
    private String idn;//       "idn": "XXXXXXXXXXX",
    private String visitdate; //       "visitdate": "DD/MM/YYYY HH24:MI",
    private int doctorid; //       "doctorid": 10
}
