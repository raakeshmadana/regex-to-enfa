package assignment1;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class RegexToENFA {

    static List<List<Edge>> enfa = new ArrayList<List<Edge>>(); // ENFA is represented as an Adjacency List
    static Deque<Integer> stack = new ArrayDeque<Integer>(); // A stack is maintained to evaluate the postfix regex notation
    static Character epsilon = '\u03b5'; // Represent epsilon transitions
    static Character[][] enfaMatrix;
    static int matrixDimension;
    static SortedSet<Integer> statesWithInTransitions = new TreeSet<Integer>();
    static int initialState;
    static int finalState;

    private static void addState() {
        List<Edge> adjList = new ArrayList<Edge>();
        enfa.add(adjList);
    }

    private static void addTransition(int source, int destination, Character character) {
        List<Edge> adjList = enfa.get(source);
        Edge edge = new Edge(destination, character);
        adjList.add(edge);

        statesWithInTransitions.add(destination);
    }

    private static void basicMachine(Character character) {
        for(int i = 0; i < 2; i++) {
            addState();
        }

        addTransition(enfa.size() - 2, enfa.size() - 1, character);

        stack.addFirst(enfa.size() - 1);
        stack.addFirst(enfa.size() - 2);
        finalState = enfa.size() - 1;
    }

    private static void kleeneClosure() {
        int head = stack.removeFirst();
        int tail = stack.removeFirst();

        for(int i = 0; i < 2; i++) {
            addState();
        }

        addTransition(enfa.size() - 2, head, epsilon);
        addTransition(enfa.size() - 2, enfa.size() - 1, epsilon);

        addTransition(tail, head, epsilon);
        addTransition(tail, enfa.size() - 1, epsilon);

        stack.addFirst(enfa.size() - 1);
        stack.addFirst(enfa.size() - 2);
        finalState = enfa.size() - 1;
    }
    
    private static void concatenation() {
        int head2 = stack.removeFirst();
        int tail2 = stack.removeFirst();
        int head1 = stack.removeFirst();
        int tail1 = stack.removeFirst();

        addTransition(tail1, head2, epsilon);

        stack.addFirst(tail2);
        stack.addFirst(head1);
        finalState = tail2;

    }

    private static void alternation() {
        int head2 = stack.removeFirst();
        int tail2 = stack.removeFirst();
        int head1 = stack.removeFirst();
        int tail1 = stack.removeFirst();

        for(int i = 0; i < 2; i++) {
            addState();
        }

        addTransition(enfa.size() - 2, head1, epsilon);
        addTransition(enfa.size() - 2, head2, epsilon);

        addTransition(tail1, enfa.size() - 1, epsilon);
        addTransition(tail2, enfa.size() - 1, epsilon);

        stack.addFirst(enfa.size() - 1);
        stack.addFirst(enfa.size() - 2);
        finalState = enfa.size() - 1;
    }

    private static void printAdjList() {
        System.out.println("Adjacency List");
        for(int i = 0; i < enfa.size(); i++) {
            List<Edge> edgeList = enfa.get(i);
            System.out.println(i);
            for(int j = 0; j < edgeList.size(); j++) {
                Edge edge = edgeList.get(j);
                System.out.println(edge.transition + " -> " + edge.state);
            }
        }
    }

    private static void createAdjMatrix() {
        matrixDimension = enfa.size();
        enfaMatrix = new Character[matrixDimension][matrixDimension];

        for(int i = 0; i < matrixDimension; i++) {
            Arrays.fill(enfaMatrix[i], null);
        }

        for(int i = 0; i < enfa.size(); i++) {
            List<Edge> edgeList = enfa.get(i);
            for(int j = 0; j < edgeList.size(); j++) {
                Edge edge = edgeList.get(j);
                enfaMatrix[i][edge.state] = edge.transition;
            }
        }
    }

    private static void printAdjMatrix() {
        System.out.println("Adjacency Matrix");

        System.out.print(" \t");
        for(int i = 0; i < matrixDimension; i++) {
            System.out.printf("%4d\t", i);
        }
        System.out.println();
        for(int i = 0; i < matrixDimension; i++) {
            System.out.printf("%4d\t", i);
            for(int j = 0; j < matrixDimension; j++) {
                System.out.printf("%4c\t", enfaMatrix[i][j]);
            }
            System.out.println();
        }
    }

    public static void regexToENFA(String postfixRegex) {
        for(int i = 0; i < postfixRegex.length(); i++) {
            char c = postfixRegex.charAt(i);
            switch(c) {
                case '*':
                    kleeneClosure();
                    break;

                case '.':
                    concatenation();
                    break;

                case '|':
                    alternation();
                    break;

                default:
                    basicMachine(c);
            }
        }

        Integer[] nonInitialStates = statesWithInTransitions.toArray(new Integer[0]);
        for(int i = 0; i < nonInitialStates.length; i++) {
            if(i != nonInitialStates[i]) {
                initialState = i;
                break;
            }
        }

        System.out.println("Initial State: " + initialState);
        System.out.println("Final State: " + finalState);

        createAdjMatrix();

        printAdjList();
        System.out.println();
        printAdjMatrix();
    }
}