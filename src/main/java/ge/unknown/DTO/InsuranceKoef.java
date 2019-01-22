package ge.unknown.DTO;

import lombok.Data;

@Data
public class InsuranceKoef {

    private String pack;
    private int insId;
    private String option;
    private Double value;

    public InsuranceKoef(String pack, int insId, String option, Double value) {
        this.pack = pack;
        this.insId = insId;
        this.option = option;
        this.value = value;
    }

}