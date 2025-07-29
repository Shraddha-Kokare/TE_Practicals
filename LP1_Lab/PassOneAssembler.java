import java.io.*;
import java.util.*;

public class PassOneAssembler {
    static Scanner sc = new Scanner(System.in);
    static String IS[] = { "STOP", "ADD", "SUB", "MULT", "MOVER", "MOVEM", "COMP", "BC", "DIV", "READ", "PRINT" };
    static String AD[] = { "START", "END", "ORIGIN", "EQU", "LTORG" };
    static String DS[] = { "DC", "DS" };
    static String CC[] = { "LT", "LE", "EQ", "GT", "GE", "ANY" };
    static int symCounter = 0;
    static int litCounter = 0;

    static String symbolTable[][] = new String[100][2];
    static String literalTable[][] = new String[100][2];

    // static String poolTable[][]=new String[100][2];
    public static void main(String args[]) throws Exception {
        int locate = 0;
        int litCount = 0, stp = 0;

        File file1 = new File("input.asm");
        File file2 = new File("output.asm");

        BufferedReader reader = new BufferedReader(new FileReader(file1));
        BufferedWriter writer = new BufferedWriter(new FileWriter((file2)));

        String line, token, ans = "", prev = "";
        String buffer = "";

        while ((line = reader.readLine()) != null) {
            int ISflag = 0;
            StringTokenizer splitted = new StringTokenizer(line);
            ans="";

            while (splitted.hasMoreTokens()) {
                token = splitted.nextToken();
                if (token.equals("START")) {
                    locate = Integer.parseInt(splitted.nextToken());
                    ans = "(AD,01)(C," + locate + ")";
                    break;
                } else {
                    if (searchIS(token)) {
                        if (token.equals("STOP")) {
                            stp = 1;
                        }
                        ISflag = 1;
                        // ans = "";
                        ans += "(IS," + Integer.toString(getISIndex(token)) + ")";
                        locate++;
                    } 
                    
                    else if (searchDS(token)) {
                        if (token.equals("DC")) {
                            ans = "";
                            ans += "(DL,1)(C," + splitted.nextToken() + ")";
                        }
                        if (token.equals("DS")) {
                            ans = "";
                            ans += "(DL,2)(C," + splitted.nextToken() + ")";
                        }
                        locate++;
                    } 
                    
                    else if (searchAD(token)) {
                        if (token.equals("LTORG")) {
                            // locate = locate + litCount;
                            // ans = "(AD,05)\n";
                            // while (litCount > 0) {
                            //     literalTable[litCounter - litCount][1] = Integer.toString(locate - litCount);
                            //     int len = literalTable[litCounter - litCount][0].length();
                            //     String temp = literalTable[litCounter - litCount][0].substring(2, len - 1);
                            //     ans += "(DL,01)(C," + temp + ")";
                            //     litCount--;
                            //     if (litCount != 0)
                            //         ans += "\n";
                            // }

                            if (litCount > 0) {
                                int startLitIndex = litCounter - litCount; // starting literal index of this pool
                                for (int i = 0; i < litCount; i++) {
                                    int litIndex = startLitIndex + i;
                                    literalTable[litIndex][1] = Integer.toString(locate);
                                    String lit = literalTable[litIndex][0];
                                    int len = lit.length();
                                    String temp = lit.substring(2, len - 1); // extract value inside literal e.g. =’5’
                                    ans += "(DL,01)(C," + temp + ")";
                                    if (i != litCount - 1)
                                        ans += "\n";
                                    locate++;  // increment location for each literal assigned
                                }
                                litCount = 0; // reset literal count after assignment
                            }

                        }

                        if (token.equals("ORIGIN")) {
                            token = splitted.nextToken();
                            String[] words = token.split("\\+");
                            int location = Integer.parseInt(symbolTable[index(words[0])][1]);
                            locate = location + Integer.parseInt(words[1]);
                            ans = "(AD,03)(S," + (index(words[0]) + 1) + ")+" + words[1];
                        }

                        if (token.equals("END") && litCount != 0) {
                            // locate += litCount;
                            // ans = "(AD,02)\n";

                            // while (litCount > 0) {
                            //     literalTable[litCounter - litCount][1] = Integer.toString(locate - litCount);
                            //     int len = literalTable[litCounter - litCount][0].length();
                            //     String temp = literalTable[litCounter - litCount][0].substring(2, len - 1);
                            //     ans += "(DL,01)(C," + temp + ")";
                            //     litCount--;
                            //     if (litCount != 0)
                            //         ans += "\n";
                            // }

                            if (litCount > 0) {
                                int startLitIndex = litCounter - litCount; // starting literal index of this pool
                                for (int i = 0; i < litCount; i++) {
                                    int litIndex = startLitIndex + i;
                                    literalTable[litIndex][1] = Integer.toString(locate);
                                    String lit = literalTable[litIndex][0];
                                    int len = lit.length();
                                    String temp = lit.substring(2, len - 1); // extract value inside literal e.g. =’5’
                                    ans += "(DL,01)(C," + temp + ")";
                                    if (i != litCount - 1)
                                        ans += "\n";
                                    locate++;  // increment location for each literal assigned
                                }
                                litCount = 0; // reset literal count after assignment
                            }
                        }

                        if (token.equals("EQU")) {
                            int temp = index(splitted.nextToken());
                            token = prev;
                            symbolTable[index(prev)][1] = symbolTable[temp][1];
                            ans = "";
                        }
                    } 
                    
                    else {
                        prev = token;
                        char[] x = token.toCharArray();
                        if (x[0] == '=') {
                            int z = litCounter;
                            ans += "(L," + z + 1 + ")";
                            literalTable[litCounter][0] = token;
                            literalTable[litCounter][1] = null;
                            litCount++;
                            litCounter++;
                        } 
                        else if (token.equals("AREG")) {
                            ans += "(1)";
                        } 
                        else if (token.equals("BREG")) {
                            ans += "(2)";
                        } 
                        else if (token.equals("CREG")) {
                            ans += "(3)";
                        } 
                        else if (token.equals("DREG")) {
                            ans += "(4)";
                        } 
                        else if (searchCC(token)) {
                            ans += "(CC," + Integer.toString(indexCC(token) + 1) + ")";
                        } 
                        else {
                            if (!searchSym(token) && ISflag == 0 && stp == 0) {
                                symbolTable[symCounter][0] = token;
                                symbolTable[symCounter][1] = Integer.toString(locate);
                                symCounter++;
                                ans += "(S," + Integer.toString(index(token) + 1) + ")";
                                if (splitted.hasMoreTokens())
                                    ans = "";
                            } 
                            else if (!searchSym(token) && ISflag == 1 && stp == 0) {
                                // forward reference so store only the token not the LC
                                symbolTable[symCounter][0] = token;
                                symCounter++;
                                ans += "(S," + Integer.toString(index(token) + 1) + ")";
                            } 
                            else if (searchSym(token) && ISflag == 0) {
                                // Symbol found in symbol table, instruction not found
                                symbolTable[index(token)][1] = Integer.toString(locate);
                                ans += "(S," + Integer.toString(index(token) + 1) + ")";
                                if (splitted.hasMoreTokens())
                                    ans = "";
                                prev = token;
                            } 
                            else {
                                // Catches all other scenarios (like symbol found and isflag == 1 or no more
                                // tokens left)
                                if (!splitted.hasMoreTokens()) {
                                    ans += "(S," + Integer.toString(index(token) + 1) + ")";
                                }
                                continue;
                            }
                        }
                    }
                }
            }
            ans = ans + "\n";
            buffer += ans;

        }
        System.out.println(buffer + "\n");
        System.out.println("Symbol Table : ");
        for (int i = 0; i < symCounter; i++) {
            System.out.print(symbolTable[i][0] + "\t");
            System.out.println(symbolTable[i][1]);
        }
        System.out.println("Literal Table : ");
        for (int i = 0; i < litCounter; i++) {
            System.out.print(literalTable[i][0] + "\t");
            System.out.println(literalTable[i][1]);
        }
        writer.write(buffer);

        reader.close();
        writer.close();
    }

    public static boolean searchIS(String s) {
        boolean flag = false;
        int i = 0;
        while (i < 11) {
            if (IS[i].equals(s)) {
                flag = true;
                break;
            }
            i++;
        }
        return flag;
    }

    public static boolean searchDS(String s) {
        boolean flag = false;
        int i = 0;
        while (i < 2) {
            if (DS[i].equals(s)) {
                flag = true;
                break;
            }
            i++;
        }
        return flag;
    }

    public static boolean searchAD(String s) {
        boolean flag = false;
        int i = 0;
        while (i < 5) {
            if (AD[i].equals(s)) {
                flag = true;
                break;
            }
            i++;
        }
        return flag;
    }

    public static boolean searchCC(String s) {
        boolean flag = false;
        int i = 0;
        while (i < 6) {
            if (CC[i].equals(s)) {
                flag = true;
                break;
            }
            i++;
        }
        return flag;
    }

    public static boolean searchSym(String s) {
        boolean flag = s.equals("BREG") || s.equals("AREG") || s.equals("CREG") || s.equals("DREG") || s.equals(",")
                || s.equals("LE") || s.equals("LT") || s.equals("ANY") || s.equals("EQ") || s.equals("GT")
                || s.equals("GE");
        int i = 0;
        while (i < symCounter) {
            if (symbolTable[i][0].equals(s)) {
                flag = true;
                break;
            }
            i++;
        }
        return flag;

    }

    public static int getISIndex(String s) {
        int i = 0;
        while (i < 11) {
            if (IS[i].equals(s))
                break;
            i++;
        }

        return i;
    }

    public static int indexCC(String s) {
        int i = 0;
        while (i < 6) {
            if (CC[i].equals(s))
                break;
            i++;
        }

        return i;
    }

    public static int index(String s) {
        int i = 0;
        while (i < symCounter) {
            if (symbolTable[i][0].equals(s)) {
                break;
            }
            i++;
        }
        return i;
    }
}
