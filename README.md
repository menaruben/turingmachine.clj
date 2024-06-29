# turingmachine.clj
A simple way to simulate (deterministic) turingmachines using clojure!

> Note that I would like to optimize this repository and also make use of the "gödel number" in the future. This is just a simple implementation.

# Definition: Turingmachine
A turingmachine is defined by the following septuple:
$$ TM = (Q, \Sigma, \Gamma, \delta, q_0, \square, F) $$
where
- $Q$ = finite set of states
- $\Sigma$ = input symbols/alphabet
- $\Gamma$ = tape symbols/alphabet
- $\delta$ = a set of state transitions in the form of $(state_{now}, reads) = (state_{after}, writes, direction)$
- $q_0$ = initial state with $q_0 \in Q$
- $\square$ = the "blank" symbol
- $F$ = the set of accepted states

The turingmachine has a tape which can be infinite in both directions. We are also only allowed to move a step to the left or right.

Let's assume that we have the following turingmachine:

![](./imgs/tm_example1.png)

This turingmachine only accepts words/inputs which are atleast 1 character long and start with 0 or 1 (ignores the blanks at the beginning).
All of the accepted states are typically circled twice. In this example we only have one accepted state which is $q_2$.
$q_1$ on the other hand is the initial state which means:

$$ Q = \set{q_1, q_2} $$

$$ F = \set{q_2} $$

Since our input words only consist of 0, 1 and _ (blank) and we only write 0, 1 or _ (blank) to the tape we can therefore say:

$$ \Sigma = \set{0, 1, \\_} $$

$$ \Gamma = \set{0, 1, \\_} $$

$$ \square = \\_ $$

Now we can take a look at the state transitions. Let's start with $q_1$:
$$0:1,R$$ means "reads 0, writes 1, goes right". Since we start at $q_1$ and end at $q_2$ with this transition we can write it as such: $$(q_1, 0) = (q_2, 1, R)$$
We can then do this for the rest of the transitions and should end up with these...
- $\delta_1(q_1, \\_) = (q_1, \_, R)$
- $\delta_2(q_1, 0) = (q_2, 1, R)$
- $\delta_3(q_1, 1) = (q_2, 0, R)$
- $\delta_4(q_2, 0) = (q_2, 1, R)$
- $\delta_5(q_2, 1) = (q_2, 0, R)$

We can now successfully *configure* a turingmachine and read a turingmachine visualization!

# Examples
You can find some examples in the [core test](./tuma/test/tuma/core_test.clj).
