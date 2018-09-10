package assignment1;

public class Assignment1 {

    public static void main(String args[]) {
        String regex = args[0];
        String infixRegex = Helpers.useDotForConcatenation(regex);
        System.out.println(infixRegex);
        String postfixRegex = Helpers.infixToPostfix(infixRegex);
        System.out.println(postfixRegex);
        RegexToENFA.regexToENFA(postfixRegex);
    }
}