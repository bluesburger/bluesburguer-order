# language: pt
Funcionalidade: Order

  Cenario: Criar pedido
    Quando criar um novo pedido
    Então o pedido é criado com sucesso

  Cenario: Buscar todos os pedidos
    Dado que um pedido foi criado
    Quando efetuar a busca de todos os pedidos
    Então o pedido deve ser apresentado na listagem

  Cenario: Buscar pedido por id
    Dado que um pedido foi criado
    Quando efetuar a busca de pedido por id
    Então o pedido deve ser apresentado

  Cenario: Buscar pedido por step
    Dado que um pedido foi criado
    Quando efetuar a busca de pedido por step
    Então o pedido deve ser apresentado na listagem

  Cenario: Atualizar step e fase do pedido pelo id
    Dado que um pedido foi criado
    Quando efetuar a atualização do step e da fase do pedido pelo id
    Então o pedido deve ser apresentado

  Cenario: Atualizar fase do pedido pelo id
    Dado que um pedido foi criado
    Quando efetuar a atualização da fase do pedido pelo id
    Então o pedido deve ser apresentado

  Cenario: Deletar pedido pelo id
    Dado que um pedido foi criado
    Quando deletar o pedido pelo id
    Quando efetuar a busca de pedido por id
    Então o pedido deletado nao deve mais ser encontrado
