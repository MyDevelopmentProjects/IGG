package ge.unknown.data;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DoctorSyncResp extends BaseResponse {
    private List<Doctor> doctors = new ArrayList<>();
}
