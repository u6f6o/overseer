(ns overseer.core
  (:use [overseer.parse :only [file->sheet]]
        [overseer.util])
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def sheets
  (let [parse-csv (comp file->sheet io/file io/resource)]
    {:acc-stmts (parse-csv "account.csv")
     :distribution (parse-csv "distribution.csv")
     :expenses (parse-csv "expenses.csv")
     :income ()}))

(defn filter-expenses [acc-stmts expense]
  (let [tokens (str/split (:tokens expense) #",")]
    (filter #(matches-ignore-case? (:desc %) tokens) acc-stmts)))

(defn filter-income [acc-stmts holder]
  (filter #(matches-ignore-case? (:desc %) [holder]) acc-stmts))

(defn find-dist [expense dists]
  (let [type (:type expense)]
    (first (filter #(= type (:type %)) dists))))

(defn extract-holders [dists]
  (let [surname-keys
        (->> (map keys dists)
             (flatten)
             (filter #(.startsWith (name %) "surname-"))
             (distinct))]
    (reduce #(into %1 (vals (select-keys %2 surname-keys))) #{} dists)))

(def sum-column
  (memoize
   (fn [acc-stmts column]
     (->> (remove (comp str/blank? column) acc-stmts)
          (map (comp parse-number column))
          (apply +)))))

(defn sum-in [acc-stmts]
  (sum-column acc-stmts :in))

(defn sum-out [acc-stmts]
  (sum-column acc-stmts :out))

(defn calc-out-vs-in [acc-stmts]
  (- (sum-out acc-stmts) (sum-in acc-stmts)))

(defn calc-share [acc-stmts ratio]
  (* (calc-out-vs-in acc-stmts) ratio))

(defn summarize-expense [acc-stmts expense dist]
  (let [matching-stmts (filter-expenses acc-stmts expense)
        ratio-1 (parse-number (:ratio-1 dist))
        ratio-2 (parse-number (:ratio-2 dist))]
    {:sum-in  (sum-in matching-stmts)
     :sum-out (sum-out matching-stmts)
     :ratio   (eval ratio-1)
     :share-1 (calc-share matching-stmts ratio-1)
     :share-2 (calc-share matching-stmts ratio-2)}))

(defn summarize-expenses [sheets]
  (let [acc-stmts (:acc-stmts sheets)
        dists (:distribution sheets)
        expenses (:expenses sheets)]
    (map #(merge % (summarize-expense acc-stmts % (find-dist % dists))) expenses)))

(defn summarize-income [sheets]
  (let [acc-stmts (:acc-stmts sheets)
        holders (extract-holders (:distribution sheets))]
    {:holder-1 (sum-in (filter-income acc-stmts (first holders)))
     :holder-2 (sum-in (filter-income acc-stmts (second holders)))}))

(defn summarize [sheets]
  (assoc sheets
         :expenses (summarize-expenses sheets)
         :income (summarize-income sheets)
    ))


(println (:income (summarize sheets)))

