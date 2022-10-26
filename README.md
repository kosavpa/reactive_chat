# reactive_chat
В проекте использовались: RabbitMQ, Spring Boot. Архитектура приложения построенна на протоколе RSocket.
На стороне сервера все взаимодействия с клиентским приложением происходят посредством контроллеров(HandshakeController, MessageController, UsersController)
с определёнными стратегиями(request-responce, fire and forget, request-stream). Сообщение приходящее от клиента на сервер отправляются в exchange брокера сообщения,
где уже сообщение добавляются в очередь клиента для публикации в GUI, модель брокера - exchange fanout.  
