(ns sketch.core
  (:require
    [c2.core :refer [unify]]
    [c2.scale :as scale]
    [c2.ticks :as ticks]
    [goog.dom :as dom]
    [goog.events :as events]
    [goog.events.EventType]
    [cljs.core.async :refer [>! <! chan timeout alts!]])
  (:require-macros 
    [c2.util :refer [bind! pp]]
    [cljs.core.async.macros :refer [go]]))

;; --------------------- 
;; Example 1
;; a simple list
;;

(bind! ".ex-basic"  
  [:div
   [:ol
     [:li "wibble"]
     [:li "wobble"]
     [:li "woooo"]]])

;; --------------------- 
;; Example 3
;; ins and outs 
;;

(defn unify-example [data]
  (bind! ".ex-keys"
    [:div
      [:p (unify data 
                 (fn [d] [:p (str "(" d ")" (.now js/Date))])
                 :key-fn (fn [d, i] d))]]))

(events/listen 
  (dom/getElement "btn-reset") "click"
  (fn [] 
    (dom/removeChildren (dom/getElementByClass "ex-keys"))
    (unify-example ["a" "b" "c"])))

(events/listen 
  (dom/getElement "btn-poke") "click"
  #(unify-example ["d" "c" "b"]))


(unify-example ["a" "b" "c"])

;; --------------------- 
;; Example 4
;; SVG chart
;;
(bind! ".ex4"
  (let [data [4, 8, 15]
        width 200 height 100
        s (scale/linear :domain [0 (apply max data)] 
                        :range  [0 width])
        h 20]

    [:div [:div.chart
     [:svg {:width width, :height height}
     
      (map-indexed (fn [i d]
        [:rect {:width (s d) 
                :height h, 
                :y (* i h)}]) data)]]]))

;; --------------------- 
;; Example 5
;; SVG chart++
;;
(bind! ".ex5"
  (let [data [4, 8, 15, 16, 23, 42] 
        width 440 height 140
        domain [0 (apply max data)]
        s (scale/linear :domain domain 
                        :range  [0 (- width 20)])
        h 20
        ticks (:ticks (ticks/search domain))]

    [:div [:div
     [:svg.chart {:width width, :height height}
      [:g {:transform "translate(10,15)"}

       ;; bars
      (map-indexed (fn [i d]
        [:rect {:width (s d) :height h, :y (* i h)}]) data)

       ;; bar labels
      (map-indexed (fn [i d]
        [:text {:x (s d) 
                :y (+ (/ h 2) (* i h))
                :text-anchor "end"
                :dx -3
                :dy ".35em"} d]) data)
     

       ;; grid lines
      (map (fn [t] 
             (let [x (s t)] 
               [:line {:x1 x :x2 x 
                       :y1 0 :y2 120 
                       :stroke "#ccc"}])) 
           ticks)

       ;; grid line labels 
      (map (fn [t] 
             (let[x (s t)] 
               [:text.rule {:x x :y 0 :dy -3 :text-anchor "middle"} t])) 
           ticks)]]]]))



(defn draw-chart! [data]
  (bind! ".ex6"
    [:div [:div.chart  
      (let [size (scale/linear :domain [0 20]
                               :range [0 100])]
        (unify (map-indexed (fn [i d] [i d]) data)
          (fn [[i d]] [:div.col 
                   {:style 
                    {:height (str (size d) "px")
                     :left (str (* i 21) "px")}}])))]]))


(let [seed (range 10)]
  (draw-chart! seed)
  (go 
    (loop [data seed] 
      (<! (timeout 5000))
      (draw-chart! data)
        (recur (vec (cons (last data) (drop-last data)))))))




