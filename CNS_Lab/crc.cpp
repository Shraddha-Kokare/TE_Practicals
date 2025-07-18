#include <iostream>
#include <bitset>
#include <string>
#include <cstdlib>
#include <ctime>

using namespace std;

// Convert input string to binary using ASCII values
string stringToBinary(const string& input) {
    string binary = "";
    for (char ch : input) {
        binary += bitset<8>(ch).to_string();  // 8-bit binary
    }
    return binary;
}

// XOR operation between two binary strings
string xorStrings(string a, string b) {
    string result = "";
    for (int i = 1; i < b.length(); i++) {
        result += (a[i] == b[i]) ? '0' : '1';
    }
    return result;
}

// CRC Encoding Function
string crcEncode(string binaryData, string generator) {
    int genLen = generator.length();
    string appendedData = binaryData + string(genLen - 1, '0');
    string remainder = appendedData.substr(0, genLen);

    for (int i = genLen; i <= appendedData.length(); i++) {
        if (remainder[0] == '1') {
            remainder = xorStrings(generator, remainder) + (i < appendedData.length() ? appendedData[i] : '\0');
        } else {
            remainder = xorStrings(string(genLen, '0'), remainder) + (i < appendedData.length() ? appendedData[i] : '\0');
        }
    }

    string crc = remainder.substr(0, genLen - 1);
    return binaryData + crc;
}

// CRC Checking Function
bool crcCheck(string received, string generator) {
    int genLen = generator.length();
    string remainder = received.substr(0, genLen);

    for (int i = genLen; i <= received.length(); i++) {
        if (remainder[0] == '1') {
            remainder = xorStrings(generator, remainder) + (i < received.length() ? received[i] : '\0');
        } else {
            remainder = xorStrings(string(genLen, '0'), remainder) + (i < received.length() ? received[i] : '\0');
        }
    }

    for (char c : remainder.substr(0, genLen - 1)) {
        if (c != '0') return false;
    }
    return true;
}

// Function to inject noise (flip a random bit)
string injectNoise(string codeword) {
    srand(time(0));
    int pos = rand() % codeword.length();
    codeword[pos] = (codeword[pos] == '0') ? '1' : '0';
    cout << "Noise injected at bit position: " << pos << endl;
    return codeword;
}

// Main Function
int main() {
    string input, generator;
    cout << "Enter input string: ";
    getline(cin, input);

    cout << "Enter generator polynomial (binary): ";
    cin >> generator;

    string binaryData = stringToBinary(input);
    cout << "Binary representation of input: " << binaryData << endl;

    string encoded = crcEncode(binaryData, generator);
    cout << "Transmitted Codeword (Data + CRC): " << encoded << endl;

    // Ask user if they want to inject noise
    char choice;
    cout << "\nDo you want to inject noise? (y/n): ";
    cin >> choice;

    string received = encoded;
    if (choice == 'y' || choice == 'Y') {
        received = injectNoise(received);
        cout << "Received codeword with noise: " << received << endl;
    } else {
        cout << "Received codeword (no noise): " << received << endl;
    }

    // Check at receiver
    cout << "\n--- Receiver Side ---" << endl;
    if (crcCheck(received, generator)) {
        cout << " No Error Detected (Data is correct)." << endl;
    } else {
        cout << " Error Detected in Data!" << endl;
    }

    return 0;
}
