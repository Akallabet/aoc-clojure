(ns aoc22.day08
  (:require #?(:clj [clojure.java.io :as io]
               :cljs [nbb.core :refer [slurp await]])
            [clojure.string :as str]
            [clojure.edn :as edn]
            #?(:cljs [promesa.core :as p])))

(defn parse-input [lines]
  (->> (str/split-lines lines)
       (map #(str/split % #""))
       (map #(map edn/read-string %))
       (map #(map (fn [n] {:height n :visible false}) %))
       (map vec)
       vec))
(defn incr [n] (+ n 1))

(defn reverse-forest [v] (vec (map #(vec (reverse %)) v)))

(defn transpose [m]
  (apply mapv vector m))

(defn rotate-forest [f]
  (->> (reverse-forest f)
       transpose))

(defn mark-trees-visible
  ([row] (mark-trees-visible row 0 -1))
  ([row pos tallest]
   (cond
     (= pos (count row)) row
     (:visible (get row pos)) row
     :else (let [tree (get row pos)
                 visible (> (:height tree) tallest)]
             (recur
              (if-not visible
                row
                (assoc
                 row
                 pos
                 (assoc tree :visible true)))
              (incr pos)
              (if visible (:height tree) tallest))))))

(defn mark-trees-visible-forest
  ([forest] (mark-trees-visible-forest forest 0))
  ([forest pos]
   (cond
     (= pos (count forest)) forest
     :else (recur
            (assoc forest pos (mark-trees-visible (get forest pos)))
            (incr pos)))))

(defn mark-trees-visible-forest-all-sides [forest]
  (->> (mark-trees-visible-forest forest)
       reverse-forest
       mark-trees-visible-forest
       transpose
       mark-trees-visible-forest
       reverse
       vec
       reverse-forest
       mark-trees-visible-forest
       reverse-forest
       transpose))

(defn visible-trees [forest]
  (->> (mark-trees-visible-forest-all-sides forest)
       flatten
       (filter :visible)
       count))

(def input "30373\n25512\n65332\n33549\n35390")

(comment
  (parse-input input)
  (mark-trees-visible [{:height 2 :visible false}
                       {:height 5 :visible false}
                       {:height 5 :visible false}
                       {:height 1 :visible false}
                       {:height 2 :visible false}])
  (mark-trees-visible [{:height 1 :visible false} {:height 0 :visible false} {:height 1 :visible false}])
  (mark-trees-visible-forest [{:height 1 :visible false} {:height 1 :visible false} {:height 0 :visible false}])
  (mark-trees-visible-forest-all-sides [{:height 1 :visible false} {:height 1 :visible false} {:height 1 :visible false}])
  (mark-trees-visible-forest-all-sides [[{:height 1 :visible false} {:height 1 :visible false}] [{:height 1 :visible false} {:height 1 :visible false}]])
  (mark-trees-visible-forest-all-sides (parse-input "1111\n1001\n1001\n1111"))
  (visible-trees (parse-input "1111\n1001\n1001\n1111"))
  (mark-trees-visible-forest (transpose (transpose (parse-input input))))
  (mark-trees-visible-forest-all-sides (parse-input input))
  (visible-trees (parse-input input))
  (visible-trees (parse-input (slurp (io/resource "aoc22/day08.txt"))))
  (= (rotate-forest [[1 2] [3 4]]) [[2 4] [1 3]])
  (= (reverse-forest [[1 2] [3 4]]) [[2 1] [4 3]])
  (reverse-forest (transpose [[1 2] [3 4]]))
  (rotate-forest (rotate-forest [[1 2] [3 4]]))
  (vec (reverse [[1 2] [3 4]])))