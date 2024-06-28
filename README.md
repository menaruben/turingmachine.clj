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
You can find the examples [here](./src/test/tuma/).

## invert input word
```clojure
(ns test.tuma.Invert
  (:require [main.tuma.transition :as trans]
            [main.tuma.turingmachine :as tm])
  (:import [main.tuma.transition Transition])
  (:import [main.tuma.turingmachine Turingmachine]))

;; turingmachine which inverts the input word, which must be of length 1 atleast
(def t1 (Transition. :q1 "_" :q1 "_" :right))
(def t2 (Transition. :q1 "0" :q2 "1" :right))
(def t3 (Transition. :q1 "1" :q2 "0" :right))
(def t4 (Transition. :q2 "0" :q2 "1" :right))
(def t5 (Transition. :q2 "1" :q2 "0" :right))

(def transitions (list t1 t2 t3 t4 t5))
(def states (list :q1 :q2))
(def input-symbols (list "0" "1" "_"))
(def tape-symbols (list "0" "1" "_"))
(def accepted-states (list :q2))
(def tm (Turingmachine. states input-symbols tape-symbols transitions :q1 "_" accepted-states))
(def inputs (list "000" "111" "10111011" "0" "1" "" "abc" "___110001"))
(doseq [input inputs] (println (tm/emulate-tm tm input)))
```
```clojure
{:input 000, :output 111, :endState :q2, :verdict :accepted}
{:input 111, :output 000, :endState :q2, :verdict :accepted}
{:input 10111011, :output 01000100, :endState :q2, :verdict :accepted}
{:input 0, :output 1, :endState :q2, :verdict :accepted}
{:input 1, :output 0, :endState :q2, :verdict :accepted}
{:input , :output , :endState :q1, :verdict :rejected}
{:input abc, :output abc, :endState :q1, :verdict :rejected}
{:input ___110001, :output ___001110, :endState :q2, :verdict :accepted}
```

## input word must have prefix 00
```clojure
(ns test.tuma.PrefixTwoZeros
  (:require [main.tuma.turingmachine :as tm]
            [main.tuma.transition])
  (:import [main.tuma.transition Transition])
  (:import [main.tuma.turingmachine Turingmachine]))

;; turingmachine which accepts the language {0^2 1^n | n > 0}
;; <=> the language of all binary strings with 00 as the prefix
(def t1 (Transition. :q1 "0" :q3 "0" :right))
(def t2 (Transition. :q3 "0" :q2 "0" :right))
(def t3 (Transition. :q2 "1" :q2 "1" :right))
(def transitions (list t1 t2 t3))
(def states (list :q1 :q2 :q3))
(def input-symbols (list "0" "1" "_"))
(def tape-symbols (list "0" "1" "_"))
(def accepted-states (list :q2))
(def tm (Turingmachine. states input-symbols tape-symbols transitions :q1 "_" accepted-states))

(def inputs (list "00111111" "0111" "00"))
(doseq [input inputs] (println (tm/emulate-tm tm input)))
```
```clojure
{:input 00111111, :output 00111111, :endState :q2, :verdict :accepted}
{:input 0111, :output 0111, :endState :q3, :verdict :rejected}
{:input 00, :output 00, :endState :q2, :verdict :accepted}
```

## add leading one infront of input
```clojure
(ns test.tuma.AddOneInfront
  (:require [main.tuma.turingmachine :as tm]
            [main.tuma.transition])
  (:import [main.tuma.transition Transition])
  (:import [main.tuma.turingmachine Turingmachine]))

(def t1 (Transition. :q1 "1" :q3 "1" :left))
(def t2 (Transition. :q1 "0" :q3 "0" :left))
(def t3 (Transition. :q3 "_" :q2 "1" :left))
(def transitions (list t1 t2 t3))

(def states (list :q1 :q2 :q3))
(def input-symbols (list "0" "1"))
(def tape-symbols (list "0" "1" "_"))
(def accepted-states (list :q2))

(def tm (Turingmachine. states input-symbols tape-symbols transitions :q1 "_" accepted-states))
(def inputs (list "000" "111" "10111011" "0" "1"))
(doseq [input inputs] (println (tm/emulate-tm tm input)))
```
```clojure
{:input 000, :output 1000, :endState :q2, :verdict :accepted}
{:input 111, :output 1111, :endState :q2, :verdict :accepted}
{:input 10111011, :output 110111011, :endState :q2, :verdict :accepted}
{:input 0, :output 10, :endState :q2, :verdict :accepted}
{:input 1, :output 11, :endState :q2, :verdict :accepted}
```

## binary is even
```clojure
(ns test.tuma.IsEven
  (:require [main.tuma.turingmachine :as tm]
            [main.tuma.transition])
  (:import [main.tuma.transition Transition])
  (:import [main.tuma.turingmachine Turingmachine]))

(def t1 (Transition. :q1 "1" :q1 "1" :right))
(def t2 (Transition. :q1 "0" :q1 "0" :right))
(def t3 (Transition. :q1 "_" :q3 "_" :left))
(def t4 (Transition. :q3 "0" :q2 "0" :right))
(def transitions (list t1 t2 t3 t4))

(def states (list :q1 :q2 :q3))
(def input-symbols (list "0" "1"))
(def tape-symbols (list "0" "1" "_"))
(def accepted-states (list :q2))

(def tm (Turingmachine. states input-symbols tape-symbols transitions :q1 "_" accepted-states))
(def inputs (list "000" "111" "10111011" "0" "1"))
(doseq [input inputs] (println (tm/emulate-tm tm input)))
```
```clojure
{:input 000, :output 000_, :endState :q2, :verdict :accepted}
{:input 111, :output 111_, :endState :q3, :verdict :rejected}
{:input 10111011, :output 10111011_, :endState :q3, :verdict :rejected}
{:input 0, :output 0_, :endState :q2, :verdict :accepted}
{:input 1, :output 1_, :endState :q3, :verdict :rejected}
```
