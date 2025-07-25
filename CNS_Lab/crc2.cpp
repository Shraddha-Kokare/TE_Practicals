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
    // string codeWord = "", xored = "";
    // int gen_len = generator.length();
    // string appended = binary_inp + string(gen_len - 1, '0');
    // string remainder = appended.substr(0, gen_len);

    // cout << "\nAppended string: " << appended;
    // // cout << "\nRemainder : " << remainder;

    // for (int i = gen_len; i <= appended.length(); i++)
    // {
    //     cout << "\ni = " << i << "\tRemainder : " << remainder << "\tGenerator: " << generator;
    //     if (remainder[i] == '1')
    //     {
    //         xored = xorStrings(remainder, generator);
    //         if (i < appended.length() - 1)
    //             remainder += appended[i];
    //         else
    //             remainder += '\0';
    //     }
    //     else
    //     {
    //         remainder = remainder.substr(1);
    //         if (i < appended.length() - 1)
    //             remainder += appended[i];
    //         else
    //             remainder += '\0';
    //     }
    // }
    // cout << "\nRemainder : " << remainder;

    // codeWord = binary_inp + remainder.substr(1);
    // return codeWord;
    int genLen = generator.length();
    string appendedData = binary_inp + string(genLen - 1, '0');
    string remainder = appendedData.substr(0, genLen);

    for (int i = genLen; i <= appendedData.length(); i++)
    {
        if (remainder[0] == '1')
        {
            remainder = xorStrings(generator, remainder) + (i < appendedData.length() ? appendedData[i] : '\0');
        }
        else
        {
            remainder = xorStrings(string(genLen, '0'), remainder) + (i < appendedData.length() ? appendedData[i] : '\0');
        }
    }

    string crc = remainder.substr(0, genLen - 1);
    return binary_inp + crc;
}

bool crcCheck(string received, string generator)
{
    int genLen = generator.length();
    string remainder = received.substr(0, genLen);

    for (int i = genLen; i <= received.length(); i++)
    {
        if (remainder[0] == '1')
        {
            remainder = xorStrings(generator, remainder) + (i < received.length() ? received[i] : '\0');
        }
        else
        {
            remainder = xorStrings(string(genLen, '0'), remainder) + (i < received.length() ? received[i] : '\0');
        }
    }

    for (char c : remainder.substr(0, genLen - 1))
    {
        if (c != '0')
            return false;
    }
    return true;
}

string injectNoise(string codeword)
{
    srand(time(0));
    int pos = rand() % codeword.length();
    codeword[pos] = (codeword[pos] == '0') ? '1' : '0';
    cout << "Noise injected at bit position: " << pos << endl;
    return codeword;
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

    string codeWord = getCodeWord(binary_rep, generator);
    cout << "\nCodeword is :" << codeWord;

    char choice;
    cout << "\nDo you want to inject noise? (y/n): ";
    cin >> choice;

    string received = codeWord;

    if (choice == 'y' || choice == 'Y')
    {
        received = injectNoise(received);
        cout << "Received codeword with noise: " << received << endl;
    }
    else
    {
        cout << "Received codeword (no noise): " << received << endl;
    }

    cout << "\nReceiver Side :\n" << endl;
    if (crcCheck(received, generator))
    {
        cout << " No Error Detected (Data is correct)." << endl;
    }
    else
    {
        cout << " Error Detected in Data!" << endl;
    }

    return 0;
}
