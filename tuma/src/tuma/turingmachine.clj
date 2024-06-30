(ns tuma.turingmachine
  (:require [tuma.predicates :as preds]
            [tuma.messages :as msg]))

(defrecord -turingmachine [states input-symbols tape-symbols transitions initial-state blank-symbol accepted-states])

(defn new-turingmachine [states input-symbols tape-symbols transitions initial-state blank-symbol accepted-states]
  (assert (preds/valid-tm? states input-symbols tape-symbols transitions initial-state blank-symbol accepted-states))
  (-turingmachine. states input-symbols tape-symbols transitions initial-state blank-symbol accepted-states))

(defn- find-transition [source read-symbol transitions]
  (assert (keyword? source))
  (assert Character (type read-symbol))
  (first (filter #(and (= (:source %) source) (= (:reads %) read-symbol)) transitions)))

(defn- write-tape [tape head-position symbol]
  (str (subs tape 0 head-position) symbol (subs tape (+ head-position 1))))

(defn- execute-transitions [tape transitions head-position current-state blank-symbol]
  (let [read-symbol (if (preds/inside-word? tape head-position)
                        (str (nth tape head-position))
                        blank-symbol)
        transition (find-transition current-state read-symbol transitions)]
    (cond
      (nil? transition)
        {:current-state current-state :tape tape}

      (= head-position (count tape))
        (recur (str tape blank-symbol) transitions head-position current-state blank-symbol)

      (= head-position -1)
        (recur (str blank-symbol tape) transitions 0 current-state blank-symbol)

      :else
      (let [new-tape (write-tape tape head-position (:writes transition))
            newHeadPosition (if (= (:direction transition) :right)
                                (inc head-position)
                                (dec head-position))]
        (recur new-tape transitions newHeadPosition (:destination transition) blank-symbol)))))

(defn- get-result [input output end-state verdict]
  {:input input :output output :end-state end-state :verdict verdict})

(defn emulate-tm [tm word]
  (assert (= (type tm) -turingmachine) (msg/format-not-expected-type tm "Turingmachine"))
  (assert (string? word) (msg/format-not-expected-type word "string"))
  (cond
    (preds/valid-word? word (:input-symbols tm))
    (get-result word word (:initial-state tm) :rejected)

    :else
    (let [result (execute-transitions word (:transitions tm) 0 (:initial-state tm) (:blank-symbol tm))]
      (if (contains? (set (:accepted-states tm)) (:current-state result))
        (get-result word (:tape result) (:current-state result) :accepted)
        (get-result word (:tape result) (:current-state result) :rejected)))))
