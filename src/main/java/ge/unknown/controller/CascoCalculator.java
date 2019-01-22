package ge.unknown.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CascoCalculator {

    List<Calculator> calculatorList = new ArrayList<>();

    public CascoCalculator() {


        calculatorList.add(Calculator.builder()
                .ages(IntStream.rangeClosed(21, 29).boxed().collect(Collectors.toSet()))
                .prices(IntStream.rangeClosed(3000, 4999).boxed().collect(Collectors.toSet()))
                .sedanPercents(7.50)
                .jeepPercents(7D).build());
        calculatorList.add(Calculator.builder()
                .ages(IntStream.rangeClosed(30, 39).boxed().collect(Collectors.toSet()))
                .prices(IntStream.rangeClosed(3000, 4999).boxed().collect(Collectors.toSet()))
                .sedanPercents(6.50)
                .jeepPercents(6D).build());
        calculatorList.add(Calculator.builder()
                .ages(IntStream.rangeClosed(40, 100).boxed().collect(Collectors.toSet()))
                .prices(IntStream.rangeClosed(3000, 4999).boxed().collect(Collectors.toSet()))
                .sedanPercents(5.50)
                .jeepPercents(5D).build());




        calculatorList.add(Calculator.builder()
                .ages(IntStream.rangeClosed(21, 29).boxed().collect(Collectors.toSet()))
                .prices(IntStream.rangeClosed(5000, 9999).boxed().collect(Collectors.toSet()))
                .sedanPercents(6.50)
                .jeepPercents(6D).build());
        calculatorList.add(Calculator.builder()
                .ages(IntStream.rangeClosed(30, 39).boxed().collect(Collectors.toSet()))
                .prices(IntStream.rangeClosed(5000, 9999).boxed().collect(Collectors.toSet()))
                .sedanPercents(5D)
                .jeepPercents(4.7).build());
        calculatorList.add(Calculator.builder()
                .ages(IntStream.rangeClosed(40, 100).boxed().collect(Collectors.toSet()))
                .prices(IntStream.rangeClosed(5000, 9999).boxed().collect(Collectors.toSet()))
                .sedanPercents(4.50)
                .jeepPercents(4D).build());



        calculatorList.add(Calculator.builder()
                .ages(IntStream.rangeClosed(21, 29).boxed().collect(Collectors.toSet()))
                .prices(IntStream.rangeClosed(10000, 19999).boxed().collect(Collectors.toSet()))
                .sedanPercents(6D)
                .jeepPercents(5D).build());
        calculatorList.add(Calculator.builder()
                .ages(IntStream.rangeClosed(30, 39).boxed().collect(Collectors.toSet()))
                .prices(IntStream.rangeClosed(10000, 19999).boxed().collect(Collectors.toSet()))
                .sedanPercents(5D)
                .jeepPercents(4D).build());
        calculatorList.add(Calculator.builder()
                .ages(IntStream.rangeClosed(40, 100).boxed().collect(Collectors.toSet()))
                .prices(IntStream.rangeClosed(10000, 19999).boxed().collect(Collectors.toSet()))
                .sedanPercents(3.5)
                .jeepPercents(3.5).build());


        calculatorList.add(Calculator.builder()
                .ages(IntStream.rangeClosed(21, 29).boxed().collect(Collectors.toSet()))
                .prices(IntStream.rangeClosed(20000, 1000000).boxed().collect(Collectors.toSet()))
                .sedanPercents(4.2)
                .jeepPercents(4D).build());
        calculatorList.add(Calculator.builder()
                .ages(IntStream.rangeClosed(30, 39).boxed().collect(Collectors.toSet()))
                .prices(IntStream.rangeClosed(20000, 1000000).boxed().collect(Collectors.toSet()))
                .sedanPercents(4D)
                .jeepPercents(3.5).build());
        calculatorList.add(Calculator.builder()
                .ages(IntStream.rangeClosed(40, 100).boxed().collect(Collectors.toSet()))
                .prices(IntStream.rangeClosed(20000, 1000000).boxed().collect(Collectors.toSet()))
                .sedanPercents(3.20)
                .jeepPercents(3D).build());

    }


    public Double calculatePrem(Integer age, Integer price, Boolean isSedan) {
        Double prem = 0D;

        for (int i = 0; i < calculatorList.size(); i++) {
            Calculator c = calculatorList.get(i);
            if (c.ages.contains(age) && c.prices.contains(price)) {
                prem = price * (isSedan ? c.sedanPercents : c.jeepPercents) / 100;
                if (prem < 200) {
                    prem = 200D;
                }
                break;
            }
        }

        return prem;
    }

}
