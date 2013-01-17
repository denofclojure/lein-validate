(ns quux.eggplant.foo
  (:require [clojure.test :refer :all]))

(defn foo [] nil)

(deftest t-failing
  (is (foo)))
