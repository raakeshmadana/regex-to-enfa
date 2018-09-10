# Convert Regular Expression to e-NFA
___
## Preprocessing
- `.` is used to represent concatenation
    - Makes it easier to evaluate the expression
    - `.` is actually an extended operator to match any character. Since the regular expression only contains basic operators, it can be used for concatenation
- Regular Expression is converted from Infix expression to Postfix
    - `*` is already in Postfix notation. So, it was treated as an operand/character during the conversion

## Compilation
Go to `src` folder and run this command  
`javac -d ../../bin *.java`  
Classes will be placed in `bin` directory. Feel free to change the path as you wish

## Run
`java -cp <path to bin> assignment1.Assignment1 <input regex>`

## Things to Note in the Output
- e-NFA is printed as Adjacency List and Adjacency Matrix
- `null` is used to represent the absence of a transition in the Adjacency Matrix
- Unicode codepoint `03b5` is used to represent epsilon transitions