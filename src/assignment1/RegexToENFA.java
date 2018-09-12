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

    private static void basicMachine(Character character) {
        for(int j = 0; j < 2; j++) {
            List<Edge> adjList = new ArrayList<Edge>();
            enfa.add(adjList);
        }

        List<Edge> adjList = enfa.get(enfa.size() - 2);
        Edge edge = new Edge(enfa.size() - 1, character);
        adjList.add(edge);

        stack.addFirst(enfa.size() - 1);
        stack.addFirst(enfa.size() - 2);
        finalState = enfa.size() - 1;

        statesWithInTransitions.add(finalState);
    }

    private static void kleeneClosure() {
        int head = stack.removeFirst();
        int tail = stack.removeFirst();

        for(int j = 0; j < 2; j++) {
            List<Edge> adjList = new ArrayList<Edge>();
            enfa.add(adjList);
        }

        List<Edge> adjList = enfa.get(enfa.size() - 2);
        Edge edge = new Edge(head, epsilon);
        adjList.add(edge);
        statesWithInTransitions.add(head);

        edge = new Edge(enfa.size() - 1, epsilon);
        adjList.add(edge);

        adjList = enfa.get(tail);
        edge = new Edge(head, epsilon);
        adjList.add(edge);
        edge = new Edge(enfa.size() - 1, epsilon);
        adjList.add(edge);

        stack.addFirst(enfa.size() - 1);
        stack.addFirst(enfa.size() - 2);
        finalState = enfa.size() - 1;

        statesWithInTransitions.add(finalState);
    }
    
    private static void concatenation() {
        int head2 = stack.removeFirst();
        int tail2 = stack.removeFirst();
        int head1 = stack.removeFirst();
        int tail1 = stack.removeFirst();

        List<Edge> adjList = enfa.get(tail1);
        Edge edge = new Edge(head2, epsilon); 
        adjList.add(edge);
        statesWithInTransitions.add(head2);

        stack.addFirst(tail2);
        stack.addFirst(head1);
        finalState = tail2;

    }

    private static void alternation() {
        int head2 = stack.removeFirst();
        int tail2 = stack.removeFirst();
        int head1 = stack.removeFirst();
        int tail1 = stack.removeFirst();

        for(int j = 0; j < 2; j++) {
            List<Edge> adjList = new ArrayList<Edge>();
            enfa.add(adjList);
        }

        List<Edge> adjList = enfa.get(enfa.size() - 2);
        Edge edge = new Edge(head1, epsilon);
        adjList.add(edge);
        statesWithInTransitions.add(head1);
        edge = new Edge(head2, epsilon);
        adjList.add(edge);
        statesWithInTransitions.add(head2);

        adjList = enfa.get(tail1);
        edge = new Edge(enfa.size() - 1, epsilon);
        adjList.add(edge);

        adjList = enfa.get(tail2);
        edge = new Edge(enfa.size() - 1, epsilon);
        adjList.add(edge);

        stack.addFirst(enfa.size() - 1);
        stack.addFirst(enfa.size() - 2);
        finalState = enfa.size() - 1;

        statesWithInTransitions.add(finalState);
    }

    private static void printAdjList() {
        System.out.println("Adjacency List");
        for(int i = 0; i < enfa.size(); i++) {
            List<Edge> edgeList = enfa.get(i);
            System.out.println(i);
            for(int j = 0; j < edgeList.size(); j++) {
                Edge edge = edgeList.get(j);
                System.out.println(edge.transition + " -> " + edge.nodeID);
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
                enfaMatrix[i][edge.nodeID] = edge.transition;
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