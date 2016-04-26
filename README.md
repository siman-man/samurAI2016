## samurAI2016
samurai coding 2016 6th place

## My strategy

It's greedy.

1. All action pattern simulate.
2. Calculate evaluation of each action.
3. Select most highest score action.

## ここから日本語

今回のゲームでは「潜伏時の相手をどこまで正確に位置推定出来るか」が大きなポイントでした。

自分はまず、ゲームのシミュレーションサーバを自作して、そこで自己対戦を繰り返し行いました。
そして対戦結果を集計して、AIの攻撃のパターンからどの位置に潜伏しているかの予測マップを作成しました。

XXXPatternを名前の付いているファイルがその予測パターンを記録したファイルです（ここは自動生成するようにしました）
結果としては、自己対戦だったのでちょっと過学習気味になってしまいましたね

