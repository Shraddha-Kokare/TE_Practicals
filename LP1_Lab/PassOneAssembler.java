import java.io.*;
import java.util.*;

public class PassOneAssembler
{
    static Scanner sc=new Scanner(System.in);
    static String IS[]={"STOP","ADD","SUB","MULT","MOVER","MOVEM","COMP","BC","DIV","READ","PRINT"};
    static String AD[]={"START","END","ORIGIN","EQU","LTORG"};
    static String DS[]={"DC","DS"};
    static String CC[]={"LT","LE","EQ","GT","GE","ANY"};
    static int symCounter=0;
    static int litCouneter=0;
    
    static String symbolTable[][]=new String[100][2];
    static String literalTable[][]=new String[100][2];
    static String poolTable[][]=new String[100][2];
    public static void main(String args[]) throws Exception
    {
        int locate=0;
        int litCounter=0;

        File file1=new File("input.asm");
        File file2=new File("output.asm");

        BufferedReader reader=new BufferedReader(new FileReader(file1));
        BufferedWriter writer=new BufferedWriter(new FileWriter((file2)));
        
        String line,token,ans;

        while((line=reader.readLine())!=null)
        {
            StringTokenizer splitted=new StringTokenizer(line);

            while (splitted.hasMoreTokens()) {
                token=splitted.nextToken();
                if(token.equals("START"))
                {
                    locate=Integer.parseInt(splitted.nextToken());
                    ans="(AD,01)(C,"+locate+")";
                    break;
                }
                else{
                    if(searchIS(token))
                    {
                        ans="";
                        ans+="(IS,"+Integer.toString(getISIndex(token))+")";
                        locate++;
                    }
                    else if(searchDS(token))
                    {
                        if(token.equals("DC"))
                        {
                            ans="";
                            ans="(DL,1)(C,"+splitted.nextToken()+")";
                        }
                        if(token.equals("DS"))
                        {
                            ans="";
                            ans="(DL,1)(C,"+splitted.nextToken()+")";
                        }
                        locate++;
                    }
                    else if(searchAD(token))
                    {

                    }
                    else{

                    }
                }
            }
        }
    }

    public static boolean searchIS(String s)
    {
        boolean flag=false;
        int i=0;
        while (i<11) {
            if(IS[i].equals(s))
            {
                flag=true;
                break;
            }
            i++;
        }
        return flag;
    }

    public static boolean searchDS(String s)
    {
        boolean flag=false;
        int i=0;
        while (i<2) {
            if(DS[i].equals(s))
            {
                flag=true;
                break;
            }
            i++;
        }
        return flag;
    }

    public static boolean searchAD(String s)
    {
        boolean flag=false;
        int i=0;
        while (i<5) {
            if(AD[i].equals(s))
            {
                flag=true;
                break;
            }
            i++;
        }
        return flag;
    }


    public static int getISIndex(String s)
    {
        int i=0;
        while (i<11) {
            if(IS[i].equals(s))
                break;
            i++;
        }

        return i;
    }
}
