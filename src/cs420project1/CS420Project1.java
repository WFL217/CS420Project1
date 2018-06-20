/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs420project1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.Stack;

/**
 * Program to determine solutions to the 8 puzzle with an A* algorithm using
 * Hamming or Manhattan distance heuristics.
 *
 * @author Jason Kaufman
 */
public class CS420Project1
{

    //Determines if a value already exists in an array.
    public static boolean ValueExists(int value, int[] array, int maxIndex)
    {
        for (int i = 0; i < maxIndex; i++)
        {
            if (value == array[i])
            {
                return true;
            }
        }

        return false;
    }

    //Determines how the priority queue should sort its contents (smaller cost is given priority for the purposes of this assignment).
    public static Comparator<Board> heuristicComparator = (Board b1, Board b2) ->
    {
        if (b1.getHeuristic() > b2.getHeuristic())
        {
            return 1;
        }
        if (b1.getHeuristic() < b2.getHeuristic())
        {
            return -1;
        }
        else
        {
            return 0;
        }
    };

    /**
     * The main program.
     *
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException
    {
        // TODO code application logic here

        //The goal state of the board.
        int[] goalState = new int[]
        {
            0, 1, 2, 3, 4, 5, 6, 7, 8
        };

        //Used to represent the board tiles to begin creating solutions.
        int[] initialState = new int[9];

        Board initialBoard = null;

        //The priority queue for the states.
        PriorityQueue<Board> pQueue = new PriorityQueue<>(heuristicComparator);

        //Used when generating states.
        ArrayList<Board> newStates;
        Board firstPossibleState;

        //Keeps track of generated nodes.
        int nodesGenerated;
        //int nodesGeneratedSum = 0;

        //Will be used to determine runtime for finding each solution.
        long initTime;
        long finalTime;
        long totalTime;

        //long totalTimeSum = 0;
        //int numOfRuns = 0;
        //Used for user input.
        Scanner keyboard = new Scanner(System.in);

        System.out.print("User Puzzle (1) or Random Puzzle (2): ");
        int userChoice = Integer.parseInt(keyboard.nextLine());

        int userSolution = 0;

        switch (userChoice)
        {
            //Allows user to use one of two approaches and to input their own puzzle.
            case 1:
                System.out.print("Hamming (1) or Manhattan (2): ");
                userSolution = Integer.parseInt(keyboard.nextLine());

                switch (userSolution)
                {
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        System.out.println("Incorrect option");
                        System.exit(0);
                        break;
                }

                System.out.print("Please enter in a puzzle (# # # # # # # # #): ");
                String userPuzzle = keyboard.nextLine();
                String[] splitLine = userPuzzle.split("\\s+");

                for (int i = 0; i < splitLine.length; i++)
                {
                    initialState[i] = Integer.parseInt(splitLine[i]);
                }

                //Create the initial state.
                initialBoard = new Board(initialState, 0, true, userSolution, null);

                while (!initialBoard.IsSolvable())
                {
                    System.out.println("A solution does not exist!!!!\n");
                    
                    System.out.print("Please enter in a puzzle (# # # # # # # # #): ");
                    userPuzzle = keyboard.nextLine();
                    splitLine = userPuzzle.split("\\s+");

                    for (int i = 0; i < splitLine.length; i++)
                    {
                        initialState[i] = Integer.parseInt(splitLine[i]);
                    }

                    //Create the initial state.
                    initialBoard = new Board(initialState, 0, true, userSolution, null);
                }

                break;
            //Allows user to use one of two approaches and to have the program generate a puzzle.
            case 2:
                System.out.print("Hamming (1) or Manhattan (2): ");
                userSolution = Integer.parseInt(keyboard.nextLine());

                switch (userSolution)
                {
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        System.out.println("Incorrect option");
                        System.exit(0);
                        break;
                }

                System.out.println("Generating puzzle");

                Random random = new Random();
                int currentValue;

                //Creates a random 8 puzzle.
                for (int i = 0; i < initialState.length; i++)
                {
                    currentValue = random.nextInt(9);

                    while (ValueExists(currentValue, initialState, i))
                    {
                        currentValue = random.nextInt(9);
                    }

                    initialState[i] = currentValue;
                }

                //Create the initial state.
                initialBoard = new Board(initialState, 0, true, userSolution, null);

                while (!initialBoard.IsSolvable())
                {
                    //Creates a random 8 puzzle.
                    for (int i = 0; i < initialState.length; i++)
                    {
                        currentValue = random.nextInt(9);

                        while (ValueExists(currentValue, initialState, i))
                        {
                            currentValue = random.nextInt(9);
                        }

                        initialState[i] = currentValue;
                    }

                    //Create the initial state.
                    initialBoard = new Board(initialState, 0, true, userSolution, null);
                }

                break;
            default:
                System.out.println("Incorrect option");
                System.exit(0);
                break;
        }

        /**
         * *********************************************************************
         * The next part of the code generates solutions to the puzzles the
         * program was given.
         * ********************************************************************
         */
        nodesGenerated = 0;

        initTime = System.currentTimeMillis();

        //Add the initial state to the priority queue.
        pQueue.add(initialBoard);

        //While the queue is not empty, traverse the solution space.
        while (pQueue.isEmpty() == false)
        {
            //If the next state at the front of the queue is the goal, end the A* algorithm and state that a goal was found.
            if (Arrays.equals(pQueue.peek().getBoard(), goalState))
            {
                /*for (int i = 0; i < pQueue.peek().getBoard().length; i++)
                {
                    System.out.print(pQueue.peek().getBoard()[i] + " ");
                }*/

                //Displays information about the solution (depth, nodes generated, runtime).
                System.out.println("\nSolution found!!!!");
                System.out.println("Depth: " + pQueue.peek().getDepth());
                finalTime = System.currentTimeMillis();
                totalTime = finalTime - initTime;

                System.out.println("Nodes generated: " + nodesGenerated);
                System.out.println("Runtime: " + totalTime + " ms");

                //Displays the solutions beginning with the initial state.
                System.out.println("\nThe solution: \n");

                Stack<Board> solutionStack = new Stack<>();

                solutionStack.add(pQueue.peek());

                while (solutionStack.peek().getPreviousState() != null)
                {
                    solutionStack.add(solutionStack.peek().getPreviousState());
                }

                int[] board;
                int step = 0;

                while (!solutionStack.isEmpty())
                {
                    //System.out.println(Arrays.toString(solutionStack.pop().getBoard()));

                    System.out.println("Step " + step);
                    step++;

                    board = solutionStack.pop().getBoard();

                    int index = 0;

                    for (int i = 0; i < 3; i++)
                    {
                        for (int j = 0; j < 3; j++)
                        {
                            System.out.print(board[index]);

                            index++;
                        }
                        System.out.println();
                    }

                    System.out.println();
                }

                break;
            }

            //Create new states from the state at the front of the queue.
            newStates = pQueue.remove().PossibleStates();

            //Add the new states to the priority queue.
            while (newStates.isEmpty() == false)
            {
                firstPossibleState = newStates.remove(0);

                pQueue.add(firstPossibleState);
                nodesGenerated++;
            }
        }

        /**
         * *********************************************************************
         * The next part of the code was used to test 3200 test cases up to a
         * depth of 32. It is commented out because it is not needed for the
         * requirements of the assignment.
         * ********************************************************************
         */
        //nodesGeneratedSum += nodesGenerated;
        //totalTimeSum += totalTime;
        //numOfRuns++;

        /*int depth = 0;

        int nodesGenerated;
        int nodesGeneratedSum = 0;

        long initTime;
        long finalTime;
        long totalTime;

        long totalTimeSum = 0;

        int numOfRuns = 0;

        try (Scanner file = new Scanner(new File("3200 Scrambled Puzzles.txt")))
        {
            String line;
            String[] splitLine;

            while (file.hasNextLine())
            {
                //Obtains a line from the file
                line = file.nextLine();

                //Splits the line into an array by space or comma
                splitLine = line.split("\\s+");

                if (splitLine[0].equals("Depth"))
                {
                    if (numOfRuns != 0)
                    {
                        System.out.println("Average number of nodes: " + (nodesGeneratedSum / numOfRuns));
                        System.out.println("Average time: " + (totalTimeSum / numOfRuns) + " ms");
                        System.out.println();
                        
                        numOfRuns = 0;
                    }

                    depth = Integer.parseInt(splitLine[1]);
                    System.out.println("Depth: " + depth);
                }
                else
                {
                    nodesGenerated = 0;

                    for (int i = 0; i < splitLine.length; i++)
                    {
                        initialState[i] = Integer.parseInt(splitLine[i]);
                    }

                    initTime = System.currentTimeMillis();

                    initialBoard = new Board(initialState, 0, true, 2);

                    pQueue.add(initialBoard);

                    if (initialBoard.IsSolvable())
                    {
                        while (pQueue.isEmpty() == false)
                        {
                            if (Arrays.equals(pQueue.peek().getBoard(), goalState))
                            {
                                for (int i = 0; i < pQueue.peek().getBoard().length; i++)
                                {
                                    System.out.print(pQueue.peek().getBoard()[i] + " ");
                                }
                                System.out.println("Solution found!!!!");
                                
                                if(pQueue.peek().getDepth() > 10)
                                {
                                    System.out.println(Arrays.toString(initialState));
                                    System.out.println(pQueue.peek().getDepth());
                                }

                                break;
                            }

                            newStates = pQueue.remove().PossibleStates();

                            while (newStates.isEmpty() == false)
                            {
                                firstPossibleState = newStates.remove(0);

                                if (firstPossibleState.getDepth() <= depth)
                                {
                                    pQueue.add(firstPossibleState);
                                    nodesGenerated++;
                                }
                            }
                        }

                        finalTime = System.currentTimeMillis();
                        totalTime = finalTime - initTime;

                        //System.out.println("Nodes generated: " + nodesGenerated);
                        //System.out.println(totalTime + " ms");
                        nodesGeneratedSum += nodesGenerated;
                        totalTimeSum += totalTime;

                        numOfRuns++;
                    }
                }
            }

            System.out.println("Average number of nodes: " + (nodesGeneratedSum / numOfRuns));
            System.out.println("Average time: " + (totalTimeSum / numOfRuns) + " ms");
        }
         */
    }
}
