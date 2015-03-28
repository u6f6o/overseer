(ns overseer.parse-spec
  (:require [speclj.core :refer :all]
            [overseer.parse :refer :all]))

(describe "filter blank lines"
  (it "empty on nil"
    (should= [] (filter-empty nil)))
  (it "empty on empty col"
    (should= [] (filter-empty [])))
  (it "empty on blank content"
    (should= [] (filter-empty ["" "" "" ""])))
  (it "meaningful content stays"
    (should= [["Bruce" "Wayne"]] (filter-empty  [[""] ["Bruce" "Wayne"] []]))))


(describe "extract header information"
  (it "empty on nil"
    (should= [] (extract-header nil)))
  (it "empty on empty line"
    (should= [] (extract-header [])))
  (it "header transformed into keywords"
    (should= [:first-name :last-name :age]
             (extract-header ["FIRST_NAME" "LAST_NAME" "AGE"]))))


(describe "merge header and content"
  (it "empty on nil"
    (should= {} (merge-content nil nil)))
  (it "empty on no information"
    (should= {} (merge-content [] [])))
  (it "header merged with line"
    (should= {:name "Paul" :age "75"}
             (merge-content [:name :age] ["Paul" "75"]))))


(describe "csv lines are transformed to maps"
  (it "empty on nil"
    (should= [] (transform nil)))
  (it "empty on no lines"
    (should= [] (transform [])))
  (it "transform meaningful content"
    (should= [{:name "Paul" :age "25"} {:name "Cindy" :age "38"}]
             (transform [["NAME" "AGE"]["Paul" "25"]["Cindy" "38"]]))))


(describe "csv lines end up as list of maps"
  (it "empty on nil"
    (should= [] (lines->sheet nil)))
  (it "empty on no lines"
    (should= [] (lines->sheet [])))
  (it "filter empty lines and transform rest"
    (should= [{:name "Paul" :age "25"} {:name "Cindy" :age "38"}]
             (lines->sheet [[] ["NAME" "AGE"] ["" ""] ["Paul" "25"] [""] ["Cindy" "38"]]))))


