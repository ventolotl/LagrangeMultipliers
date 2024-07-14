# Lagrange Multipliers

## Introduction
To find the maxima and minima of a function $f(x,y)$ subject to a constraint $g(x,y)=0$, one can use the method of Lagrange multipliers.
All these points must satisfy the equation $\nabla f = \lambda \nabla g$.

To visualize this, one can create a contour plot of $f$ while also plotting $g$, and examine the gradients of $f$ and $g$ at various points. 
A point $(x,y)$ is a solution to the optimization problem if and only if it lies on the curve defined by $g$ and the gradient of $g$ points in the same (or opposite) direction as the gradient of $f$.

## Some techniques

### Contour plot
The contour plot is created by iterating through various values of z and thereafter solving the equation $f(x,y)=z$. 
By fixing z, all points $(x,y)$ which satisfy the equation are colored similarly.
The equation $f(x,y)=z$ is then solved by Newton's method while also fixing one of the two variables to project the function to a single-valued function.
The initial guesses are determined by the [intermediate value theorem](https://en.wikipedia.org/wiki/Intermediate_value_theorem).

### Finding the optimization solutions
To find a solution to the optimization problem, one must find $(x,y)$ which satisfy $\nabla f = \lambda \nabla g$ subject to $g(x,y)=0$.
This translates into the equations $f_x = \lambda g_x$, $f_y = \lambda g_y$ and $g(x,y)=0$. 
To find solutions - which means to satisfy all three equations for same $(x,y)$ -, a [multidimensional formulation of Newton's method](https://en.wikipedia.org/wiki/Newton's_method#Systems_of_equations) is used.


## Preview
The following plot was created by the program with $f(x,y)=x^2+y^2$ and $g(x,y)=xy \cdot \cos(y) - 4$:

<img src="https://github-production-user-asset-6210df.s3.amazonaws.com/43090398/348569553-a8a2267e-f451-4a2b-901f-739cfa322e68.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAVCODYLSA53PQK4ZA%2F20240714%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20240714T204818Z&X-Amz-Expires=300&X-Amz-Signature=759427c4d888e7fed57c5104f45a37b4e159b16e78c7fef6b9321252b4c5bf5d&X-Amz-SignedHeaders=host&actor_id=43090398&key_id=0&repo_id=736715605" />

## Some problems
Note that this program is not perfect. There are certainly many ways to improve parts of the program, especially when it comes to solving the three equations $f_x = \lambda g_x$, $f_y = \lambda g_y$ and $g(x,y)=0$.
For very weird functions, it is therefore possible that solutions are not found by the program or that the provided function is not rendered correctly, as the calculated points are too far away from each other. 
