[![Build Status](https://travis-ci.com/mtumilowicz/java11-vavr093-partial-function-lifting-workshop.svg?branch=master)](https://travis-ci.com/mtumilowicz/java11-vavr093-partial-function-lifting-workshop)
[![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

# java11-vavr093-partial-function-lifting-workshop

# project description
* https://www.vavr.io/vavr-docs/#_lifting
* https://static.javadoc.io/io.vavr/vavr/0.9.3/io/vavr/PartialFunction.html
* https://github.com/mtumilowicz/java11-vavr-function-lifting
* on the workshop we will try to fix failing `Workshop`
* answers: `Answers` (same tests as in `Workshop` but correctly solved)

# theory in a nutshell
* a partial function from `X` to `Y` is a function `f: K → Y`, 
  for some `K c X`. For `x e X\K` function is undefined
* in programming, if partial function is called with a disallowed 
  input value, it will typically throw an exception
* partial function (to set intuition)
    ```
    int do(int positive) {
        if (first < 0) {
                throw new IllegalArgumentException("Only positive integers are allowed"); 
        }
        // other stuff
    }
    ```
* vavr's partial function interface
    ```
    public interface PartialFunction<T, R> {
        R apply(T t);
    
        boolean isDefinedAt(T value); // tests if a value is contained in the function's domain.
    }
    ```
    * the caller is responsible for calling the method isDefinedAt() before this function is applied to the value.
    * if the function is not defined for a specific value, apply() may produce an arbitrary result.
        * in particular - even random values
        * more specifically it is not guaranteed that the function will throw an exception
    * above example rewritten with vavr
        ```
        class RandomIdentityAnswer implements PartialFunction<Integer, Integer> {
            
            @Override
            public Integer apply(Integer o) {
                if (!isDefinedAt(o)) {
                    throw new IllegalArgumentException("Only positive integers are allowed");
                }
                // other stuff
            }
        
            @Override
            public boolean isDefinedAt(Integer value) {
                return nonNull(value) && value > 0;
            }
        }
        ```
* It generalizes the concept of a function `f: X → Y` by not forcing `f` to map every element of `X` to an element 
    of `Y`
    * That means a partial function works properly only for some input values
    * If the function is called with a disallowed input value, it will typically throw an exception
* In programming - we usually **lift** function `f: (K c X) -> Y` to `g: X -> Option<Y>` in such a manner:
    * A lifted function returns Some, if the function is invoked with allowed input values.
        * `g(x).get() = f(x)` on `K`
    * A lifted function returns None instead of throwing an exception, if the function is invoked with disallowed 
        * `g(x) = Option.none()` for `x e X\K`
input values
# conclusions in a nutshell