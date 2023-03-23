echo "Stating rabbit-1"

docker run -d --rm --net rabbit-net \
-v ${PWD}/config/rabbit-01/:/config/ \
-e RABBITMQ_CONFIG_FILE=/config/rabbitmq \
-e RABBITMQ_ERLANG_COOKIE=ETOBVBEFXUPGETFECHSQ \
--hostname rabbit-01 \
--name rabbit-01 \
-p 8081:15672 \
rabbitmq:3.6.6-management

echo "Stating rabbit-2"

docker run -d --rm --net rabbit-net \
-v ${PWD}/config/rabbit-02/:/config/ \
-e RABBITMQ_CONFIG_FILE=/config/rabbitmq \
-e RABBITMQ_ERLANG_COOKIE=ETOBVBEFXUPGETFECHSQ \
--hostname rabbit-02 \
--name rabbit-02 \
-p 8082:15672 \
rabbitmq:3.6.6-management

echo "Stating rabbit-3"

docker run -d --rm --net rabbit-net \
-v ${PWD}/config/rabbit-03/:/config/ \
-e RABBITMQ_CONFIG_FILE=/config/rabbitmq \
-e RABBITMQ_ERLANG_COOKIE=ETOBVBEFXUPGETFECHSQ \
--hostname rabbit-03 \
--name rabbit-03 \
-p 8083:15672 \
rabbitmq:3.6.6-management
