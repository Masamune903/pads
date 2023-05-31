# PaDS (Purchase and Delivery System)

・データベース名
	pads

・変更点
	- 商品にカテゴリを追加
	- 「商品」に「倉庫」との関連を作った
	- 「運送計画」を「運送」内で表現
		計画されたものは「運送」内に、start_time等がない状態で入る
		運送指示があったものは、delivery_member が入る
		運送が始まったものには start_time が入る
		運送が終わったら end_time が入る

	- 「運送」のリレーションスキーマ見直し
		delivery (
		　☆product_name: product.code(VARCHAR(20)),
		　☆product_model: product.model (VARCHAR(20)),
		　☆delivery_member: delivery_member.code (VARCHAR(20)),
		　　start_time (DATETIME),
		　　end_time (DATETIME),
		　　from_location: location.name (VARCHAR(50)),
		　　to_location: location.name (VARCHAR(50))
		)
		↓
		delivery (
		　☆product_name: product.code(VARCHAR(20)),
		　☆product_model: product.model (VARCHAR(20)),
		　☆from_location: location.name (VARCHAR(50)),
		　　to_location: location.name (VARCHAR(50)),
		　　delivery_member: delivery_member.code (VARCHAR(20)),
		　　start_time: DATETIME,
		　　end_time: DATETIME
		)

##発表について
### 手順
	- デモンストレーション（ユースケース図見せながら）
		- ユーザー（購入・受け取り場所登録・購入履歴・配達状況）：みはら
		- 配達員：まさむね
	- 細かい処理（ER見ながら）
		- 商品・ユーザー：もりもと
		- 配達・配達員：ささはら


TODO
・重複追加登録を消す

補足
・注文を確定した時に配送計画を立てることを言う
　距離で計算
　