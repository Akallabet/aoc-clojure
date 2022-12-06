(ns aoc22.day06
  (:require #?(:clj [clojure.java.io :as io]
               :cljs [nbb.core :refer [slurp await]])
            [clojure.string :as str]
            #?(:cljs [promesa.core :as p])))

(defn marker?
  [string]
  (let [letters (str/split string #"")]
    (= (count letters) (count (set letters)))))

(defn find-marker [subroutine letters start]
  (cond
    (marker? (subs subroutine start (+ start letters))) (+ start letters)
    :else (recur subroutine letters (+ start 1))))

(comment
  (marker? "mfgm")
  (marker? "abcd")
  (find-marker "mjqjpqmgbljsphdztnvjfqwrcgsmlb" 4 0)
  (find-marker "bvwbjplbgvbhsrlpgdmjqwftvncz" 4 0)
  (find-marker "nppdvjthqldpwncqszvftbrmjlhg" 4 0)
  (find-marker "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" 4 0)
  (find-marker "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" 4 0)
  (find-marker (slurp (io/resource "aoc22/day06.txt")) 4 0)
  (find-marker "mjqjpqmgbljsphdztnvjfqwrcgsmlb" 14 0)
  (find-marker "bvwbjplbgvbhsrlpgdmjqwftvncz" 14 0)
  (find-marker (slurp (io/resource "aoc22/day06.txt")) 14 0))