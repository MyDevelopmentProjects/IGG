package ge.unknown.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by user on 4/20/18.
 */
public class Algorithms {

    /**
     * მასივიდან ორი მაქსიმუმი რიცხვის ამოღება
     * @param nums
     */
    public void printTwoMaxNumbers(int[] nums){
        int MAX_ONE = 0;
        int MAX_TWO = 0;
        for(int number:nums){
            if(MAX_ONE < number){ // თუ უმაღლესი რიცხვი ნაკლებია, მაშნ მნიშვნელობებს გადავათამაშებთ
                MAX_TWO = MAX_ONE;
                MAX_ONE = number;
            } else if(MAX_TWO < number){
                MAX_TWO = number;
            }
        }
        System.out.println("First Max Number: "+MAX_ONE);
        System.out.println("Second Max Number: "+MAX_TWO);
    }


    /**
     * მინიმუმის და მაქსიმუმის პოვნა ერთი მასივის გადავლით
     * @param arr
     */
    public static void findMinMax(int[] arr){
        int MIN=arr[0];
        int MAX=arr[0];
        for(int ii=0; ii<arr.length; ii++){
            if(arr[ii] < MIN){
                MIN = arr[ii];
            }
            else if(arr[ii] > MAX){
                MAX = arr[ii];
            }
        }
        System.out.println("The minimum in the arr::"+MIN);
        System.out.println("The maximum in the arr::"+MAX);
    }

    /**
     * მარტივია რიცხვი თუ შედგენილი
     * @param number
     * @return
     */
    public boolean isPrimeNumber(int number){
        for(int i=2; i<=number/2; i++){
            if(number % i == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * ორობითი -> ათობითში გადაყვანა
     * @param binaryNumber
     * @return
     */
    public static int binaryToDecimal(int binaryNumber){
        int decimal = 0;
        int power = 0;
        while(binaryNumber != 0){
            int reminder = binaryNumber % 10; // ნაშთი
            decimal += reminder * Math.pow(2, power); // ნაშთი ახარისხებული 2 ით
            binaryNumber /= 10; // მეათედებით გაყოფა
            power++; // ვზრდით ხარისხის მაჩვენებელს როგორც ხელით გაყოფისას
        }
        return decimal;
    }

    /**
     * ათობითს -> ორობითში გადაყვანა
     * @param decimalNumber
     * @return
     */
    public static String decimalToBinary(int decimalNumber){
        StringBuilder number = new StringBuilder();
        while(decimalNumber !=  0){
            int remainder = decimalNumber % 2;
            number.append(remainder);
            decimalNumber /= 2;
        }
        return number.reverse().toString();
    }

    /**
     * რიცხვის შემოტრიალება
     * @param number
     * @return
     */
    public int reverseNumber(int number){
        int reverse = 0;
        while(number != 0){
            reverse = (reverse*10) + (number%10); // ვუმატებთ ნაშთს
            number /= 10;
        }
        return reverse;
    }

    /**
     * ASCII ში ჩაწერილი ციფრები გადაყავს ნამდვილ რიცხვთა სიმრავლეშ
     * @param numStr
     * @return
     */
    public static int convertStringToNumber(String numStr){
        char ch[] = numStr.toCharArray();
        int sum = 0;
        //get ascii value for zero
        int zeroAscii = (int)'0';
        for(char c:ch){
            int tmpAscii = (int)c;
            sum = (sum*10)+(tmpAscii-zeroAscii);
        }
        return sum;
    }

    /**
     * მასივიდან დუპლიკატის გაქრობა
     * @param input
     * @return
     */
    public static int[] removeDuplicates(int[] input){
        int j = 0;
        int i = 1;
        if(input.length < 2){
            return input;
        }
        while(i < input.length){
            if(input[i] == input[j]){
                i++;
            }else{
                input[++j] = input[i++];
            }
        }
        int[] output = new int[j+1];
        for(int k=0; k<output.length; k++){
            output[k] = input[k];
        }
        return output;
    }

    /**
     * Bubble Sort
     * @param array
     */
    public static void bubble_srt(int array[]) {
        for (int m = array.length; m >= 0; m--) {
            for (int i = 0; i < array.length - 1; i++) {
                if (array[i] > array[i + 1]) {
                    swapNumbers(i, i + 1, array);
                }
            }
            printNumbers(array);
        }
    }

    private static void swapNumbers(int i, int j, int[] array) {
        int temp;
        temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static void printNumbers(int[] input) {
        for (int i = 0; i < input.length; i++) {
            System.out.print(input[i] + ", ");
        }
        System.out.println("\n");
    }


    /**
     * ტექსტის პერმუტაცია
     * @param perm
     * @param word
     * @param permutations
     */
    private static void permutation(String perm, String word, List<String> permutations) {
        if (word.isEmpty()) {
            permutations.add(perm + word);
        } else {
            for (int i = 0; i < word.length(); i++) {
                permutation(perm + word.charAt(i), word.substring(0, i) + word.substring(i + 1, word.length()), permutations);
            }
        }
    }


    /**
     * არის თუ არა ანაგრამა სიტყვა[შედგენილია თუ არა მსგავსი სიმბოლოებისგან]
     * @param word
     * @param anagram
     * @return
     */
    public static boolean isAnagram(String word, String anagram){
        char[] charFromWord = word.toCharArray();
        char[] charFromAnagram = anagram.toCharArray();
        Arrays.sort(charFromWord);
        Arrays.sort(charFromAnagram);
        return Arrays.equals(charFromWord, charFromAnagram);
    }

    /**
     * არის თუ არა პალინდრომი[ana,aka,akka]
     * @param txt
     * @return
     */
    public static boolean isPalindrom(String txt){
        char[] word = txt.toCharArray();
        int i1 = 0;
        int i2 = word.length - 1;
        while (i2 > i1) {
            if (word[i1] != word[i2]) {
                return false;
            }
            ++i1;
            --i2;
        }
        return true;
    }

    public static String inPlaceReverse(final String input) {
        final StringBuilder builder = new StringBuilder(input);
        int length = builder.length();
        for (int i = 0; i < length / 2; i++) {
            final char start = builder.charAt(i);
            final int end = (length - i) - 1;
            // ადგილების გაცვლას ვაკეთებთ
            // ბოლო მოგვაქვს წინ
            builder.setCharAt(i, builder.charAt(end));
            // წინა მიგვაქვს ბოლოში
            builder.setCharAt(end, start);
        }

        return builder.toString();
    }


    public static int factorial(int n){
        if(n == 0)
            return 1;
        return n * factorial(n-1);
    }

    Set<String> strings = new HashSet<>();

    public static void main(String a[]){
        System.out.println(isAnagram("Mikheil", "Mih1kiel"));
        System.out.println(isPalindrom("misho"));

        String ninia = "ninia";
        String mikha = "ninia";


        System.out.println(ninia.hashCode());
        System.out.println(factorial(4));
    }


}
