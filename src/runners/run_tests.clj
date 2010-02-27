(ns tests.run-tests
  (:use 
    clojure.contrib.test-is 
    (tests 
      test-traversal 
      test-basic-cuts 
      test-cut-traversal 
      test-distance
      test-fingerprints
      test-tanimoto)))
 
(run-tests 
  'tests.test-traversal
  'tests.test-basic-cuts
  'tests.test-cut-traversal
  'tests.test-distance
  'tests.test-fingerprints
  'tests.test-tanimoto
  )