(ns overseer.core
  (:use [overseer.parse :only [parse-file]]
        [overseer.calc :only [sum-up]])
  (:require [clojure.string :as str]))

(defrecord InPosition [desc search-tokens])
(defrecord DiffPosition [desc search-tokens dist])

(def in-positions
  [(->InPosition    "Gitschthaler"      ["Gitschthaler"])
   (->InPosition    "Zwinz"             ["Zwinz"])])

(def diff-positions
  [(->DiffPosition  "Gas"               ["Montana"]                  55/100)
   (->DiffPosition  "Versicherungen"    ["Versicher" "Landesbrand"]  1/2)
   (->DiffPosition  "Müll"              ["Abfall"]                   1/2)
   (->DiffPosition  "(Ab)wasser/Kanal"  ["Wasser"]                   3/4)
   (->DiffPosition  "Zaun"              ["Stemmer"]                  1/2)
   (->DiffPosition  "Heizung"           ["Bredl"]                    1/2)
   (->DiffPosition  "Kamin"             ["Kamin"]                    1/2)])

(defn contains-ic? [haystack needle]
  (re-find (re-pattern (str "(?i)" needle)) haystack))

(defn matching-desc? [haystack needles]
  (some (partial contains-ic? haystack) needles))

(def sheet
   (parse-file "/Users/u6f6o/Engineering/projects/private/overseer/in_out.csv"))

(doseq [in-position in-positions]
  (let [match-fn (fn [x](matching-desc? x (:search-tokens in-position)))
        sum-in (sum-up sheet match-fn :IN)]
    (prn (:desc in-position) sum-in)))

(doseq [diff-position diff-positions]
  (let [match-fn (fn [x](matching-desc? x (:search-tokens diff-position)))
        sum-in (sum-up sheet match-fn :IN)
        sum-out (sum-up sheet match-fn :OUT)
        diff (- sum-out sum-in)
        dist-ug (* diff (:dist diff-position))
        dist-zk (- diff dist-ug)]
    (prn (:desc diff-position) diff " - " dist-ug " - " dist-zk)))
