;   Copyright (c) Chas Emerick. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns 
  ^{:author "Chas Emerick <cemerick@snowtide.com>"
    :doc "A simple date formatting/parsing library that (by default)
          normalizes all formatted dates to UTC, and assumes all date
          strings are indicated in UTC.

          The default date format (held by `*date-format*`) is:

          yyyy-MM-dd'T'HH:mm:ss.SSSZ

          which can be dynamically rebound as desired.

          All dates emitted by `parse` will have their time zone shifted
          to the local environment's.

          If it wasn't obvious, this is not meant to be a general-purpose
          date/time library.  Its intended use is to offer a simple API
          useful for representing/consuming dates within
          lexicographically-sensitive database systems (e.g. couchdb,
          AWS SimpleDB, etc)."}
  cemerick.utc-dates
 (:import (java.util Date TimeZone)
   java.text.SimpleDateFormat)
 (:refer-clojure :exclude (format)))

(def *date-format* "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
(def UTC-tz (TimeZone/getTimeZone "UTC"))
(def *time-zone* UTC-tz)
(def local-tz (TimeZone/getDefault))

(defn now
  []
  (Date.))
 
(defprotocol Dates
  (format [date-data] [date-data string-format])
  (parse [date-data] [date-data string-format]))

(extend-type Number
  Dates
  (format
    ([num] (format num *date-format*))
    ([num string-format] (-> num long Date. (format string-format))))
  (parse
    ([num] (-> num long Date.))
    ([num string-format] (-> num long Date.))))

(extend-type Date
  Dates
  (format
    ([dt] (format dt *date-format*))
    ([dt string-format]
      (-> string-format
        SimpleDateFormat.
        (doto (.setTimeZone *time-zone*))
        (.format dt))))
  (parse 
    ([dt & _] dt)))

(extend-type String
  Dates
  (format [s & _] s)
  (parse
    ([s] (parse s *date-format*))
    ([s string-format]
      (-> string-format
        SimpleDateFormat.
        (.parse s)))))

(defn now-string
  []
  (format (now)))