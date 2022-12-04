(ns aoc22.day04
  (:require #?(:clj [clojure.java.io :as io]
               :cljs [nbb.core :refer [slurp await]])
            [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.set]
            #?(:cljs [promesa.core :as p])))

(defn split-pairs [pairs] (str/split pairs #","))
(defn split-pair [pair]
  (->> (str/split pair #"-")
       (map edn/read-string)))

(defn pair-contains? [p1 p2] (and (<= (first p1) (first p2)) (>= (second p1) (second p2))))

(defn pairs-contained? [p1 p2]
  (or (pair-contains? p1 p2) (pair-contains? p2 p1)))

(defn within-pair? [n p] (and (<= (first p) n) (>= (second p) n)))

(defn pairs-overlap? [p1 p2]
  (or
   (within-pair? (first p1) p2)
   (within-pair? (second p1) p2)
   (within-pair? (first p2) p1)
   (within-pair? (second p2) p1)))

(defn assignements [pairs-list compare]
  (->> (str/split-lines pairs-list)
       (map split-pairs)
       (map #(map split-pair %))
       (filter #(apply compare %))
       count))

(def input "2-4,6-8\n2-3,4-5\n5-7,7-9\n2-8,3-7\n6-6,4-6\n2-6,4-8")

(comment
  (assignements input pairs-contained?)
  (assignements (slurp (io/resource "aoc22/day04.txt")) pairs-contained?)
  (assignements input pairs-overlap?)
  (assignements (slurp (io/resource "aoc22/day04.txt")) pairs-overlap?))