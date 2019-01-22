package ge.unknown.data;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DoctorSyncReq {
    private String oper = "syncdoctor";
}
