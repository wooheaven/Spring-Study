| HTTP(method)      |
| :---------------- |
| GET(read)         |
| POST(create)      |
| PUT/PATCH(update) |
| DELETE(delete)    |


| URL(resource) | GET(read)           | POST(create) | PUT/PATCH(update)          | DELETE(delete)  |
| :------------ | :------------------ | :----------- | :------------------------- | :-------------- |
| sbb/items     | list up of items    | add a item   | -                          | -               |
| sbb/item/{id} | details of the item | -            | update details of the item | delete the item |

| URL(resource)            | GET(read)                                        | POST(create) | PUT/PATCH(update)          | DELETE(delete)  |
| :----------------------- | :----------------------------------------------- | :----------- | :------------------------- | :-------------- |
| sbb                      | return "안녕하세요. sbb에 오신 것을 환영합니다." | -            | -                          | -               |
| sbb/hello                | return "hello world"                             | -            | -                          | -               |
| sbb/questions            | list up of questions                             | -            | -                          | -               |
| sbb/question/detail/{id} | details of the question                          | -            | -                          | -               |
| sbb/users                | list up of users                                 | -            | -                          | -               |
| sbb/user/detail/{id}     | details of the user                              | -            | -                          | -               |
