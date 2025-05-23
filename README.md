Amazing Numbers
Amazing Numbers is a Java console application that analyzes numbers for a variety of interesting mathematical properties. Users can query single numbers or ranges with complex filters, including inclusion and exclusion of specific properties. The program supports mutually exclusive properties and offers detailed feedback on invalid or conflicting inputs.

Features
Analyze properties such as:
even, odd, buzz, duck, palindromic, gapful, spy, square, sunny, jumping, happy, sad

Query a single number or a list of consecutive numbers

Filter results by including or excluding multiple properties (e.g., even -duck)

Detect and warn about mutually exclusive properties or conflicting filters

User-friendly instructions and error messages

Efficient calculation with proper input validation

Supported Requests
Enter a natural number to display its properties

Enter two natural numbers:

First: starting number

Second: how many numbers to process

Enter two numbers followed by properties to filter results

Properties can be prefixed with - to exclude them from results

Enter 0 to exit the program

Example Usage
csharp
Copy
Enter a request: 1 10
1 is odd, palindromic, spy, square, jumping, happy
2 is even, palindromic, spy, jumping, sad
...

Enter a request: 1 5 -odd
2 is even, palindromic, spy, jumping, sad
4 is even, palindromic, spy, square, jumping, sad
...

Enter a request: 0
Goodbye!
How to Run
Make sure you have Java 17 or later installed.

Compile the project:
javac Main.java

Run the program:
java numbers.Main

Project Structure
Main.java — main program with number property logic and user interaction

Uses Java collections and string formatting for output

Possible Improvements
Add caching for faster repeated queries

Implement GUI or web frontend

Support more numerical properties

Optimize property calculations for very large numbers

License
MIT License
