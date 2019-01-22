package ge.unknown.data;


import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DoctorAvailResponse extends BaseResponse {
    private List<DoctorAvailSchedule> schedule = new ArrayList<>();
}
