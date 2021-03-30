const amqp =  require('amqplib');
const express = require('express');
const bodyParser = require('body-parser');
const deferred = require("promise-defer")
const questions = require("./question.json");
const uuid = require('node-uuid');


const rabbitMqUri = 'amqp://localhost';
const port = 8000;

const app = express();

app.use(bodyParser.urlencoded({ extended: true }));


app.set('view engine', 'pug');
app.use(express.static(__dirname + "/public"));


app.get('/', (req, res) => {
    res.render('index', {
        title: 'NodeJS Client',
        local_pub: __dirname + '/pug.gif',
        originalUrl: req.originalUrl
    });

});

app.get('/question', (req, res) => {
    const question = questions.numbers.find(q => q.id === req.query.id);
    res.render('question', {
        title: 'NodeJS Client',
        question,
    });
});


app.post('/request', (req, res) => {
    console.log('Got body:', req.body);
    var input = req.body;
    // console.log(res);
    makeRpcRequest(input).then(result => {
        console.log(result);
        result = JSON.parse(result);
        // const filename = result.filename;
    });
    res.sendStatus(200);
});


function makeRpcRequest(channel, data) {
    const correlationId = uuid();
    console.log(data);

    function maybeAnswer(message) {
        if (message.properties.correlationId === correlationId) {
            deferred.resolve(message.content.toString());
        }
    }

    return channel.assertQueue('', { exclusive: true }).then(result => {
        const queue = result.queue;

        return channel.consume(queue, maybeAnswer, { noAck: false }).then(() => {

            console.log('Performing RPC request: ' + correlationId);
            console.log(JSON.stringify(data));
            channel.sendToQueue('rpc_queue', Buffer.from(JSON.stringify(data)), {
                correlationId: correlationId,
                replyTo: queue
            });
            return deferred.promise; 
        });
    });
}

function handleError(error) {
    // FIXME: Handle properly
    console.error(error.stack);
    console.log('Continuing …');
}

function startRabbitMq(port)
{
    amqp.connect(rabbitMqUri).then(connection => {

        return connection.createChannel().then(channel => {

            makeRpcRequest = makeRpcRequest.bind(null, channel);

            // (Would use HTTPS for production)
            const server = app.listen(port, () => {
                console.log('Amqp server running → PORT ' + port);
            });

            return server;
        })
        .catch(handleError);
    })
    .catch(error => {
        console.error('Could not connect to RabbitMQ server\n\n' + error.stack);
        process.exit(1);
    });
}

startRabbitMq(port);

const server = app.listen(7000, () => {
    console.log(`Express running → PORT ${server.address().port}`);
});

