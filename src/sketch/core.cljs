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
(bind! ".ex-bars"
  (let [data [4, 8, 15]
        width 200 
        height 60
        bar-height 20
        scale-x (scale/linear 
                  :domain [0 (apply max data)] 
                  :range  [0 width])]

    [:div [:div.chart
     [:svg {:width width, :height height}
     
      (map-indexed (fn [i d]
        [:rect {:width (scale-x d) 
                :height bar-height, 
                :y (* i bar-height)}]) data)]]]))

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
     [:svg.chart {:width width, :height height
                  :xmlns "http://www.w3.org/2000/svg"}
      [:g {:transform "translate(10,15)"}

       
       ;; grid line labels 
       (map (fn [t] 
             (let[x (s t)] 
               [:text.rule {:x x :y 0 :dy -3 :text-anchor "middle"} t])) ticks)

       ;; grid lines
       (map (fn [t] 
             (let [x (s t)] 
               [:line {:x1 x :x2 x 
                       :y1 0 :y2 120 
                       :stroke "#ccc"}])) ticks)


       ;; bars
       (map-indexed (fn [i d]
        [:rect {:width (s d) :height h, :y (* i h)}
         [:svg:animate {:attributeName "width" 
                        :attributeType "XML"
                        :to (str (s d)) 
                        :from 0
                        :begin 0
                        :dur "600ms"}]]) data)
       ;; bar labels
       (map-indexed (fn [i d]
        [:text {:x (s d) 
                :y (+ (/ h 2) (* i h))
                :text-anchor "end"
                :dx -3
                :dy ".35em"} d]) data)]]]]))


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
      (<! (timeout 3000))
      (draw-chart! data)
        (recur (vec (cons (last data) (drop-last data)))))))




