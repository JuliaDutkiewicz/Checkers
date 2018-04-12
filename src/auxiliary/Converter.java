package auxiliary;

public class Converter {
    public static  int LettersToNumbers(char c){
        switch(c){
            case 'a' : return 1;
            case 'b' : return 2;
            case 'c' : return 3;
            case 'd' : return 4;
            case 'e' : return 5;
            case 'f' : return 6;
            case 'g' : return 7;
            case 'h' : return 8;
            case 'i' : return 9;
            case 'j' : return 10;
            case 'k' : return 11;
            case 'l' : return 12;
            default: return 13;
        }
    }
    public static char NumbersToLetters (int i){
        switch (i){
            case 1: return 'a';
            case 2: return 'b';
            case 3: return 'c';
            case 4: return 'd';
            case 5: return 'e';
            case 6: return 'f';
            case 7: return 'g';
            case 8: return 'h';
            case 9: return 'i';
            case 10: return 'j';
            case 11: return 'k';
            case 12: return 'l';
            default: return 'z';
        }
    }
}
