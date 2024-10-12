```plantuml
@startuml

participant User as U
participant Web as W

title 게시글 조회

activate U
U -> W : 'localhost/sbb/hello'를 입력

activate W
W -> U : 'hello world'를 포함한 페이지를 제공

@enduml
```
