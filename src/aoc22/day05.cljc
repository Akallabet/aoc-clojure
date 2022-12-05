(ns aoc22.day05
  (:require #?(:clj [clojure.java.io :as io]
               :cljs [nbb.core :refer [slurp await]])
            [clojure.string :as str]
            [clojure.edn :as edn]
            #?(:cljs [promesa.core :as p])))

(defn parse-instructions [instructions]
  (->> instructions
       str/split-lines
       (map #(str/split % #" "))
       (map
        (fn
          [instr]
          {:amount (- (edn/read-string (get instr 1)) 1)
           :from (- (edn/read-string (get instr 3)) 1)
           :to (- (edn/read-string (get instr 5)) 1)}))))

(defn move [stacks instr]
  (conj (vec (take (instr :from) stacks))
        (drop (:amount instr) (stacks (:from instr)))
        (subvec stacks (+ (:from instr) 1) (:to instr))
        (concat
         (take (:amount instr) (stacks (:from instr)))
         (stacks (:to instr)))))

(defn supply-stacks
  [stacks instructions]
  (cond
    (empty? instructions) stacks
    :else (move stacks (:0 instructions))))

(def input {:stacks [["N" "Z"] ["D" "C" "M"] ["P"]]
            :instructions "move 1 from 2 to 1\nmove 3 from 1 to 3\nmove 2 from 2 to 1\nmove 1 from 1 to 2"})

(comment
  (parse-instructions (:instructions input))
  (conj [[1 2 3]] [4 5 6] [7 8])
  (move (:stacks input) {:amount 1 :from 1 :to 2})
  (move (:stacks input) {:amount 2 :from 1 :to 2})
  (move (:stacks input) {:amount 3 :from 1 :to 2})
  (move (:stacks input) {:amount 1 :from 0 :to 1})
  ;; (subvec [1 2 3] (instr 0) (instr :from))
  (supply-stacks (:stacks input) (:instructions (parse-instructions input))))