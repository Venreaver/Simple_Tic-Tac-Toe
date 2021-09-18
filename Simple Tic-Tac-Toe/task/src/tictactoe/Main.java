package tictactoe;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static Character[][] grid;

    public static void main(String[] args) {
        grid = populateGrid();
        printGrid();
        System.out.println(play());
    }

    private static Character[][] populateGrid() {
        Character[][] grid = new Character[3][3];
        for (Character[] characters : grid) {
            Arrays.fill(characters, ' ');
        }
        return grid;
    }

    private static void printGrid() {
        System.out.println("---------");
        for (Character[] characters : grid) {
            System.out.print("| ");
            for (Character character : characters) {
                System.out.print(character + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    private static String play() {
        boolean xWinner = false;
        boolean oWinner = false;
        int xMove = 1;
        do {
            System.out.println("Enter the coordinates: ");
            try {
                int row = sc.nextInt() - 1;
                int col = sc.nextInt() - 1;
                if (row > 2 || col > 2 || row < 0 || col < 0) {
                    System.out.println("Coordinates should be from 1 to 3!");
                } else {
                    if (isCellEmpty(row, col)) {
                        grid[row][col] = (xMove++ & 1) == 1 ? 'X' : 'O';
                        printGrid();
                        xWinner = isXWinner(grid);
                        oWinner = isOWinner(grid);
                    } else {
                        System.out.println("This cell is occupied! Choose another one!");
                    }
                }
            } catch (InputMismatchException ex) {
                System.out.println("You should enter numbers!");
            }
        } while (!isDraw() && !xWinner && !oWinner);
        String status;
        if (oWinner) {
            status = "O wins";
        } else if (xWinner) {
            status = "X wins";
        } else {
            status = "Draw";
        }
        return status;
    }

    private static boolean isCellEmpty(int row, int col) {
        return grid[row][col] == ' ';
    }

    private static boolean isDraw() {
        return !areMovesAvailable(grid) && !isXWinner(grid) && !isOWinner(grid);
    }

    private static String getStatus(Character[][] grid) {
        String status;
        boolean diffsWrong = areDiffsWrong(grid);
        boolean xWinner = isXWinner(grid);
        boolean oWinner = isOWinner(grid);
        boolean movesAvailable = areMovesAvailable(grid);
        boolean impossible = xWinner && oWinner || diffsWrong;
        boolean draw = !movesAvailable && !oWinner && !xWinner;
        if (impossible) {
            status = "Impossible";
        } else if (oWinner) {
            status = "O wins";
        } else if (xWinner) {
            status = "X wins";
        } else if (draw) {
            status = "Draw";
        } else {
            status = "Game not finished";
        }
        return status;
    }

    private static boolean isXWinner(Character[][] grid) {
        return isWinner('X', grid);
    }

    private static boolean isOWinner(Character[][] grid) {
        return isWinner('O', grid);
    }

    private static boolean areDiffsWrong(Character[][] grid) {
        return Math.abs(count('X', grid) - count('O', grid)) > 1;
    }

    private static int count(Character x, Character[][] grid) {
        int count = 0;
        for (Character[] characters : grid) {
            for (Character character : characters) {
                if (x.equals(character)) {
                    count++;
                }
            }
        }
        return count;
    }

    private static boolean isWinner(Character player, Character[][] grid) {
        return isRowOf(player, grid) || isColOf(player, grid) || isDiagonalOf(player, grid);
    }

    private static boolean areMovesAvailable(Character[][] grid) {
        return Arrays.stream(grid).flatMap(Arrays::stream).anyMatch(ch -> ch.equals(' '));
    }

    private static boolean isDiagonalOf(Character player, Character[][] grid) {
        return isLineOf(player, new Character[]{grid[0][0], grid[1][1], grid[2][2]})
                || isLineOf(player, new Character[]{grid[0][2], grid[1][1], grid[2][0]});
    }

    private static boolean isColOf(Character player, Character[][] grid) {
        return isLinesOf(player, rotateToRight(grid));
    }

    private static boolean isRowOf(Character player, Character[][] grid) {
        return isLinesOf(player, grid);
    }

    private static boolean isLinesOf(Character player, Character[][] grid) {
        for (Character[] ch : grid) {
            if (isLineOf(player, ch)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isLineOf(Character player, Character[] line) {
        return Arrays.equals(new Character[]{player, player, player}, line);
    }

    private static Character[][] rotateToRight(Character[][] grid) {
        Character[][] cols = new Character[3][3];
        for (int i = 0; i < grid.length; i++) {
            for (int j = grid.length - 1; j > -1; j--) {
                cols[i][j] = grid[j][i];
            }
        }
        return cols;
    }
}
