(ns overseer.core
  (:use [overseer.parse :only [parse-file]]
        [overseer.calc :only [sum-up]])
  (:require [clojure.string :as str]))

(defrecord InPosition [desc search-tokens])
(defrecord DiffPosition [desc search-tokens distribution])

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

(with-open [csv-file (io/reader "/Users/u6f6o/Engineering/projects/private/overseer/in_out.csv")]
  (let [sheet (parse-file csv-file)]
    (doseq [sum-position sum-positions]
      (let [desc (str (:desc sum-position) ": " )
            sum-in (sum-up sheet #(matching-desc? % (:search-tokens sum-position)) :IN)
            sum-out (sum-up sheet #(matching-desc? % (:search-tokens sum-position)) :OUT)]
        (prn (str desc (- sum-out sum-in)))))))

