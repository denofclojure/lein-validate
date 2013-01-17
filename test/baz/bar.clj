(ns baz.bar
  (:require [clojure.test :refer :all]))

(defn test-stuff [] nil)

(deftest t-things
  (is (nil? (test-stuff))))
