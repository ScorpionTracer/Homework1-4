import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {

  private static final char DOT_EMPTY = '*';
  private static final char DOT_X = 'X';
  private static final char DOT_O = 'O';

  private static final int SIZE = 3;
  private static final int DOTS_TO_WIN = 3;
  private static final int DOT_AI_CHECK = 2;

  private static final char[][] map = new char[SIZE][SIZE];
  private static final Scanner scanner = new Scanner(System.in);
  private static final Random random = new Random();

  public static void main(String[] args) {
    prepareGame();
    playGame();
    System.out.println("Игра окончена");
  }

  private static void playGame() {
    while (true) {
      humanTurn();
      printMap();

      if (checkWin(DOT_X)) {
        System.out.println("Победил человек!");
        break;
      }

      if (isMapFull()) {
        System.out.println("Ничья");
        break;
      }

      aiTurn();
      printMap();

      if (checkWin(DOT_O)) {
        System.out.println("Победил искуственный интелект!");
        break;
      }

      if (isMapFull()) {
        System.out.println("Ничья");
        break;
      }
    }
  }

  private static void prepareGame() {
    initMap();
    printMap();
  }

  private static boolean isMapFull() {
    for (char[] row : map) {
      for (char cell : row) {
        if (cell == DOT_EMPTY) {
          return false;
        }
      }
    }
    return true;
  }

  private static boolean checkWin(char symbol) {
    if (!linesAndColsChecksCheck(symbol)) {
      return checkDiagonals(symbol);
    }
    return true;
  }

  private static boolean checkDiagonals(char symbol) {
    int oneDiagonal = 0, twoDiagonal = 0;
    for (var i = 0; i < map.length; i++) {
      if (map[i][i] == symbol) {
        oneDiagonal += 1;
      } else oneDiagonal = 0;
      if (map[i][map.length - 1 - i] == symbol) {
        twoDiagonal += 1;
      } else twoDiagonal = 0;
    }
    return oneDiagonal == DOTS_TO_WIN || twoDiagonal == DOTS_TO_WIN;
  }


  private static boolean linesAndColsChecksCheck(char symbol) {
    for (var i = 0; i < map.length; i++) {
      int rowIndex = 0, colIndex = 0;
      for (var j = 0; j < map[i].length; j++) {
        if (map[i][j] == symbol) rowIndex += 1;
        else rowIndex = 0;
        if (map[j][i] == symbol) colIndex += 1;
        else colIndex = 0;
      }
      if (rowIndex == DOTS_TO_WIN || colIndex == DOTS_TO_WIN) return true;
    }
    return false;
  }


  private static void aiTurn() {
    if (!aiCheckLinesAndCols() && !aiCheckDiagonals()) {
      aiRandom();
    }
  }

  private static boolean aiCheckDiagonals() {
    int oneDiagonal = 0, twoDiagonal = 0;
    var indexOneDiagonal = 0;
    int begTwoDiagonal = 0, endTwoDiagonal = 0;

    for (var i = 0; i < map.length; i++) {
      if (map[i][i] == DOT_X) {
        oneDiagonal += 1;
      } else if (map[i][i] == DOT_EMPTY) {
        indexOneDiagonal = i;
      }
      if (map[i][map.length - 1 - i] == DOT_X) {
        twoDiagonal += 1;
      } else if (map[i][map.length - 1 - i] == DOT_EMPTY) {
        begTwoDiagonal = i;
        endTwoDiagonal = map.length - 1 - i;
      }
    }
    if (oneDiagonal == DOT_AI_CHECK) {
      map[indexOneDiagonal][indexOneDiagonal] = DOT_O;
      return true;
    } else if (twoDiagonal == DOT_AI_CHECK) {
      map[begTwoDiagonal][endTwoDiagonal] = DOT_O;
      return true;
    }
    return false;
  }

  private static void aiRandom() {
    int rowIndex, colIndex;
    do {
      rowIndex = random.nextInt(SIZE);
      colIndex = random.nextInt(SIZE);
    } while (!isCellValid(rowIndex, colIndex));
    map[rowIndex][colIndex] = DOT_O;
  }

  private static boolean aiCheckLinesAndCols() {
    int begRow = 0, endRow = 0;
    int begCol = 0, endCol = 0;

    for (var i = 0; i < map.length; i++) {
      int rowIndex = 0, colIndex = 0;
      for (var j = 0; j < map[i].length; j++) {
        if (map[i][j] == DOT_X) rowIndex += 1;
        else if (map[i][j] == DOT_EMPTY) {
          begRow = i;
          endRow = j;
        }
        if (map[j][i] == DOT_X) colIndex += 1;
        else if (map[j][i] == DOT_EMPTY) {
          begCol = j;
          endCol = i;
        }
      }
      if (rowIndex == DOT_AI_CHECK) {
        map[begRow][endRow] = DOT_O;
        return true;
      } else if (colIndex == DOT_AI_CHECK) {
        map[begCol][endCol] = DOT_O;
        return true;
      }
    }
    return false;
  }

  private static void humanTurn() {
    int rowIndex = -1, colIndex = -1;

    do {
      System.out.println("Введите координаты в формате <номер строки> <номер колонки>");
      String[] stringData = scanner.nextLine().split(" ");
      if (stringData.length != 2) {
        continue;
      }
      try {
        rowIndex = Integer.parseInt(stringData[0]) - 1;
        colIndex = Integer.parseInt(stringData[1]) - 1;
      } catch (NumberFormatException e) {
        System.out.println("Были введены некорректные данные");
      }
    } while (!isCellValid(rowIndex, colIndex));
    map[rowIndex][colIndex] = DOT_X;
  }


  private static boolean isCellValid(int rowIndex, int colIndex) {
    if (rowIndex < 0 || rowIndex >= SIZE || colIndex < 0 || colIndex >= SIZE) {
      return false;
    }
    return map[rowIndex][colIndex] == DOT_EMPTY;
  }

  private static void printMap() {
    printColumnNumbers();
    printRows();
    System.out.println();
  }

  private static void printRows() {
    for (var i = 0; i < map.length; i++) {
      System.out.print(i + 1 + " ");
      for (var j = 0; j < map[i].length; j++) {
        System.out.print(map[i][j] + " ");
      }
      System.out.println();
    }
  }

  private static void printColumnNumbers() {
    for (var i = 0; i <= SIZE; i++) {
      System.out.print(i + " ");
    }
    System.out.println();
  }

  private static void initMap() {
    for (char[] row : map) {
      Arrays.fill(row, DOT_EMPTY);
    }
  }
}
