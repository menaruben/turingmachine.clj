(ns tuma.messages)

(defn format-not-expected-type [val typestring]
  (str val " is not a " typestring "!"))

(defn format-not-defined [val typestring]
  (str val " is not a defined " typestring "!"))

(defn format-invalid-type-of [typestring]
  (str "invalid " typestring "!"))

(defn format-transition [transition]
  (str "source: " (:source transition)
       " reads: " (:reads transition)
       " destination: " (:destination transition)
       " writes: " (:writes transition)
       " direction: " (:direction transition)))
