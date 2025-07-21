#include <iostream>
#include <string>
using namespace std;
string toBinary(string input)
{
    string binary_rep = "";
    for (char ch : input)
    {
        for (int i = 7; i >= 0; i--)
        {
            binary_rep += ((ch >> i) & 1) ? '1' : '0';
        }
    }

    return binary_rep;
}

string xorStrings(string str1, string str2)
{
    string result = "";
    for (int i = 1; i < str2.length(); i++)
    {
        result += ((str1[i] == str2[i]) ? '0' : '1');
    }

    return result;
}

string getCodeWord(string binary_inp, string generator)
{
    string codeWord = "", xored = "";
    int gen_len = generator.length();
    string appended = binary_inp + string(gen_len - 1, '0');
    string remainder = appended.substr(0, gen_len);

    for (int i = gen_len; i <= appended.length(); i++)
    {
        if (remainder[i] == '1')
        {
            xored = xorStrings(remainder, generator);
            if (i < appended.length() - 1)
                remainder += appended[i];
            else
                remainder += '\0';
        }
        else
        {
            remainder = remainder.substr(1);
            if (i < appended.length() - 1)
                remainder += appended[i];
            else
                remainder += '\0';
        }
    }

    codeWord = binary_inp + remainder.substr(1);
    return codeWord;
}

int main()
{
    string inputString, generator;
    cout << "\nEnter the input string: ";
    cin >> inputString;
    string binary_rep = toBinary(inputString);
    cout << "\nBinary Representation :" << binary_rep;

    cout << "\nEnter Generator bits: ";
    cin >> generator;

    return 0;
}
