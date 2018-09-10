# spring-stateless-csrf-filter

Cookieを使ったCSRF防止用のSpring コンポーネントです。


## Examples

thymeleaf 2.x and 3.xに対するサンプルがあります。


1. `git clone git@github.com:cm-kazup0n/spring-statelss-csrf-filter.git`
1. `cd spring-statelss-csrf-filter`
1. `./gradlew :example-thymeleaf2:run` or `./gradlew :example-thymeleaf3:run`
1. ブラウザで [http://localhost:8080/users](http://localhost:8080/users) にアクセスします


- 表示されたフォームをサブミットすると正常に処理が終了します
- hidden inputの値を書き換えてからフォームをサブミットする（トークンの検証に失敗する）とエラー画面が表示されます。


## Components

下記のコンポーネントから構成されています。

- stateless-csrf-filter-core: springコンポーネントとして使用できるCSRFフィルター
- thymeleaf x: thymeleafに埋め込んで使用するためのコンポーネント
- example-thymeleaf x: thymeleafでの使用例


## 実装の詳細

検証の方式については下記記事を参照

https://dev.classmethod.jp/etc/implementation-of-play-csrf-token/

