# Ordering System Order
Responsável por resolver:

### Rest
CRUD de pedido:
- GET
- POST
- UPDATE
- DELETE

### Client
PaymentRestClient:
- newPayment(OrderId)
- getPaymentByOrderId() - opcional

### SqsPublisher
- publicação de evento StatusPedidoAtualizado(Step.Payment, Fase.In_Progress)


## Para utilizar

Instalação
- Instalar o make conforme tutorial
- Rodar localmente o comando `make`

Desinstalação
- Rodar localmente o comando `make down`