(ns cemerick.utc-dates-test
  (:use cemerick.utc-dates
    clojure.test)
  (:import (java.util Date TimeZone))
  (:refer-clojure :exclude (format)))

(deftest test-dates
  (let [dt (java.util.Date. 1298978536123)
        dtstr (format dt)]
    (is (= "2011-03-01T11:22:16.123+0000" dtstr))
    (is (= dt (parse dtstr)))
    
    (is (= "11:22:16"
          (format dt "HH:mm:ss")))
    
    (is (= "2011-03-01T06:22:16.123-0500"
          (binding [*time-zone* (TimeZone/getTimeZone "EST")]
            (format dt))))))
