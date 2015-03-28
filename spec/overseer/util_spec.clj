(ns overseer.util-spec
  (:require [speclj.core :refer :all]
            [overseer.util :refer :all]))

(describe "match one of the tokens in a given string"
  (it "no match on nil string"
    (should-not (matches-ignore-case? nil [])))
  (it "no match on nil col"
    (should-not (matches-ignore-case? "Foobar" nil)))
  (it "no match if none of the tokens matches"
    (should-not (matches-ignore-case? "Foobar" ["houlse" "car" "girl"])))
  (it "match if one of the tokens matches"
    (should (matches-ignore-case? "Foobar" ["2013" "2014" "OBA"]))))


(describe "parse given strings into numbers"
  (it "parse integer"
    (should= 42 (parse-number "42")))
  (it "parse ratio"
    (should= 1/2 (parse-number "5/10")))
  (it "parse double"
    (should= 6.66 (parse-number "6.66")))
  (it "parse octal number"
    (should= 0177 (parse-number "0177")))
  (it "parse hex number"
    (should= 0x7F (parse-number "0x7F"))))





