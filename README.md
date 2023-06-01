# PaDS (Purchase and Delivery System)

・データベース名
	pads

### Run UserApp
```PowerShell
$ cd C:\Users\masam\OneDrive\Documents\MyDocuments\Documents\学校\芝浦工業大学\授業\3学年\前期\プロジェクト演習12\pads;
	javac -d ./class -sourcepath ./src -encoding utf-8 ./src/UserApp.java;
	java -cp "C:/Program Files/MySQL/mysql-connector-j-8.0.33/mysql-connector-j-8.0.33.jar;./class" UserApp;
```

### Run DeliveryMemberApp
```PowerShell
$ cd C:\Users\masam\OneDrive\Documents\MyDocuments\Documents\学校\芝浦工業大学\授業\3学年\前期\プロジェクト演習12\pads;
	javac -d ./class -sourcepath ./src -encoding utf-8 ./src/DeliveryMemberApp.java;
	java -cp "C:/Program Files/MySQL/mysql-connector-j-8.0.33/mysql-connector-j-8.0.33.jar;./class" DeliveryMemberApp;
```

### Run DeliveryMemberConductorApp
```PowerShell
$ cd C:\Users\masam\OneDrive\Documents\MyDocuments\Documents\学校\芝浦工業大学\授業\3学年\前期\プロジェクト演習12\pads;
	javac -d ./class -sourcepath ./src -encoding utf-8 ./src/DeliveryMemberConductorApp.java;
	java -cp "C:/Program Files/MySQL/mysql-connector-j-8.0.33/mysql-connector-j-8.0.33.jar;./class" DeliveryMemberConductorApp;
```

VSCodeでの .md のプレビューは「Ctrl + Shift + V」