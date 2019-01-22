package ge.unknown.controller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
class Calculator {

    public Set<Integer> ages;
    public Set<Integer> prices;
    public Double sedanPercents;
    public Double jeepPercents;

}
