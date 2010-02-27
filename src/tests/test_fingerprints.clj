(ns tests.test-fingerprints
  (:use 
    clojure.contrib.test-is
    molecule-cut.fingerprints))

(deftest test-seen-before-generator
  (let [seen-before (seen-before-generator)]
    (is (= 0 (seen-before :foo)))
    (is (= 0 (seen-before :foo)))
    (is (= 1 (seen-before :bar)))
    (is (= 0 (seen-before :foo)))
  )
  (let [seen-before (seen-before-generator)]
    (is (= 0 (seen-before :bar)))
    (is (= 1 (seen-before :foo)))
  )
)
	
(deftest test-fingerprints
  (is (=
    [[0 1] [2 0]]
    (fingerprints [[:foo :bar] [:baz :foo]]))))
		
(deftest test-parse-fingerprints
  (is (= #{42} (parse-fingerprint "42")))
  (is (= #{1 2 42} (parse-fingerprint "1,2,42"))))