/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs420project1;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class for creating an object that represents the state of an 8 puzzle board.
 *
 * @author Jason Kaufman
 */
public final class Board
{

    //Declare instance variables.
    private int[] board;
    private int heuristic;
    private int depth;
    private int option;
    private static final int BOARD_DIMENSION = 3;
    private Board previousState;

    //Constructor to create a Board object based on the order of tiles, the solution depth, if it is an initial state, and the type of heuristic.
    public Board(int[] board_, int depth_, boolean initialState_, int option_, Board previousState_)
    {
        board = board_.clone();
        depth = depth_;
        option = option_;

        if (initialState_)
        {
            heuristic = 0;
            previousState = null;
        }
        else
        {
            //Heuristic plus the step cost.
            switch (option_)
            {
                case 1:
                    heuristic = Hamming() + depth_;
                    break;
                case 2:
                    heuristic = ManhattanSum() + depth_;
                    break;
            }

            previousState = previousState_;
        }
    }

    //Determines if the board is solvable based on the number of inversions.
    //If odd number of inversions, the board can not be solved.
    public boolean IsSolvable()
    {
        int inversions = 0;

        for (int i = 0; i < board.length; i++)
        {
            for (int j = i + 1; j < board.length; j++)
            {
                if (board[i] > board[j] && board[j] != 0)
                {
                    inversions++;
                }
            }
        }

        return inversions % 2 == 0;
    }

    //Obtains the cost of the board using Manhattan distance of the tiles from their appropriate locations.
    public int ManhattanSum()
    {
        int sum = 0;

        for (int i = 0; i < board.length; i++)
        {
            if (board[i] != i + 1 && board[i] != 0)
            {
                sum += ManhattanDistance(board[i], i);
            }
        }

        return sum;
    }

    //Obtains the manhattan distance of a misplaced block.
    private int ManhattanDistance(int goal, int current)
    {
        int row, col;

        row = Math.abs((goal - 1) / BOARD_DIMENSION - current / BOARD_DIMENSION);
        col = Math.abs((goal - 1) % BOARD_DIMENSION - current % BOARD_DIMENSION);

        return row + col;
    }

    //Obtains the cost of the board using the number of misplaced tiles.
    public int Hamming()
    {
        int count = 0;

        //Compares the current state with the goal state.
        for (int i = 0; i < board.length; i++)
        {
            if (board[i] != i + 1 && board[i] != 0)
            {
                count++;
            }
        }

        return count;
    }

    //Returns an ArrayList of new states that result from this state.
    public ArrayList<Board> PossibleStates()
    {
        //Records the position of the empty tile.
        int index = 0;

        Board possibleState;
        ArrayList<Board> possibleStateList = new ArrayList<>();

        //Search for the empty tile.
        for (int i = 0; i < board.length; i++)
        {
            if (board[i] == 0)
            {
                index = i;
                break;
            }
        }

        //If the empty tile is not in the first row, exchange with the block above.
        if (index / BOARD_DIMENSION != 0)
        {
            possibleState = new Board(board, depth + 1, false, option, this);
            ExchangeTiles(possibleState, index, index - BOARD_DIMENSION);

            if (this.getPreviousState() == null || !Arrays.equals(possibleState.getBoard(), this.getPreviousState().getBoard()))
            {
                possibleStateList.add(possibleState);
            }
        }

        if (index / BOARD_DIMENSION != (BOARD_DIMENSION - 1))
        {
            possibleState = new Board(board, depth + 1, false, option, this);
            ExchangeTiles(possibleState, index, index + BOARD_DIMENSION);

            if (this.getPreviousState() == null || !Arrays.equals(possibleState.getBoard(), this.getPreviousState().getBoard()))
            {
                possibleStateList.add(possibleState);
            }
        }

        //If the empty tile is not in the leftmost column, exchange with the left block.
        if ((index % BOARD_DIMENSION) != 0)
        {
            possibleState = new Board(board, depth + 1, false, option, this);
            ExchangeTiles(possibleState, index, index - 1);

            if (this.getPreviousState() == null || !Arrays.equals(possibleState.getBoard(), this.getPreviousState().getBoard()))
            {
                possibleStateList.add(possibleState);
            }
        }

        //If the empty tile is not in the rightmost column, exchange with the right block.
        if ((index % BOARD_DIMENSION) != BOARD_DIMENSION - 1)
        {
            possibleState = new Board(board, depth + 1, false, option, this);
            ExchangeTiles(possibleState, index, index + 1);

            if (this.getPreviousState() == null || !Arrays.equals(possibleState.getBoard(), this.getPreviousState().getBoard()))
            {
                possibleStateList.add(possibleState);
            }
        }

        //System.out.println(Arrays.toString(board));
        return possibleStateList;
    }

    //Exchange two tiles on the board.
    private void ExchangeTiles(Board a, int i, int j)
    { 
        int temp = a.board[i];
        a.board[i] = a.board[j];
        a.board[j] = temp;
    }

    public int[] getBoard()
    {
        return board;
    }

    public void setBoard(int[] board)
    {
        this.board = board;
    }

    public int getHeuristic()
    {
        return heuristic;
    }

    public void setHeuristic(int heuristic)
    {
        this.heuristic = heuristic;
    }

    public int getDepth()
    {
        return depth;
    }

    public void setDepth(int depth)
    {
        this.depth = depth;
    }

    public int getOption()
    {
        return option;
    }

    public void setOption(int option)
    {
        this.option = option;
    }

    public Board getPreviousState()
    {
        return previousState;
    }

    public void setPreviousState(Board previousState)
    {
        this.previousState = previousState;
    }

}
